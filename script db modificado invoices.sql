CREATE DATABASE udee;
USE udee;

CREATE TABLE models(
                       id int NOT NULL AUTO_INCREMENT,
                       model varchar(50) NOT NULL,
                       constraint pk_model PRIMARY KEY (id),
                       constraint unq_model UNIQUE (model)
);

CREATE TABLE brands(
                       id int NOT NULL AUTO_INCREMENT,
                       brand varchar(50) NOT NULL,
                       constraint pk_brand PRIMARY KEY (id),
                       constraint unq_brand UNIQUE (brand)
);

CREATE TABLE users(
                      id int NOT NULL AUTO_INCREMENT,
                      user_type varchar(50) NOT NULL,
                      dni varchar(50) NOT NULL,
                      firstname varchar(50) NOT NULL,
                      lastname varchar(50) NOT NULL,
                      DROP DATABASE udeebd;
CREATE DATABASE udeebd;
USE udeebd;

CREATE TABLE models(
                       id int NOT NULL AUTO_INCREMENT,
                       model varchar(50) NOT NULL,
                       constraint pk_model PRIMARY KEY (id),
                       constraint unq_model UNIQUE (model)
);


CREATE TABLE brands(
                       id int NOT NULL AUTO_INCREMENT,
                       brand varchar(50) NOT NULL,
                       constraint pk_brand PRIMARY KEY (id),
                       constraint unq_brand UNIQUE (brand)
);

CREATE TABLE users(
                      id int NOT NULL AUTO_INCREMENT,
                      user_type varchar(50) NOT NULL,
                      dni varchar(50) NOT NULL,
                      firstname varchar(50) NOT NULL,
                      lastname varchar(50) NOT NULL,
                      username varchar(50) NOT NULL,
                      u_password varchar(50) NOT NULL,
                      constraint pk_user PRIMARY KEY (id),
                      constraint unq_dni UNIQUE (dni),
                      constraint unq_username UNIQUE (username)
);

CREATE TABLE tariffs(
                        id int NOT NULL AUTO_INCREMENT,
                        tariff_type varchar(50) NOT NULL,
                        tariff float UNSIGNED,
                        constraint pk_tariff PRIMARY KEY (id)
);

CREATE TABLE addresses(
                          id int NOT NULL AUTO_INCREMENT,
                          address varchar(50) NOT NULL,
                          id_tariff int NOT NULL,
                          id_customer int NOT NULL,
                          constraint pk_address PRIMARY KEY (id),
                          constraint fk_address_user foreign key (id_customer) references users(id),
                          constraint fk_address_tariff foreign key (id_tariff) references tariffs(id),
                          constraint unq_adress UNIQUE (address)
);


CREATE TABLE meters(
                       id int NOT NULL AUTO_INCREMENT,
                       serial_number varchar(50) NOT NULL,
                       m_password varchar(50) NOT NULL,
                       id_model int NOT NULL,
                       id_brand int NOT NULL,
                       id_address int NOT NULL,
                       constraint pk_meter PRIMARY KEY (id),
                       constraint fk_meter_model FOREIGN KEY (id_model) references models(id),
                       constraint fk_meter_brand FOREIGN KEY (id_brand) references brands(id),
                       constraint fk_meters_address FOREIGN KEY (id_address) references addresses(id),
                       constraint unq_serial_number UNIQUE (serial_number)
);

CREATE TABLE invoices (
                          id INT NOT NULL AUTO_INCREMENT,
                          id_customer INT NOT NULL,
                          serial_number varchar(50) NOT NULL,
                          tariff_type varchar(50) NOT NULL,
                          initial_measurement FLOAT,
                          final_measurement FLOAT,
                          initial_datetime datetime,
                          final_datetime datetime,
                          total_consumption FLOAT UNSIGNED NOT NULL,
                          total_to_paid FLOAT UNSIGNED NOT NULL,
                          due_date datetime,
                          payment_status varchar(50) NOT NULL,
                          
                          constraint pk_invoice PRIMARY KEY (id),
                          constraint fk_invoice_user FOREIGN KEY (id_customer) references users(id),
);

CREATE TABLE measurements(
                             id int NOT NULL AUTO_INCREMENT,
                             id_meter int NOT NULL,
                             id_invoice int,
                             measurement float UNSIGNED NOT NULL,
                             price float UNSIGNED,
                             m_datetime datetime NOT NULL,
                             constraint pk_measurement PRIMARY KEY (id),
                             constraint fk_measurement_meter FOREIGN KEY (id_meter) references meters(id),
                             constraint fk_measurement_invoice FOREIGN KEY (id_invoice) references invoices(id)
);


# ------------------ Invoicing process for one meter ----------------------
DROP PROCEDURE invoicing_process;
DELIMITER $$
CREATE PROCEDURE invoicing_process (meter_id_entry int)
BEGIN
    DECLARE vFinished int DEFAULT 0;
    DECLARE vIdAddress int;
    DECLARE vIdCustomer int;
    DECLARE vIdTariff int;
    DECLARE vIdInvoice int;
    DECLARE vMId int;
    DECLARE vMMeasurement float;
    DECLARE vMPrice float;
    DECLARE vTotalPrice float DEFAULT 0;
    DECLARE vTotalConsumption float DEFAULT 0;
    DECLARE vMinDatetime datetime;
    DECLARE vMaxDatetime datetime;

	DECLARE cur_invoicing CURSOR FOR select id, measurement, price from measurements where id_invoice is null and meter_id_entry = id_meter;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET vFinished = 1;

    SELECT id_address INTO vIdAddress FROM meters WHERE id = meter_id_entry;
    SELECT id_customer INTO vIdCustomer FROM addresses WHERE id = vIdAddress;
    SELECT id_tariff INTO vIdTariff FROM addresses WHERE id = vIdAddress;

    #Insert the invoice and save the id to update values. If there aren't any measurements to pay, the invoice in generated in 0.
	INSERT into invoices(id_meter, id_customer, id_tariff, initial_measurement, final_measurement, total_consumption, total_to_paid) VALUES (meter_id_entry, vIdCustomer, vIdTariff, 0, 0, 0, 0);
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

	SELECT MIN(m_datetime) INTO vMinDatetime FROM measurements WHERE id_invoice = vIdInvoice;
	SELECT MAX(m_datetime) INTO vMaxDatetime FROM measurements WHERE id_invoice = vIdInvoice;
	UPDATE invoices SET total_consumption = vTotalConsumption,
						total_to_paid = vTotalPrice,
						initial_measurement = (SELECT measurement FROM measurements WHERE id_invoice = vIdInvoice AND m_datetime = vMinDatetime),
						initial_datetime = vMinDatetime,
						final_measurement = (SELECT measurement FROM measurements WHERE id_invoice = vIdInvoice AND m_datetime = vMaxDatetime),
						final_datetime = vMaxDatetime
						WHERE id = vIdInvoice;
	CLOSE cur_invoicing;
END
$$

#  ------------------ Invoicing process for all meters  ----------------------

DROP PROCEDURE IF EXISTS invoicing_all_process;
DELIMITER $$
CREATE PROCEDURE invoicing_all_process()
BEGIN
	DECLARE vFinished int DEFAULT 0;
    DECLARE vIdMeter int;
	DECLARE cur_invoicing_all CURSOR FOR select id from meters;
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET vFinished = 1;

    OPEN cur_invoicing_all;
	FETCH cur_invoicing_all INTO vIdMeter;
    WHILE(vFinished = 0) DO
		CALL invoicing_process(vIdMeter);
		FETCH cur_invoicing_all INTO vIdMeter;
	END WHILE;
	CLOSE cur_invoicing_all;
END
$$

# ------------------ Event to call invoicing process each month  ----------------------
SET GLOBAL event_scheduler = ON;
CREATE EVENT e_monthly_invoicing
ON SCHEDULE EVERY 5 SECOND
STARTS '2021-01-01 00:00:00'
DO CALL invoicing_all_process;
#DROP EVENT e_monthly_invoicing
#show events;

insert into models(model) values("Model1");
insert into brands(brand) values("Brand1");
insert into users (user_type, dni, firstname, lastname, username, u_password) values("client", "11222333", "John", "Doe", "john1", "1122");
insert into users (user_type, dni, firstname, lastname, username, u_password) values("client", "1332221", "Jane", "Doe", "jane1", "122");
insert into tariffs(tariff_type, tariff) values("residential", 112.2);
insert into addresses(address, id_tariff, id_customer) values("Street 111", 1, 1);
insert into addresses(address, id_tariff, id_customer) values("Avenue 222", 1, 2);
insert into meters(serial_number, m_password, id_model, id_brand, id_address) values("11122asd", "P4ss", 1, 1, 1);
insert into meters(serial_number, m_password, id_model, id_brand, id_address) values("dd2222", "P12", 1, 1, 2);
insert into measurements(id_meter, measurement, price, m_datetime) values(1, 12.2, 33.2, "2020-03-10 11:22:11");
insert into measurements(id_meter, measurement, price, m_datetime) values(1, 33.4, 63.4, "2020-03-10 11:22:33");
insert into measurements(id_meter, measurement, price, m_datetime) values(1, 12.24, 73.6, "2012-03-10 11:22:11");
insert into measurements(id_meter, measurement, price, m_datetime) values(1, 2.7, 5.5, "2021-03-10 11:22:11");
insert into measurements(id_meter, measurement, price, m_datetime) values(1, 2.7, 5.5, "2021-03-10 11:22:12");
insert into measurements(id_meter, measurement, price, m_datetime) values(2, 6.2, 36.3, "2022-03-10 11:22:11");
insert into measurements(id_meter, measurement, price, m_datetime) values(2, 12.2, 33.2, "2018-03-10 11:22:11");
insert into measurements(id_meter, measurement, price, m_datetime) values(2, 5.2, 22.4, "2020-03-3 11:22:11");
insert into measurements(id_meter, measurement, price, m_datetime) values(1, 3.5, 1.1, "2021-03-14 11:22:12");
insert into measurements(id_meter, measurement, price, m_datetime) values(1, 2.5, 4.2, "2021-08-10 11:22:12");

select * from meters;
select * from invoices;
select * from measurements;

CALL invoicing_all_process();