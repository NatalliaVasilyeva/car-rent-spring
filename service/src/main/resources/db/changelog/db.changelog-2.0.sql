--liquibase formatted sql

--changeset natallia.vasilyeva:db.changelog-2.0 splitStatements:false logicalFilePath:classpath:/db/changelog/db.changelog-2.0.sql runOnChange:true

--User
ALTER TABLE users
    ADD COLUMN IF NOT EXISTS modified_at TIMESTAMP NOT NULL DEFAULT now(),
    ADD COLUMN IF NOT EXISTS modified_by VARCHAR(255),
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS created_by VARCHAR(255);

--Order
ALTER TABLE orders
    ADD COLUMN IF NOT EXISTS modified_at TIMESTAMP NOT NULL DEFAULT now(),
    ADD COLUMN IF NOT EXISTS modified_by VARCHAR(255),
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS created_by VARCHAR(255);

--Accident
ALTER TABLE accident
    ADD COLUMN IF NOT EXISTS modified_at TIMESTAMP NOT NULL DEFAULT now(),
    ADD COLUMN IF NOT EXISTS modified_by VARCHAR(255),
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS created_by VARCHAR(255);

--CarRentalTime
ALTER TABLE car_rental_time
    ADD COLUMN IF NOT EXISTS modified_at TIMESTAMP NOT NULL DEFAULT now(),
    ADD COLUMN IF NOT EXISTS modified_by VARCHAR(255),
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS created_by VARCHAR(255);

--UserDetails
ALTER TABLE user_details
    ADD COLUMN IF NOT EXISTS modified_at TIMESTAMP NOT NULL DEFAULT now(),
    ADD COLUMN IF NOT EXISTS modified_by VARCHAR(255),
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS created_by VARCHAR(255);