

# -------------------- Trigger to calculate measurement price on INSERT -------------

DROP TRIGGER tbi_measurement;
DELIMITER $$
CREATE TRIGGER tbi_measurement BEFORE INSERT ON measurements FOR EACH ROW
BEGIN
    DECLARE vLastDatetime datetime;
    DECLARE vLastMeasurement float;
    DECLARE vTariff float;
    SELECT MAX(m_datetime) INTO vLastDatetime FROM measurements WHERE id_meter = new.id_meter AND m_datetime < new.m_datetime;
    SELECT tariff INTO vTariff FROM
        meters m INNER JOIN addresses a ON m.id_address = a.id
                 INNER JOIN tariffs t ON a.id_tariff = t.id
    WHERE m.id = new.id_meter;
    #If exists a previous measurement then the kws to invoice is the difference between the new and the last measurement
    IF (vLastDatetime IS NOT NULL) THEN
    SELECT measurement INTO vLastMeasurement FROM measurements WHERE id_meter = new.id_meter AND m_datetime = vLastDatetime;
    ELSE
		SET vLastMeasurement = 0;
END IF;
SET new.price = (new.measurement - vLastMeasurement) * vTariff;
END
$$



# -------------------- Trigger to recalculate measurement price on UPDATE and generated adjustment invoice if is needed -------------

DROP TRIGGER tbu_tariff;
DELIMITER $$
CREATE TRIGGER tbu_tariff BEFORE UPDATE ON tariffs FOR EACH ROW
BEGIN
    DECLARE vFinished int DEFAULT 0;
    DECLARE vIdAddress int;
    DECLARE vIdTariff int;
    DECLARE vIdCustomer int;
    DECLARE vNewTotalPrice float DEFAULT 0;
    DECLARE vIdMeter int;
    DECLARE vTariffType varchar(50);
    DECLARE cur_adjustment_tariffs CURSOR FOR select id, id_tariff, id_customer from addresses;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET vFinished = 1;

    OPEN cur_adjustment_tariffs;
    FETCH cur_adjustment_tariffs INTO vIdAddress, vIdTariff, vIdCustomer;
    #For each address check if it has the updated tariff
    WHILE (vFinished = 0) DO
            IF (vIdTariff = old.id) THEN
                SELECT id INTO vIdMeter FROM meters WHERE id_address = vIdAddress;
                #If there are any measurements from the meter the adjustment occurs
                IF ((SELECT COUNT(*) FROM measurements WHERE id_meter = vIdMeter) > 0) THEN
                    SELECT tariff_type INTO vTariffType FROM tariffs WHERE id = vIdTariff;
                    #This procedure change the price of each measurement and generate a adjustment invoice if is needed
                    CALL adjust_measurements(vIdMeter, old.tariff, new.tariff, vTariffType, vIdCustomer);
                END IF;
            END IF;
            FETCH cur_adjustment_tariffs INTO vIdAddress, vIdTariff, vIdCustomer;
        END WHILE;
END
$$


# --------------- Update tariffs: Procedure to change the price of measurements and genera adjustment invoice ----------------------

DROP PROCEDURE IF EXISTS adjust_measurements;
DELIMITER $$
CREATE PROCEDURE adjust_measurements(id_meter_entry int, oldPrice float, newPrice float, tariff_type_entry varchar(50), id_customer_entry int)
BEGIN

    DECLARE vFinished int DEFAULT 0;				DECLARE vId int;
    DECLARE vIdMeter int;							DECLARE vIdInvoice int;
    DECLARE vMeasurement float;						DECLARE vPrice float;
    DECLARE vNewPrice float;						DECLARE vDifference float DEFAULT 0;
    DECLARE vSerialNumber varchar(50);				DECLARE vMinDatetime datetime;
    DECLARE vMaxDatetime datetime;

    DECLARE cur_adjustment_measurements CURSOR FOR select id, id_meter, id_invoice, measurement, price from measurements;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET vFinished = 1;

    OPEN cur_adjustment_measurements;
    FETCH cur_adjustment_measurements INTO vId, vIdMeter, vIdInvoice, vMeasurement, vPrice;
    #Search the measurements from the meter to update the price value
    WHILE(vFinished = 0) DO
            IF (vIdMeter = id_meter_entry) THEN
                SET vNewPrice = (vPrice / oldPrice) * newPrice;
                UPDATE measurements SET price = vNewPrice WHERE id = vId;
                # If the measurement was already invoiced, it will be part of the adjustment invoice
                IF (vIdInvoice is not null) THEN
                    SET vDifference = vDifference + (vNewPrice - vPrice);
                END IF;
            END IF;
            FETCH cur_adjustment_measurements INTO vId, vIdMeter, vIdInvoice, vMeasurement, vPrice;
        END WHILE;
    #If the difference is not 0 it means that measurements already invoiced had changed their values, so an adjustment invoice is genereted.
    IF (vDifference <> 0) THEN
        SELECT serial_number INTO vSerialNumber FROM meters WHERE id = id_meter_entry;
        SELECT MIN(m_datetime) INTO vMinDatetime FROM measurements WHERE id_meter = id_meter_entry AND id_invoice is not null;
        SELECT MAX(m_datetime) INTO vMaxDatetime FROM measurements WHERE id_meter = id_meter_entry AND id_invoice is not null;

        INSERT into invoices(id_customer, serial_number, tariff_type, initial_datetime, final_datetime, initial_measurement, final_measurement, total_to_paid, due_date, payment_status)
        VALUES (id_customer_entry, vSerialNumber, tariff_type_entry, vMinDatetime, vMaxDatetime,
                (SELECT measurement FROM measurements WHERE m_datetime = vMinDatetime),
                (SELECT measurement FROM measurements WHERE m_datetime = vMaxDatetime),
                vDifference,
                DATE_ADD(NOW(), INTERVAL 15 DAY),
                "UNPAID");
    END IF;
    CLOSE cur_adjustment_measurements;
END
$$