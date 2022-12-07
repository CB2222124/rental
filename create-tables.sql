CREATE TABLE IF NOT EXISTS employee(
    employee_id serial PRIMARY KEY,
    username varchar (25),
    password varchar (25)
    );


CREATE TABLE IF NOT EXISTS city(
    city_code varchar(2) PRIMARY KEY,
    city varchar(50)
    );


CREATE TABLE IF NOT EXISTS country(
    country_code varchar(2) PRIMARY KEY,
    country varchar(50)
    );


CREATE TABLE IF NOT EXISTS address(
    address_id serial PRIMARY KEY,
    num integer,
    street varchar(50),
    city_code varchar(2) REFERENCES city(city_code),
    country_code varchar (2) REFERENCES country (country_code),
    postcode varchar (9)
    );


CREATE TABLE IF NOT EXISTS customer(
    customer_id serial PRIMARY KEY,
    firstname character varying(50),
    lastname character varying(50),
    username character varying(35),
    password character varying(25),
    address_id serial REFERENCES address (address_id)
    );


CREATE TABLE IF NOT EXISTS location(
    location_id serial PRIMARY KEY,
    address_id serial REFERENCES address (address_id)
    );

CREATE TABLE IF NOT EXISTS booking(
    booking_id serial PRIMARY KEY,
    customer_id serial REFERENCES customer (customer_id),
    pickup_loc integer REFERENCES location (location_id),
    dropoff_loc integer REFERENCES location (location_id),
    reg_no character varying(8),
    datefrom date,
    dateto date
    );


CREATE TABLE IF NOT EXISTS vehicle(
    reg_no varchar PRIMARY KEY,
    make varchar (30),
    model varchar (30),
    available boolean,
    location_id serial REFERENCES location (location_id),
    daily_fee numeric
    );