CREATE OR REPLACE FUNCTION createTables()
RETURNS VOID
AS
$$
DECLARE
BEGIN
    DROP TABLE IF EXISTS booking;
    DROP TABLE IF EXISTS vehicle;
    DROP TABLE IF EXISTS location;
    DROP TABLE IF EXISTS customer;
    DROP TABLE IF EXISTS address;
    DROP TABLE IF EXISTS country;
    DROP TABLE IF EXISTS city;
    DROP TABLE IF EXISTS employee;

    CREATE TABLE employee (
        employee_id serial PRIMARY KEY,
        username VARCHAR(25) UNIQUE,
        password VARCHAR(25)
    );

    CREATE TABLE city (
        city_code VARCHAR(2) PRIMARY KEY,
        city VARCHAR(50)
    );

    CREATE TABLE country (
        country_code VARCHAR(2) PRIMARY KEY,
        country VARCHAR(50)
    );

    CREATE TABLE address (
        address_id SERIAL PRIMARY KEY,
        num VARCHAR(15),
        street VARCHAR(50),
        city_code VARCHAR(2) REFERENCES city(city_code),
        country_code VARCHAR(2) REFERENCES country(country_code),
        postcode VARCHAR(9)
    );

    CREATE TABLE customer (
        customer_id SERIAL PRIMARY KEY,
        firstname VARCHAR(50),
        lastname VARCHAR(50),
        username VARCHAR(35) UNIQUE,
        password VARCHAR(25)
        --address_id SERIAL REFERENCES address(address_id)
        --Note: Ideally the entry above would be used. However it is omitted here for the sake of satisfying a MVP. Revisit.
    );

    CREATE TABLE location (
        location_id SERIAL PRIMARY KEY,
        address_id INTEGER REFERENCES address(address_id)
    );

    CREATE TABLE vehicle (
        vehicle_id SERIAL PRIMARY KEY,
        reg VARCHAR(8),
        make VARCHAR(30),
        model VARCHAR(30),
        location_id INTEGER REFERENCES location(location_id),
        daily_fee numeric
    );

    CREATE TABLE booking (
        booking_id SERIAL PRIMARY KEY,
        customer_id INTEGER REFERENCES customer(customer_id),
        vehicle_id INTEGER REFERENCES vehicle(vehicle_id),
        pickup_loc INTEGER REFERENCES location(location_id),
        dropoff_loc INTEGER REFERENCES location(location_id),
        datefrom date,
        dateto date,
        with_customer BOOLEAN
    );
END;
$$ language plpgsql;

--We wrap the creation of tables as a function for now, so that the database can be restored during testing.
SELECT createTables();
