insert into models(model) values("Model1");
insert into brands(brand) values("Brand1");
insert into users (user_type, dni, firstname, lastname, username, u_password) values("Client", "11222333", "John", "Doe", "john1", "1122");
insert into users (user_type, dni, firstname, lastname, username, u_password) values("Client", "1332221", "Jane", "Doe", "jane1", "122");
insert into users (user_type, dni, firstname, lastname, username, u_password) values("Employee", "77666555", "Luna", "Rojas", "moon", "red");
insert into tariffs(tariff_type, tariff) values("RESIDENTIAL", 112.2);
insert into tariffs(tariff_type, tariff) values("SOCIAL", 51.8);
insert into addresses(address, id_tariff, id_customer) values("Street 111", 1, 1);
insert into addresses(address, id_tariff, id_customer) values("Avenue 222", 2, 2);
insert into meters(serial_number, m_password, id_model, id_brand, id_address) values("11122asd", "P4ss", 1, 1, 1);
insert into meters(serial_number, m_password, id_model, id_brand, id_address) values("dd2222", "P12", 1, 1, 2);

insert into measurements(id_meter, measurement, m_datetime) values(1, 12.2, "2020-03-10 11:22:11");
insert into measurements(id_meter, measurement, m_datetime) values(1, 33.4, "2020-03-10 11:22:33");
insert into measurements(id_meter, measurement, m_datetime) values(1, 62.5, "2021-03-10 11:22:11");
insert into measurements(id_meter, measurement, m_datetime) values(2, 6.2, "2022-03-10 11:22:11");
insert into measurements(id_meter, measurement, m_datetime) values(2, 12.2, "2018-03-10 11:22:11");