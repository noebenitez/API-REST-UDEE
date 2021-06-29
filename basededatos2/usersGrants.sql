# ------------------ Users privileges ----------------------

# >> User: backoffice
CREATE USER 'backoffice' IDENTIFIED BY 'b4ck0ff1c3';
GRANT SELECT (id, user_type, dni, firstname, lastname, username),
UPDATE (dni, firstname, lastname, username),
INSERT, DELETE ON udeebd.users TO 'backoffice';
GRANT SELECT, INSERT, UPDATE, DELETE ON udeebd.meters TO 'backoffice';
GRANT SELECT, INSERT, UPDATE, DELETE ON udeebd.tariffs TO 'backoffice';
GRANT SELECT, INSERT, UPDATE, DELETE ON udeebd.brands TO 'backoffice';
GRANT SELECT, INSERT, UPDATE, DELETE ON udeebd.models TO 'backoffice';
GRANT SELECT, INSERT, UPDATE, DELETE ON udeebd.addresses TO 'backoffice';
#REVOKE ALL PRIVILEGES ON *.* FROM 'backoffice';
#DROP USER 'backoffice';

# >> User: client
CREATE USER 'client' IDENTIFIED BY 'cl13nt';
GRANT SELECT ON udeebd.measurements TO 'client';
GRANT SELECT ON udeebd.invoices TO 'client';
#REVOKE ALL PRIVILEGES ON *.* FROM 'client';
#DROP USER 'client';


# >> User: meter
CREATE USER 'meter' IDENTIFIED BY 'm3t3r';
GRANT INSERT ON udeebd.measurements TO 'meter';
#REVOKE ALL PRIVILEGES ON *.* FROM 'meter';
#DROP USER 'meter';

# >> User: Invoicing
CREATE USER 'invoicing' IDENTIFIED BY '1nv01c1ng';
#REVOKE ALL PRIVILEGES ON *.* FROM 'invoicing';
#DROP USER 'invoicing';
