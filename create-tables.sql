CREATE TABLE customer (
    customer_id SERIAL PRIMARY KEY NOT NULL,
    date_of_birth DATE,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    username VARCHAR(50) UNIQUE,
    password VARCHAR(50) NOT NULL,
    address VARCHAR(255)
);

CREATE TABLE location (
    location_id SERIAL PRIMARY KEY NOT NULL,
    street VARCHAR(255),
    city VARCHAR(50),
    country VARCHAR(50),
    postcode VARCHAR(50),
    telephone VARCHAR(50)
);

CREATE TABLE employee (
    employee_id SERIAL PRIMARY KEY  NOT NULL,
    username VARCHAR(50) UNIQUE,
    password VARCHAR(50) NOT NULL,
    location_id SERIAL REFERENCES location (location_id)
);

CREATE TABLE vehicle (
    vehicle_id SERIAL PRIMARY KEY NOT NULL,
    location_id SERIAL REFERENCES location(location_id),
    availability BOOLEAN,
    reg_no VARCHAR(8),
    make VARCHAR(255),
    model VARCHAR(255),
    colour VARCHAR(255),
    daily_rate MONEY
    --MORE FIELDS NOT INCLUDED
);

CREATE TABLE booking (
    booking_id SERIAL PRIMARY KEY NOT NULL,
    vehicle_id SERIAL REFERENCES vehicle(vehicle_id),
    customer_id SERIAL REFERENCES customer(customer_id),
    location_id SERIAL REFERENCES location(location_id),
    drop_off_location_id INTEGER,
    number_of_days INTEGER,
    booking_total INTEGER
);