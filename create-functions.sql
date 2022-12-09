CREATE FUNCTION isVehicleAvailable(input_vehicle_id integer)
RETURNS BOOLEAN
AS $$ BEGIN
    IF NOT EXISTS (SELECT * FROM booking b WHERE b.vehicle_id = input_vehicle_id)
        THEN RETURN true;
        ELSE RETURN false;
    END IF;
END;
$$ language plpgsql;

CREATE FUNCTION customerLocMakeModelSearch(input_make varchar, input_model varchar, input_location integer)
RETURNS TABLE (
  vehicle_id integer,
  location_id integer,
  reg varchar,
  make varchar,
  model varchar,
  daily_fee numeric
)
AS $$ BEGIN Return Query
SELECT
    v.vehicle_id,
    v.location_id,
    v.reg,
    v.make,
    v.model,
    v.daily_fee
FROM
    vehicle v
WHERE
    isVehicleAvailable(v.vehicle_id)
    AND v.location_id = input_location
    AND (v.make = input_make OR input_make = '')
    AND (v.model = input_model OR input_model = '');
END;
$$ language plpgsql;

CREATE FUNCTION getLocationsWithAddresses()
RETURNS TABLE (
    location_id integer,
    num varchar,
    street varchar,
    city_code varchar,
    country_code varchar,
    postcode varchar
)
AS $$ BEGIN Return Query
SELECT location.location_id, address.num, address.street, address.city_code, address.country_code, address.postcode
FROM location
INNER JOIN address ON location.address_id = address.address_id;
END;
$$ language plpgsql;

CREATE FUNCTION locationCityCountryCodeSearch(input_city_code varchar, input_country_code varchar)
RETURNS TABLE (
    location_id integer,
    num varchar,
    street varchar,
    city_code varchar,
    country_code varchar,
    postcode varchar
)
AS $$ BEGIN Return Query
SELECT
    *
FROM
    getLocationsWithAddresses() l
WHERE
    (l.city_code = input_city_code OR input_city_code = '')
    AND (l.country_code = input_country_code OR input_country_code = '');
END;
$$ language plpgsql;

CREATE FUNCTION addBooking(input_customer_id integer, input_vehicle_id integer, input_dropoff integer, input_date_from date, input_date_to date)
RETURNS VOID
AS $$ BEGIN
INSERT INTO booking (
customer_id,
vehicle_id,
pickup_loc,
dropoff_loc,
datefrom,
dateto)
VALUES (
input_customer_id,
input_vehicle_id,
(SELECT location_id FROM vehicle WHERE vehicle_id = input_vehicle_id),
input_dropoff,
input_date_from,
input_date_to);
END;
$$ language plpgsql;