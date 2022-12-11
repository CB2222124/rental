-- https://www.mockaroo.com/
CREATE OR REPLACE FUNCTION populateTables()
RETURNS VOID
AS
$$
DECLARE
BEGIN
    insert into employee (username, password) values ('admin', 'password');

    insert into city (city_code, city) values ('SW', 'Swansea');
    insert into city (city_code, city) values ('BS', 'Bristol');
    insert into city (city_code, city) values ('GL', 'Glasgow');
    insert into city (city_code, city) values ('LI', 'Liverpool');
    insert into city (city_code, city) values ('ED', 'Edinburgh');
    insert into city (city_code, city) values ('BA', 'Bangor');


    insert into country (country_code, country) values ('WA', 'Wales');
    insert into country (country_code, country) values ('EN', 'England');
    insert into country (country_code, country) values ('SC', 'Scotland');

    insert into address (num, street, city_code, country_code, postcode) values (5, 'Morriston Way', 'SW', 'WA', 'SA6 6GG');
    insert into address (num, street, city_code, country_code, postcode) values (17, 'Harbour Walk', 'BS', 'EN', 'BS1 5NG');
    insert into address (num, street, city_code, country_code, postcode) values (14, 'Bruce Avenue', 'GL', 'SC', 'GL1 2LS');
    insert into address (num, street, city_code, country_code, postcode) values (22, 'Hall Road', 'BS', 'EN', 'BS3 1NG');
    insert into address (num, street, city_code, country_code, postcode) values (123, 'Arthur Close', 'GL', 'SC', 'GL3 6HH');
    insert into address (num, street, city_code, country_code, postcode) values (44, 'Melrose Avenue', 'LI', 'EN', 'LI2 3EB');
    insert into address (num, street, city_code, country_code, postcode) values (8, 'Birchwood Close', 'SW', 'WA', 'SA3 3OD');
    insert into address (num, street, city_code, country_code, postcode) values (58, 'Hooker Lane', 'ED', 'SC', 'ED1 3ED');
    insert into address (num, street, city_code, country_code, postcode) values (17, 'Hilltop Lane', 'BA', 'WA', 'BA1 2HT');

    insert into customer (firstname, lastname, username, password) values ('Jamila', 'Jones', 'user101', 'letmein');
    insert into customer (firstname, lastname, username, password) values ('Barney', 'Rubble', 'BRubble101', 'sticksnstones');
    insert into customer (firstname, lastname, username, password) values ('Theresita', 'Beneze', 'tbeneze0', 'Qw8AtWhwDm');
    insert into customer (firstname, lastname, username, password) values ('Dagmar', 'Lait', 'dlait1', '2UGqXLy7fXN');
    insert into customer (firstname, lastname, username, password) values ('Merrill', 'Bush', 'mbush2', 'hsIw2WTMEXBy');
    insert into customer (firstname, lastname, username, password) values ('Howie', 'Ineson', 'hineson3', 'ZdqQzRJZp');
    insert into customer (firstname, lastname, username, password) values ('Sande', 'Readman', 'sreadman4', 'lfAtCfoqlP');

    insert into location (address_id) values (1);
    insert into location (address_id) values (2);
    insert into location (address_id) values (3);

    insert into vehicle (reg, make, model, location_id, daily_fee) values ('GH26 VVC', 'Honda', 'Ridgeline', 1, 124.11);
    insert into vehicle (reg, make, model, location_id, daily_fee) values ('RR02 CSE', 'GMC', 'Acadia', 1, 99.53);
    insert into vehicle (reg, make, model, location_id, daily_fee) values ('HX51 OLP', 'Ford', 'Mustang', 3, 130.58);
    insert into vehicle (reg, make, model, location_id, daily_fee) values ('VC88 HDV', 'Bentley', 'Continental GT', 2, 139.72);
    insert into vehicle (reg, make, model, location_id, daily_fee) values ('HR91 JAO', 'Chevrolet', 'Silverado', 3, 186.17);
    insert into vehicle (reg, make, model, location_id, daily_fee) values ('OI80 AXE', 'Jeep', 'Grand Cherokee', 2, 125.9);
    insert into vehicle (reg, make, model, location_id, daily_fee) values ('ML17 LVW', 'BMW', '7 Series', 2, 154.94);
    insert into vehicle (reg, make, model, location_id, daily_fee) values ('RL31 IIL', 'Buick', 'Century', 3, 171.95);
    insert into vehicle (reg, make, model, location_id, daily_fee) values ('FC63 FCC', 'Oldsmobile', 'Bravada', 3, 168.48);
    insert into vehicle (reg, make, model, location_id, daily_fee) values ('JI93 PRL', 'Saturn', 'VUE', 3, 105.81);
    insert into vehicle (reg, make, model, location_id, daily_fee) values ('AY29 VZN', 'Porsche', 'Boxster', 2, 146.79);
    insert into vehicle (reg, make, model, location_id, daily_fee) values ('TT56 QTY', 'Nissan', '350Z', 1, 154.65);
    insert into vehicle (reg, make, model, location_id, daily_fee) values ('MO89 GZP', 'Buick', 'LeSabre', 1, 198.15);
    insert into vehicle (reg, make, model, location_id, daily_fee) values ('NT19 IFL', 'Ford', 'Contour', 2, 197.19);
    insert into vehicle (reg, make, model, location_id, daily_fee) values ('NS31 GDT', 'Mercury', 'Cougar', 1, 187.96);

    insert into booking (customer_id, vehicle_id, pickup_loc, dropoff_loc, datefrom, dateto, with_customer) values (2, 3, 3, 1, '2022-12-21', '2022-12-28', false);
    insert into booking (customer_id, vehicle_id, pickup_loc, dropoff_loc, datefrom, dateto, with_customer) values (4, 8, 3, 2, '2022-12-05', '2022-12-28', true);
    insert into booking (customer_id, vehicle_id, pickup_loc, dropoff_loc, datefrom, dateto, with_customer) values (5, 9, 3, 1, '2022-12-15', '2022-12-16', false);
    insert into booking (customer_id, vehicle_id, pickup_loc, dropoff_loc, datefrom, dateto, with_customer) values (6, 11, 2, 2, '2022-12-24', '2022-12-31', false);
END;
$$ language plpgsql;

--We wrap the population of tables as a function for now, so that the database can be restored during testing.
SELECT populateTables();