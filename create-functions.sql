CREATE FUNCTION customerLocMakeModelSearch(
  car_make varchar, car_model varchar,
  loc_id integer
) RETURNS TABLE (
  vehicles_id integer, registration varchar,
  make varchar, model varchar, daily_charge numeric
) AS $$ BEGIN Return Query
SELECT
    v.vehicle_id,
    v.reg,
    v.make,
    v.model,
    v.daily_fee
FROM
    vehicle v
WHERE
    v.available = true
        AND v.make = car_make
        OR car_make = ''
        AND v.model = car_model
        OR car_model = ''
        AND location_id = loc_id;
END;
$$ language plpgsql;