-- PERSONS
CREATE TABLE "persons"(
    "id" UUID NOT NULL,
    "name" VARCHAR NOT NULL,
    "last_name" VARCHAR NOT NULL,
    "email" VARCHAR NOT NULL NOT NULL,
    "dob" DATE NOT NULL NOT NULL,
    "address" VARCHAR NULL
);
ALTER TABLE "persons" ADD CONSTRAINT "persons_id" PRIMARY KEY("id");