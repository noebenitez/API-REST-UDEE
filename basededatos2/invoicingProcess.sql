

    # ------------------ Invoicing process for one meter ----------------------
    DROP PROCEDURE invoicing_process;
    DELIMITER $$
    CREATE PROCEDURE invoicing_process (serial_number_entry varchar(50))
    BEGIN

        DECLARE vFinished int DEFAULT 0;		DECLARE vIdMeter int DEFAULT 0;
        DECLARE vIdAddress int;					DECLARE vIdCustomer int;
        DECLARE vTariffType varchar(50);		DECLARE vDueDate datetime;
        DECLARE vIdInvoice int;					DECLARE vMId int;
        DECLARE vMMeasurement float;			DECLARE vMPrice float;
        DECLARE vTotalPrice float DEFAULT 0;	DECLARE vTotalConsumption float DEFAULT 0;
        DECLARE vMinDatetime datetime;			DECLARE vMaxDatetime datetime;


        DECLARE cur_invoicing CURSOR FOR select mes.id, mes.measurement, mes.price from
        measurements mes INNER JOIN meters met ON mes.id_meter = met.id
                                         WHERE mes.id_invoice is null and met.serial_number = serial_number_entry;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET vFinished = 1;

    SELECT id INTO vIdMeter FROM meters WHERE serial_number = serial_number_entry;

    IF (vIdMeter <> 0) THEN
            #Transaction to ensure the update of the measurements that are part of the invoice
                                       START TRANSACTION;
    SELECT id_address INTO vIdAddress FROM meters WHERE id = vIdMeter;
    SELECT id_customer INTO vIdCustomer FROM addresses WHERE id = vIdAddress;
    SELECT tariff_type INTO vTariffType FROM
        addresses a INNER JOIN tariffs t ON a.id_tariff = t.id
    WHERE a.id = vIdAddress;
    SET vDueDate = DATE_ADD(NOW(), INTERVAL 15 DAY);

                # Insert the invoice and save the id to update values. If there aren't any measurements to pay, the invoice in generated in 0 with status 'PAID'.
                INSERT into invoices(id_customer, serial_number, tariff_type, initial_measurement, final_measurement, total_consumption, total_to_paid, due_date, payment_status)
                    VALUES (vIdCustomer, serial_number_entry, vTariffType, 0, 0, 0, 0, vDueDate, "UNPAID");
                SET vIdInvoice = last_insert_id();
                OPEN cur_invoicing;
                FETCH cur_invoicing INTO vMId, vMMeasurement, vMPrice;

                #For each measurement the price and the consumption of kwh are added to the totals and the id_invoice is setted
                WHILE(vFinished = 0) DO
                    SET vTotalPrice = vTotalPrice + vMPrice;
                    SET vTotalConsumption = vTotalConsumption + vMMeasurement;
                    UPDATE measurements SET id_invoice = vIdInvoice WHERE id = vMId;
                    FETCH cur_invoicing INTO vMId, vMMeasurement, vMPrice;
                END WHILE;

                #When all the measurements to invoice were setted with the id_invoice, the rest of the invoice information is setted
                SELECT MIN(m_datetime) INTO vMinDatetime FROM measurements WHERE id_invoice = vIdInvoice;
                SELECT MAX(m_datetime) INTO vMaxDatetime FROM measurements WHERE id_invoice = vIdInvoice;
                UPDATE invoices SET total_consumption = vTotalConsumption,
                                    total_to_paid = vTotalPrice,
                                    initial_measurement = (SELECT measurement FROM measurements WHERE id_invoice = vIdInvoice AND m_datetime = vMinDatetime),
                                    initial_datetime = vMinDatetime,
                                    final_measurement = (SELECT measurement FROM measurements WHERE id_invoice = vIdInvoice AND m_datetime = vMaxDatetime),
                                    final_datetime = vMaxDatetime
                                    WHERE id = vIdInvoice;
                #If the total price is 0 then is considerated paid.
                IF (vTotalPrice = 0) THEN
                    UPDATE invoices SET payment_status = 'PAID' WHERE id = vIdInvoice;
                END IF;
                CLOSE cur_invoicing;
            COMMIT;
        END IF;
    END
    $$

    #  ------------------ Invoicing process for all meters  ----------------------

    DROP PROCEDURE IF EXISTS invoicing_all_process;
    DELIMITER $$
    CREATE PROCEDURE invoicing_all_process()
    BEGIN
        DECLARE vFinished int DEFAULT 0;
        DECLARE vSerialNumber varchar(50);
        DECLARE cur_invoicing_all CURSOR FOR select serial_number from meters;
        DECLARE CONTINUE HANDLER FOR NOT FOUND SET vFinished = 1;

        OPEN cur_invoicing_all;
        FETCH cur_invoicing_all INTO vSerialNumber;
        #Invoicing for each meter
        WHILE(vFinished = 0) DO
            CALL invoicing_process(vSerialNumber);
            FETCH cur_invoicing_all INTO vSerialNumber;
        END WHILE;
        CLOSE cur_invoicing_all;
    END
    $$

    # ------------------ Event to call invoicing process each month  ----------------------
    SET GLOBAL event_scheduler = ON;
    CREATE EVENT e_monthly_invoicing
    ON SCHEDULE EVERY 1 MONTH
    STARTS '2021-01-01 00:00:00'
    DO CALL invoicing_all_process;