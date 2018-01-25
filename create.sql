
CREATE TABLE benefit (
    benefit_id MEDIUMINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(500) NOT NULL,
    sleva	INTEGER NOT NULL
);

CREATE TABLE cenova_zona (
    cenova_zona_id   MEDIUMINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    price            INTEGER NOT NULL
);

CREATE TABLE ma (
    cenova_zona_cenova_zona_id   INTEGER NOT NULL,
    udalost_udalost_id           INTEGER NOT NULL
);

ALTER TABLE ma ADD CONSTRAINT ma_pk PRIMARY KEY ( cenova_zona_cenova_zona_id,
udalost_udalost_id );

CREATE TABLE nabizi (
    udalost_udalost_id   INTEGER NOT NULL,
    benefit_benefit_id   INTEGER NOT NULL
);

ALTER TABLE nabizi ADD CONSTRAINT nabizi_pk PRIMARY KEY ( udalost_udalost_id,
benefit_benefit_id );

CREATE TABLE obsahuje (
    omezeni_omezeni_id   INTEGER NOT NULL,
    udalost_udalost_id   INTEGER NOT NULL
);

ALTER TABLE obsahuje ADD CONSTRAINT obsahuje_pk PRIMARY KEY ( omezeni_omezeni_id,
udalost_udalost_id );

CREATE TABLE omezeni (
    omezeni_id    MEDIUMINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    type          INTEGER NOT NULL,
    name          VARCHAR(50) NOT NULL,
    description   VARCHAR(500)
);

CREATE TABLE osoba (
    osoba_id   MEDIUMINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(20) NOT NULL,
    surname    VARCHAR(20) NOT NULL,
    username   VARCHAR(30) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    rights     INTEGER NOT NULL,
    money      INTEGER
);

CREATE TABLE rezervace (
    udalost_udalost_id   INTEGER NOT NULL,
    osoba_osoba_id       INTEGER NOT NULL
);

ALTER TABLE rezervace ADD CONSTRAINT rezervace_pk PRIMARY KEY ( udalost_udalost_id,
osoba_osoba_id );

CREATE TABLE udalost (
    udalost_id             MEDIUMINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name                   VARCHAR(100) NOT NULL,
    datum                  DATE NOT NULL,
    available_seats        INTEGER NOT NULL,
    ticket_price           INTEGER NOT NULL,
    zarizeni_id		   INTEGER NOT NULL
);

CREATE TABLE zarizeni (
    zarizeni_id   MEDIUMINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(70) NOT NULL,
    location      VARCHAR(70) NOT NULL
);

CREATE TABLE ziskala (
    osoba_osoba_id       INTEGER NOT NULL,
    benefit_benefit_id   INTEGER NOT NULL
);

ALTER TABLE ziskala ADD CONSTRAINT ziskala_pk PRIMARY KEY ( osoba_osoba_id,
benefit_benefit_id );




-- Oracle SQL Developer Data Modeler Summary Report: 
-- 
-- CREATE TABLE                            11
-- CREATE INDEX                             0
-- ALTER TABLE                             22
-- CREATE VIEW                              0
-- ALTER VIEW                               0
-- CREATE PACKAGE                           0
-- CREATE PACKAGE BODY                      0
-- CREATE PROCEDURE                         0
-- CREATE FUNCTION                          0
-- CREATE TRIGGER                           0
-- ALTER TRIGGER                            0
-- CREATE COLLECTION TYPE                   0
-- CREATE STRUCTURED TYPE                   0
-- CREATE STRUCTURED TYPE BODY              0
-- CREATE CLUSTER                           0
-- CREATE CONTEXT                           0
-- CREATE DATABASE                          0
-- CREATE DIMENSION                         0
-- CREATE DIRECTORY                         0
-- CREATE DISK GROUP                        0
-- CREATE ROLE                              0
-- CREATE ROLLBACK SEGMENT                  0
-- CREATE SEQUENCE                          0
-- CREATE MATERIALIZED VIEW                 0
-- CREATE SYNONYM                           0
-- CREATE TABLESPACE                        0
-- CREATE USER                              0
-- 
-- DROP TABLESPACE                          0
-- DROP DATABASE                            0
-- 
-- ORDS DROP SCHEMA                         0
-- ORDS ENABLE SCHEMA                       0
-- ORDS ENABLE OBJECT                       0
-- 
-- ERRORS                                   0
-- WARNINGS                                 0
