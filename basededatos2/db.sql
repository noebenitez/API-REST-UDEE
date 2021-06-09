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

CREATE TABLE adresses(
                         id int NOT NULL AUTO_INCREMENT,
                         adress varchar(50) NOT NULL,
                         id_tariff int NOT NULL,
                         id_customer int NOT NULL,
                         constraint pk_adress PRIMARY KEY (id),
                         constraint fk_adress_user foreign key (id_customer) references users(id),
                         constraint fk_adress_tariff foreign key (id_tariff) references tariffs(id),
                         constraint unq_adress UNIQUE (adress)
);


CREATE TABLE meters(
                       id int NOT NULL AUTO_INCREMENT,
                       serial_number varchar(50) NOT NULL,
                       m_password varchar(50) NOT NULL,
                       id_model int NOT NULL,
                       id_brand int NOT NULL,
                       id_adress int NOT NULL,
                       constraint pk_meter PRIMARY KEY (id),
                       constraint fk_meter_model FOREIGN KEY (id_model) references models(id),
                       constraint fk_meter_brand FOREIGN KEY (id_brand) references brands(id),
                       constraint fk_meters_adress FOREIGN KEY (id_adress) references adresses(id),
                       constraint unq_serial_number UNIQUE (serial_number)
);


CREATE TABLE measurements(
                             id int NOT NULL AUTO_INCREMENT,
                             id_meter int NOT NULL,
                             measurement float UNSIGNED NOT NULL,
                             price float UNSIGNED NOT NULL,
                             m_datetime datetime NOT NULL,
                             constraint pk_measurement PRIMARY KEY (id),
                             constraint fk_measurement_meter FOREIGN KEY (id_meter) references meters(id)
);

ALTER TABLE measurements ADD id_invoice int not null;
ALTER TABLE measurements ADD constraint fk_measurement_invoice FOREIGN KEY (id_invoice) references invoices(id);
ALTER TABLE measurements ADD constraint fk_measurement_invoice FOREIGN KEY (id_invoice) references invoices(id);

CREATE TABLE invoices(
                         id int NOT NULL AUTO_INCREMENT,
                         id_meter int NOT NULL,
                         initial_measurement float NOT NULL,
                         final_measurement float NOT NULL,
                         total_consumption float UNSIGNED NOT NULL,
                         total_to_paid float UNSIGNED NOT NULL,
                         constraint pk_invoice PRIMARY KEY (id),
                         constraint fk_invoice_neter FOREIGN KEY (id_meter) references meters(id)
);