CREATE TABLE IF NOT EXISTS employee (
    employee_id serial PRIMARY KEY,
    username VARCHAR(25) UNIQUE,
    password VARCHAR(25)
);

CREATE TABLE IF NOT EXISTS city (
    city_code VARCHAR(2) PRIMARY KEY,
    city VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS country (
    country_code VARCHAR(2) PRIMARY KEY,
    country VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS address (
    address_id SERIAL PRIMARY KEY,
    num VARCHAR(15),
    street VARCHAR(50),
    city_code VARCHAR(2) REFERENCES city(city_code),
    country_code VARCHAR(2) REFERENCES country(country_code),
    postcode VARCHAR(9)
);

CREATE TABLE IF NOT EXISTS customer (
    customer_id SERIAL PRIMARY KEY,
    firstname VARCHAR(50),
    lastname VARCHAR(50),
    username VARCHAR(35) UNIQUE,
    password VARCHAR(25)
    --address_id SERIAL REFERENCES address(address_id)
    --Note: Ideally the entry above would be used. However it is omitted here for the sake of satisfying a MVP. Revisit.
);

CREATE TABLE IF NOT EXISTS location (
    location_id SERIAL PRIMARY KEY,
    address_id INTEGER REFERENCES address(address_id)
);

CREATE TABLE IF NOT EXISTS vehicle (
    vehicle_id SERIAL PRIMARY KEY,
    reg VARCHAR(8),
    make VARCHAR(30),
    model VARCHAR(30),
    location_id INTEGER REFERENCES location(location_id),
    daily_fee numeric
);

CREATE TABLE IF NOT EXISTS booking (
    booking_id SERIAL PRIMARY KEY,
    customer_id INTEGER REFERENCES customer(customer_id),
    vehicle_id INTEGER REFERENCES vehicle(vehicle_id),
    pickup_loc INTEGER REFERENCES location(location_id),
    dropoff_loc INTEGER REFERENCES location(location_id),
    datefrom date,
    dateto date
);