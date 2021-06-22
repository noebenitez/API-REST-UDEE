# ------------------------ Indexes -----------------------------

CREATE INDEX measurement_datetime_index ON measurements(m_datetime) USING BTREE;
CREATE INDEX measurement_dste_index ON measurements (m_datetime) USING BTREE;

CREATE INDEX users_login_index ON users (username, u_password) USING HASH;
CREATE UNIQUE INDEX users_dni_index ON users(dni) USING HASH;

CREATE INDEX meters_serial_pass_index ON meters (serial_number, m_password) USING HASH;

CREATE UNIQUE INDEX tariff_type_index ON tariffs (tariff_type) USING HASH;

CREATE INDEX invoice_serial_index ON invoices(serial_number) USING HASH;
CREATE INDEX invoice_initial_datetime_index ON invoices(initial_datetime) USING BTREE;
CREATE INDEX invoice_final_datetime_index ON invoices(final_datetime) USING BTREE;
