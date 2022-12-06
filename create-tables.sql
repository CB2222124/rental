-- TODO
--Customer
SELECT * FROM customer;

CREATE TABLE customer(

    customer_id SERIAL PRIMARY KEY NOT NULL ,
    date_of_birth DATE,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    username varchar(50) NOT NULL ,
    password varchar(50) NOT NULL ,
    address varchar(255)
);

SELECT * FROM employee;

CREATE TABLE employee(
    employee_id SERIAL PRIMARY KEY  NOT NULL ,
    --role_id INTEGER,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    username varchar(50) NOT NULL ,
    password varchar(50) NOT NULL ,
    location_id SERIAL REFERENCES location (location_id)
);

SELECT * FROM location;

CREATE TABLE location (
    location_id SERIAL PRIMARY KEY NOT NULL ,
    street VARCHAR(255),
    postcode VARCHAR(50),
    telephone VARCHAR(50),
    city_code CHAR(2),
    country_code CHAR(2)
    --address_description VARCHAR (255)
);

SELECT * FROM vehicle;

CREATE TABLE vehicle(
    vehicle_id SERIAL PRIMARY KEY NOT NULL ,
    reg_no INTEGER,
    make VARCHAR(255),
    model VARCHAR(255),
    colour VARCHAR(255),
    daily_rate VARCHAR(255)
    --MORE FIELDS NOT INCLUDED
);
SELECT * FROM location_vehicle;

CREATE TABLE   location_vehicle(
    location_id SERIAL REFERENCES location(location_id),
    vehicle_id SERIAL REFERENCES vehicle(vehicle_id),
    availability BOOLEAN,
    PRIMARY KEY (location_id, vehicle_id)
);
SELECT * FROM orders;

CREATE TABLE orders(
    order_id SERIAL PRIMARY KEY NOT NULL ,
    vehicle_id SERIAL REFERENCES vehicle(vehicle_id) ,
    customer_id SERIAL REFERENCES customer(customer_id),
    location_id SERIAL REFERENCES location(location_id),
    drop_off_location_id INTEGER,
    number_of_days INTEGER,
    booking_total INTEGER
);