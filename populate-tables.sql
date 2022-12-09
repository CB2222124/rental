-- https://www.mockaroo.com/
insert into employee (username, password) values ('admin', 'password');

insert into city (city_code, city) values ('SW', 'Swansea');
insert into city (city_code, city) values ('BS', 'Bristol');
insert into city (city_code, city) values ('GL', 'Glasgow');


insert into country (country_code, country) values ('WA', 'Wales');
insert into country (country_code, country) values ('EN', 'England');
insert into country (country_code, country) values ('SC', 'Scotland');


insert into address (num, street, city_code, country_code, postcode) values (5, 'Morriston Way', 'SW', 'WA', 'SW6 6GG');
insert into address (num, street, city_code, country_code, postcode) values (17, 'Harbour Walk', 'BS', 'EN', 'BS1 5NG');
insert into address (num, street, city_code, country_code, postcode) values (14, 'Bruce Avenue', 'GL', 'SC', 'GL1 2LS');
insert into address (num, street, city_code, country_code, postcode) values (22, 'Hall Road', 'BS', 'EN', 'BS3 1NG');
insert into address (num, street, city_code, country_code, postcode) values (123, 'Arthur Close', 'GL', 'SC', 'GL3 6HH');


--insert into customer (firstname, lastname, username, password, address_id) values ('Jamila', 'Jones', 'user101', 'letmein', 4);
--insert into customer (firstname, lastname, username, password, address_id) values ('Barney', 'Rubble', 'BRubble101', 'sticksnstones', 5);
insert into customer (firstname, lastname, username, password) values ('Jamila', 'Jones', 'user101', 'letmein');
insert into customer (firstname, lastname, username, password) values ('Barney', 'Rubble', 'BRubble101', 'sticksnstones');


insert into location (address_id) values (1);
insert into location (address_id) values (2);
insert into location (address_id) values (3);

insert into vehicle (reg, make, model, location_id, daily_fee) values ('GH26 VVC', 'Honda', 'Ridgeline', 1, 124.11);
insert into vehicle (reg, make, model, location_id, daily_fee) values ('RR02 CSE', 'GMC', 'Acadia', 1, 99.53);
insert into vehicle (reg, make, model, location_id, daily_fee) values ('HX51 OLP', 'Ford', 'Mustang', 1, 130.58);
insert into vehicle (reg, make, model, location_id, daily_fee) values ('VC88 HDV', 'Bentley', 'Continental GT', 2, 139.72);
insert into vehicle (reg, make, model, location_id, daily_fee) values ('HR91 JAO', 'Chevrolet', 'Silverado', 3, 186.17);


insert into booking (customer_id, vehicle_id, pickup_loc, dropoff_loc, datefrom, dateto, with_customer) values (2, 3, 3, 1, '2022-12-21', '2022-12-28', false);