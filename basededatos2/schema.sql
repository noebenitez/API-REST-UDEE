

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
                          total_to_paid FLOAT NOT NULL,
                          due_date datetime,
                          payment_status varchar(50) NOT NULL,

                          constraint pk_invoice PRIMARY KEY (id),
                          constraint fk_invoice_user FOREIGN KEY (id_customer) references users(id)
    );


    CREATE TABLE measurements(
                             id int NOT NULL AUTO_INCREMENT,
                             id_meter int NOT NULL,
                             id_invoice int,
                             measurement float UNSIGNED NOT NULL,
                             price float,
                             m_datetime datetime NOT NULL,
                             constraint pk_measurement PRIMARY KEY (id),
                             constraint fk_measurement_meter FOREIGN KEY (id_meter) references meters(id),
                             constraint fk_measurement_invoice FOREIGN KEY (id_invoice) references invoices(id)
    );
