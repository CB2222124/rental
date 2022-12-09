--Checks if a vehicle ID is present in the booking table. Callan.
CREATE FUNCTION isVehicleAvailable(input_vehicle_id integer)
RETURNS BOOLEAN
AS $$ BEGIN
    IF NOT EXISTS (SELECT * FROM booking b WHERE b.vehicle_id = input_vehicle_id)
        THEN RETURN true;
        ELSE RETURN false;
    END IF;
END;
$$ language plpgsql;

--Filters vehicles at a location by make and model. Liam.
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

--Function allows employees to update a vehicle reg only if it exists. Rania.
CREATE FUNCTION updateReg(newReg VARCHAR, vehicle_idToLook INTEGER)
RETURNS
		VOID
LANGUAGE PLPGSQL
AS
$$
DECLARE

BEGIN
	IF EXISTS(
	SELECT vehicle_id FROM vehicle WHERE vehicle_id = vehicle_idToLook)
	THEN
UPDATE vehicle
SET reg = newReg WHERE vehicle.vehicle_id = vehicle_idToLook;

ELSE
	RAISE 'Oops looks like vehicle % does not exist', vehicle_idToLook;
END IF;

END;
$$;

--Function allows employees to update a vehicles daily price only if it exists. Rania.
CREATE FUNCTION updateDailyPrice(newDailyFee DOUBLE PRECISION, vehicle_idToLook INTEGER)
    RETURNS
        VOID
    LANGUAGE PLPGSQL
AS
$$
DECLARE

BEGIN
	IF EXISTS(
	SELECT vehicle_id FROM vehicle WHERE vehicle_id = vehicle_idToLook)
	THEN
UPDATE vehicle
SET daily_fee = newDailyFee WHERE vehicle.vehicle_id = vehicle_idToLook;

ELSE
	RAISE 'Oops looks like vehicle % does not exist', vehicle_idToLook;
END IF;

END;
$$;

--Fetches a table that shows all locations and their associated addresses. Callan.
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

--Filters locations by city and country code. Callan.
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
    getLocationsWithAddresses() location
WHERE
    (location.city_code = input_city_code OR input_city_code = '')
    AND (location.country_code = input_country_code OR input_country_code = '');
END;
$$ language plpgsql;

--Adds a new booking. Callan.
CREATE FUNCTION addBooking(input_customer_id integer, input_vehicle_id integer, input_dropoff integer, input_date_from date, input_date_to date)
RETURNS VOID
AS $$ BEGIN
INSERT INTO booking (
    customer_id,
    vehicle_id,
    pickup_loc,
    dropoff_loc,
    datefrom,
    dateto,
    with_customer
)
VALUES (
    input_customer_id,
    input_vehicle_id,
    (SELECT location_id FROM vehicle WHERE vehicle_id = input_vehicle_id),
    input_dropoff,
    input_date_from,
    input_date_to,
    false
);
END;
$$ language plpgsql;

--Callan: Allows customer to swap the vehicle on a booking provided:
--The old vehicle is not in the customers possession.
--The new vehicle is available and at the same location.
CREATE FUNCTION updateBookingVehicle(input_booking_id integer, input_vehicle_id integer)
RETURNS VOID
AS $$ BEGIN
UPDATE
    booking b
SET
    vehicle_id = input_vehicle_id
WHERE
    input_booking_id = booking_id
    AND with_customer = 'false'
    AND isVehicleAvailable(input_vehicle_id)
    AND EXISTS (SELECT * FROM vehicle v WHERE v.vehicle_id = input_vehicle_id AND v.location_id = b.pickup_loc);
END;
$$ language plpgsql;
