DROP TABLE DeliveryToPharmacyDescription;
DROP TABLE DeliveryToPharmacy;
DROP TABLE Car;
DROP TABLE MedicineInPharmacies;
DROP TABLE Pharmacy;
DROP TABLE DeliveryToWarehouseDescription;
DROP TABLE DeliveryToWarehouse;
DROP TABLE Warehouse;
DROP TABLE Distributor;
DROP TABLE Medicine;
DROP TABLE Form;
DROP TABLE MedicineDescription;
DROP TABLE MedicineInternationalDescription;
DROP TABLE Ingredient;
DROP TABLE Certificate;
DROP TABLE Laboratory;


-- Описание лаборатории, которая делала исследование лекарства.
CREATE TABLE Laboratory
(
    id          SERIAL PRIMARY KEY,
    name        TEXT NOT NULL,
    leader_name TEXT NOT NULL
);

-- Сертификат лекарства.
CREATE TABLE Certificate
(
    id         INT PRIMARY KEY,
    validity   DATE                      NOT NULL,
    laboratory INT REFERENCES Laboratory NOT NULL
);

-- Действующее вещество.
CREATE TABLE Ingredient
(
    id      SERIAL PRIMARY KEY,
    name    TEXT UNIQUE NOT NULL,
    formula TEXT        NOT NULL
);

-- Международное описание лекарства.
CREATE TABLE MedicineInternationalDescription
(
    id                   SERIAL PRIMARY KEY,
    international_name   TEXT UNIQUE               NOT NULL,
    active_ingredient_id INT REFERENCES Ingredient NOT NULL
);

-- Описание лекарства, независящее от производителя.
CREATE TABLE MedicineDescription
(
    id                        SERIAL PRIMARY KEY,
    trade_name                TEXT UNIQUE                                     NOT NULL,
    medicine_international_id INT REFERENCES MedicineInternationalDescription NOT NULL
);

-- Форма лекарства.
CREATE TABLE Form
(
    id   SERIAL PRIMARY KEY,
    name TEXT UNIQUE NOT NULL
);

-- Производимое лекарство.
CREATE TABLE Medicine
(
    id                      SERIAL PRIMARY KEY,
    medicine_description_id INT REFERENCES MedicineDescription NOT NULL,
    manufacturer            TEXT                               NOT NULL,
    form_id                 INT REFERENCES Form                NOT NULL,
    certificate_id          INT REFERENCES Certificate         NOT NULL,
    UNIQUE (medicine_description_id, manufacturer, form_id)
);

-- Поставщик лекарств.
CREATE TABLE Distributor
(
    id           SERIAL PRIMARY KEY,
    name         TEXT        NOT NULL,
    surname      TEXT        NOT NULL,
    address      TEXT        NOT NULL,
    bank_account TEXT UNIQUE NOT NULL,
    phone        TEXT UNIQUE NOT NULL
);

-- Склад.
CREATE TABLE Warehouse
(
    id      INT PRIMARY KEY,
    address TEXT NOT NULL
);

-- Описание доставки на склад.
CREATE TABLE DeliveryToWarehouse
(
    id               SERIAL PRIMARY KEY,
    distributor_id   INT REFERENCES Distributor NOT NULL,
    warehouse_id     INT REFERENCES Warehouse   NOT NULL,
    date             DATE                       NOT NULL,
    completed_time   TIME,
    completed_worker TEXT
);

-- Описание состава доставки на склад.
CREATE TABLE DeliveryToWarehouseDescription
(
    delivery_id      INT REFERENCES DeliveryToWarehouse NOT NULL,
    medicine_id      INT REFERENCES Medicine            NOT NULL,
    transport_amount INT                                NOT NULL CHECK (transport_amount > 0),
    transport_weight DECIMAL(10, 4)                     NOT NULL CHECK (transport_weight >= 0),
    sell_amount      INT                                NOT NULL CHECK (sell_amount > 0),
    sell_price       DECIMAL(10, 3)                     NOT NULL CHECK (sell_price >= 0),
    PRIMARY KEY (delivery_id, medicine_id)
);

-- Аптека.
CREATE TABLE Pharmacy
(
    id      INT PRIMARY KEY,
    address TEXT NOT NULL
);

-- Запас лекарства в аптеке.
CREATE TABLE MedicineInPharmacies
(
    pharmacy_id INT REFERENCES Pharmacy NOT NULL,
    medicine_id INT REFERENCES Medicine NOT NULL,
    price       DECIMAL(10, 3)          NOT NULL CHECK (price >= 0),
    amount      INT                     NOT NULL CHECK (amount >= 0),
    PRIMARY KEY (pharmacy_id, medicine_id)
);

-- Машина, осуществляющая доставку в аптеки.
CREATE TABLE Car
(
    number       TEXT PRIMARY KEY,
    service_date DATE NOT NULL
);

-- Доставка со склада в аптеку.
CREATE TABLE DeliveryToPharmacy
(
    id          SERIAL PRIMARY KEY,
    car_number  TEXT REFERENCES Car      NOT NULL,
    date        DATE                     NOT NULL,
    storage_id  INT REFERENCES Warehouse NOT NULL,
    pharmacy_id INT REFERENCES Pharmacy  NOT NULL,
    UNIQUE (car_number, date, pharmacy_id)
);

-- Описание содержимого доставки в аптеку.
CREATE TABLE DeliveryToPharmacyDescription
(
    delivery_id     INT REFERENCES DeliveryToPharmacy NOT NULL,
    medicine_id     INT REFERENCES Medicine           NOT NULL,
    packages_amount INT                               NOT NULL CHECK (packages_amount >= 0),
    PRIMARY KEY (delivery_id, medicine_id)
);

WITH Forms AS (
    SELECT unnest(ARRAY ['порошок', 'мазь', 'сироп']) AS name)
INSERT
INTO Form(name)
SELECT name
FROM Forms;

WITH Pharmacies AS (
    SELECT unnest(ARRAY ['Невский, 48', 'Садовая, 13', 'Большой пр., 22']) AS address,
           unnest(ARRAY [1, 2, 3])                                         AS number
)
INSERT
INTO Pharmacy(id, address)
SELECT number, address
FROM Pharmacies;

WITH Laboratories AS (
    SELECT unnest(ARRAY ['First lab', 'Second lab'])       AS name,
           unnest(ARRAY ['Петров Сергей', 'Сергеев Петр']) AS leader_name
)
INSERT
INTO Laboratory(name, leader_name)
SELECT name, leader_name
FROM Laboratories;

INSERT
INTO Certificate(id, validity, laboratory)
SELECT n, CURRENT_DATE, (1 + random() * (SELECT COUNT(*) - 1 FROM Laboratory))::INT
FROM generate_series(1, 10) AS n;

WITH Ingredients AS (
    SELECT unnest(ARRAY [
        'вода',
        'перекись водорода',
        'аскорбиновая кислота',
        'ацетилсалициловая кислота',
        'азотная кислота',
        'кремнивая кислота'
        ])        AS name,
           unnest(ARRAY [
               'H2O',
               'H2O2',
               'C6H8O6',
               'C9H8O4',
               'HNO3',
               'H2SiO3'
               ]) AS formula
)
INSERT
INTO Ingredient(name, formula)
SELECT name, formula
FROM Ingredients;

INSERT
INTO MedicineInternationalDescription(international_name, active_ingredient_id)
SELECT concat('international_medicine_', n::text),
       (1 + random() * (SELECT COUNT(*) - 1 FROM Ingredient))::INT
FROM generate_series(1, 100) AS n;

INSERT
INTO MedicineDescription(trade_name, medicine_international_id)
SELECT concat('medicine_', n::text),
       (1 + random() * (SELECT COUNT(*) - 1 FROM MedicineInternationalDescription))::INT
FROM generate_series(1, 1000) AS n;

INSERT
INTO Medicine(medicine_description_id, manufacturer, form_id, certificate_id)
    (SELECT MedicineDescription.id,
            concat('Производитель №', ((1 + random() * 10)::INT)::text),
            Form.id,
            (1 + random() * (SELECT COUNT(*) - 1 FROM Certificate))::INT
     FROM MedicineDescription,
          Form);

INSERT
INTO MedicineInPharmacies(pharmacy_id, medicine_id, price, amount)
    (SELECT Pharmacy.id,
            Medicine.id,
            (random() * 1000)::DECIMAL(10, 3),
            (random() * 100)::INT
     FROM Pharmacy,
          Medicine);

