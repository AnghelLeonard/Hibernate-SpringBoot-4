-- Drop tables
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

-- Table "flight"
CREATE TABLE "flight" (
  "id"            BIGINT       NOT NULL,
  "flight_number" VARCHAR(255) NOT NULL,
  "capacity"      INTEGER      NOT NULL,
  "version"       BIGINT       NOT NULL,
  CONSTRAINT "flights_pk" PRIMARY KEY ("id"),
  CONSTRAINT "flights_flight_number_uk" UNIQUE ("flight_number")
);

-- Table "seat_assignment"
CREATE TABLE "seat_assignment" (
  "id"             BIGINT                   NOT NULL,
  "flight_id"      BIGINT                   NOT NULL,
  "seat_code"      VARCHAR(255)             NOT NULL,
  "passenger_name" VARCHAR(255)             NOT NULL,
  "assigned_at"    TIMESTAMP WITH TIME ZONE NOT NULL,
  CONSTRAINT "seat_assignments_pk" PRIMARY KEY ("id"),
  CONSTRAINT "seat_assignments_flight_fk" FOREIGN KEY ("flight_id") REFERENCES "flight" ("id"),
  CONSTRAINT "seat_assignments_flight_seat_uk" UNIQUE ("flight_id", "seat_code")
);

