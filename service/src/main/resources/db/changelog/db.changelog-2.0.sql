--liquibase formatted sql

--changeset natallia.vasilyeva:db.changelog-2.0 splitStatements:false logicalFilePath:classpath:/db/changelog/db.changelog-2.0.sql runOnChange:true

--User
ALTER TABLE ${database.defaultSchemaName}.users
    ADD COLUMN IF NOT EXISTS modified_at TIMESTAMP NOT NULL DEFAULT now(),
    ADD COLUMN IF NOT EXISTS modified_by VARCHAR(255),
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS created_by VARCHAR(255);

--Order
ALTER TABLE ${database.defaultSchemaName}.orders
    ADD COLUMN IF NOT EXISTS modified_at TIMESTAMP NOT NULL DEFAULT now(),
    ADD COLUMN IF NOT EXISTS modified_by VARCHAR(255),
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS created_by VARCHAR(255);

--Accident
ALTER TABLE ${database.defaultSchemaName}.accident
    ADD COLUMN IF NOT EXISTS modified_at TIMESTAMP NOT NULL DEFAULT now(),
    ADD COLUMN IF NOT EXISTS modified_by VARCHAR(255),
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS created_by VARCHAR(255);

--CarRentalTime
ALTER TABLE ${database.defaultSchemaName}.car_rental_time
    ADD COLUMN IF NOT EXISTS modified_at TIMESTAMP NOT NULL DEFAULT now(),
    ADD COLUMN IF NOT EXISTS modified_by VARCHAR(255),
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS created_by VARCHAR(255);

--UserDetails
ALTER TABLE ${database.defaultSchemaName}.user_details
    ADD COLUMN IF NOT EXISTS modified_at TIMESTAMP NOT NULL DEFAULT now(),
    ADD COLUMN IF NOT EXISTS modified_by VARCHAR(255),
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS created_by VARCHAR(255);