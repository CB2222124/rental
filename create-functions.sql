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

/*
@author Rania, following function allows employees to update a vehicle reg only if it exists

*/

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
	RAISE 'Vehicle % does not exist', vehicle_idToLook;
END IF;

END;
$$;

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
	RAISE 'Vehicle % does not exist', vehicle_idToLook;
END IF;

END;
$$;