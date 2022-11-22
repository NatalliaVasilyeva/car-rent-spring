INSERT INTO car_rent.users (id, username, email, password, role)
VALUES (1, 'Admin', 'admin@gmail.com', 'VmFzaWxlY2hla0JlbDEyMyE=', 'ADMIN'),
       (2, 'Client', 'client@gmail.com', 'VmFzaWxlY2hla0JlbDEyMyE=', 'CLIENT');
SELECT SETVAL('car_rent.users_id_seq', (SELECT MAX(id) FROM car_rent.users));

INSERT INTO car_rent.user_details (id, user_id, name, surname, address, phone, birthday, registration_date)
VALUES (1, (SELECT id FROM car_rent.users WHERE email = 'admin@gmail.com'), 'Ivan', 'Ivanov', 'Minsk',
        '+375 29 124 56 78', '1986-07-02 00:00:00', '2022-09-22'),
       (2, (SELECT id FROM car_rent.users WHERE email = 'client@gmail.com'), 'Petia', 'Petrov', 'Minsk',
        '+375 29 124 56 79', '1989-03-12 00:00:00', '2022-09-22');
SELECT SETVAL('car_rent.user_details_id_seq', (SELECT MAX(id) FROM car_rent.user_details));

INSERT INTO car_rent.driver_license (id, user_details_id, number, issue_date, expired_date)
VALUES (1, (SELECT id FROM car_rent.user_details WHERE phone = '+375 29 124 56 78'), 'AB12345', '2015-03-02 00:00:00',
        '2025-03-01 00:00:00'),
       (2, (SELECT id FROM car_rent.user_details WHERE phone = '+375 29 124 56 79'), 'AB12346', '2014-03-02 00:00:00',
        '2024-12-01 00:00:00');
SELECT SETVAL('car_rent.driver_license_id_seq', (SELECT MAX(id) FROM car_rent.driver_license));

INSERT INTO car_rent.category (id, name, price)
VALUES (1, 'ECONOMY', 50),
       (2, 'BUSINESS', 100);
SELECT SETVAL('car_rent.category_id_seq', (SELECT MAX(id) FROM car_rent.category));

INSERT INTO car_rent.brand (id, name)
VALUES (1, 'audi'),
       (2, 'mercedes');
SELECT SETVAL('car_rent.brand_id_seq', (SELECT MAX(id) FROM car_rent.brand));

INSERT INTO car_rent.model (id, brand_id, category_id, name, transmission, engine_type)
VALUES (1, (SELECT id FROM car_rent.brand WHERE name = 'audi'),
        (SELECT id FROM car_rent.category WHERE name = 'ECONOMY'), 'A8', 'MANUAL', 'FUEL'),
       (2, (SELECT id FROM car_rent.brand WHERE name = 'mercedes'),
        (SELECT id FROM car_rent.category WHERE name = 'BUSINESS'), 'Benz', 'ROBOT', 'FUEL');
SELECT SETVAL('car_rent.model_id_seq', (SELECT MAX(id) FROM car_rent.model));

INSERT INTO car_rent.car (id, model_id, color, year, car_number, vin, repaired)
VALUES (1, '1', 'WHITE', '2020', '7865AE-7', 'AmhBHqJ8BgD0p3PRgkoi', 'false'),
       (2, '2', 'RED', '2022', '7834AE-7', 'AmhBdhjJ8BgD0p3PRgkoi', 'false');
SELECT SETVAL('car_rent.car_id_seq', (SELECT MAX(id) FROM car_rent.car));

INSERT INTO car_rent.orders (id, date, user_id, car_id, passport, insurance, order_status, sum)
VALUES (1, '2022-07-01 00:00:00', (SELECT id FROM car_rent.users WHERE email = 'admin@gmail.com'),
        (SELECT id FROM car_rent.car WHERE vin = 'AmhBHqJ8BgD0p3PRgkoi'), 'MP1234567', 'true', 'CONFIRMATION_WAIT',
        1020),
       (2, '2022-07-02 00:00:00', (SELECT id FROM car_rent.users WHERE email = 'client@gmail.com'),
        (SELECT id FROM car_rent.car WHERE vin = 'AmhBdhjJ8BgD0p3PRgkoi'), 'MP1234589', 'true', 'PAYED', 300);
SELECT SETVAL('car_rent.orders_id_seq', (SELECT MAX(id) FROM car_rent.orders));

INSERT INTO car_rent.car_rental_time (id, order_id, start_rental_date, end_rental_date)
VALUES (1, '1', '2022-07-02 00:00:00', '2022-07-03 23:59:00'),
       (2, '2', '2022-09-02 00:00:00', '2022-09-04 23:59:00');
SELECT SETVAL('car_rent.car_rental_time_id_seq', (SELECT MAX(id) FROM car_rent.car_rental_time));

INSERT INTO car_rent.accident (id, order_id, accident_date, description, damage)
VALUES (1, '1', '2022-07-02 00:00:00', 'faced tree', 75.50),
       (2, '2', '2022-09-03 00:00:00', 'accident', 10.05);
SELECT SETVAL('car_rent.accident_id_seq', (SELECT MAX(id) FROM car_rent.accident));