INSERT INTO car_rent.users (id, username, email, password, role)
VALUES (1, 'Admin', 'admin@gmail.com', '{noop}TestTest1234!', 'ADMIN'),
       (2, 'Client', 'client@gmail.com', '{noop}TestTest1234!', 'CLIENT');
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
       (2, 'LUXURY', 150),
       (3, 'BUSINESS',100),
       (4, 'SPORT', 100);
SELECT SETVAL('car_rent.category_id_seq', (SELECT MAX(id) FROM car_rent.category));

INSERT INTO car_rent.brand (id, name)
VALUES (1, 'audi'),
       (2, 'bmw'),
       (3, 'citroen'),
       (4, 'honda'),
       (5, 'ford'),
       (6, 'nissan'),
       (7, 'opel'),
       (8, 'skoda'),
       (9, 'toyota'),
       (10, 'volvo'),
       (11, 'mercedes'),
       (12, 'lexus'),
       (13, 'peugeot'),
       (14, 'suzuki'),
       (15, 'hyndai');
SELECT SETVAL('car_rent.brand_id_seq', (SELECT MAX(id) FROM car_rent.brand));

INSERT INTO car_rent.model(id, brand_id, name, transmission, engine_type)
VALUES (1, (SELECT b.id FROM car_rent.brand b WHERE b.name = 'audi'), '100', 'MANUAL', 'fuel'),
       (2, (SELECT b.id FROM car_rent.brand b WHERE b.name = 'audi'), 'A8', 'automatic', 'electric'),
       (3, (SELECT b.id FROM car_rent.brand b WHERE b.name = 'audi'), 'TT', 'robot', 'diesel'),
       (4, (SELECT b.id FROM car_rent.brand b WHERE b.name = 'bmw'), 'i3', 'manual', 'fuel'),
       (5, (SELECT b.id FROM car_rent.brand b WHERE b.name = 'bmw'), 'X7', 'automatic', 'electric'),
       (6, (SELECT b.id FROM car_rent.brand b WHERE b.name = 'bmw'), 'Z4', 'robot', 'electric'),
       (7, (SELECT b.id FROM car_rent.brand b WHERE b.name = 'citroen'), 'Zsara', 'manual', 'fuel'),
       (8, (SELECT b.id FROM car_rent.brand b WHERE b.name = 'citroen'), 'Xantia', 'manual', 'fuel'),
       (9, (SELECT b.id FROM car_rent.brand b WHERE b.name = 'citroen'), 'C8', 'robot', 'electric'),
       (10, (SELECT b.id FROM car_rent.brand b WHERE b.name = 'honda'), 'CR-V', 'automatic', 'fuel'),
       (11, (SELECT b.id FROM car_rent.brand b WHERE b.name = 'honda'), 'Accord', 'manual', 'diesel'),
       (12, (SELECT b.id FROM car_rent.brand b WHERE b.name = 'honda'), 'Odyssey', 'robot', 'fuel'),
       (13, (SELECT b.id FROM car_rent.brand b WHERE b.name = 'ford'), 'Mustang', 'automatic', 'fuel'),
       (14, (SELECT b.id FROM car_rent.brand b WHERE b.name = 'nissan'), 'Micra', 'automatic', 'fuel'),
       (15, (SELECT b.id FROM car_rent.brand b WHERE b.name = 'nissan'), 'Almera', 'manual', 'fuel'),
       (16, (SELECT b.id FROM car_rent.brand b WHERE b.name = 'opel'), 'Omega', 'manual', 'diesel'),
       (17, (SELECT b.id FROM car_rent.brand b WHERE b.name = 'skoda'), 'Fabia', 'manual', 'diesel'),
       (18, (SELECT b.id FROM car_rent.brand b WHERE b.name = 'skoda'), 'Octavia', 'robot', 'fuel'),
       (19, (SELECT b.id FROM car_rent.brand b WHERE b.name = 'toyota'), 'Corolla', 'automatic', 'gas'),
       (20, (SELECT b.id FROM car_rent.brand b WHERE b.name = 'toyota'), 'Yaris', 'manual', 'fuel'),
       (21, (SELECT b.id FROM car_rent.brand b WHERE b.name = 'volvo'), 'XC90', 'manual', 'fuel'),
       (22, (SELECT b.id FROM car_rent.brand b WHERE b.name = 'mercedes'), 'CLS', 'robot', 'diesel'),
       (23, (SELECT b.id FROM car_rent.brand b WHERE b.name = 'lexus'), 'NX I', 'automatic', 'fuel'),
       (24, (SELECT b.id FROM car_rent.brand b WHERE b.name = 'peugeot'), '308', 'automatic', 'fuel'),
       (25, (SELECT b.id FROM car_rent.brand b WHERE b.name = 'suzuki'), 'Grand Vitara', 'manual', 'diesel'),
       (26, (SELECT b.id FROM car_rent.brand b WHERE b.name = 'suzuki'), 'Solaris', 'manual', 'fuel');
SELECT SETVAL('car_rent.model_id_seq', (SELECT MAX(id) FROM car_rent.model));

INSERT INTO car_rent.car(id, brand_id, model_id, category_id, color, year, car_number, vin, repaired)
VALUES (1, (SELECT b.id FROM brand b WHERE b.name = 'audi'),
        (SELECT m.id FROM model m WHERE m.name = '100'), (SELECT c.id FROM category c WHERE c.name = 'ECONOMY'),
        'white', '2020', '7865AE-7', 'AmhBHqJ8BgD0p3PRgkoi', 'false'),
       (2, (SELECT b.id FROM brand b WHERE b.name = 'audi'),
        (SELECT m.id FROM model m WHERE m.name = 'A8'), (SELECT c.id FROM category c WHERE c.name = 'BUSINESS'),
        'black', '2020', '7869AE-7', '6iFm73ncHPAAmpZoi0pM', 'false'),
       (3, (SELECT b.id FROM brand b WHERE b.name = 'audi'),
        (SELECT m.id FROM model m WHERE m.name = 'TT'), (SELECT c.id FROM category c WHERE c.name = 'SPORT'),
        'black', '2018', '7845HR-7', 'UR1g99JsoqcyovJzXhLJ', 'false'),
       (4, (SELECT b.id FROM brand b WHERE b.name = 'bmw'),
        (SELECT m.id FROM model m WHERE m.name = 'i3'), (SELECT c.id FROM category c WHERE c.name = 'ECONOMY'),
        'red', '2021', '9823JK-7', '5Wgm1wipFhdUviBZ4Qil', 'false'),
       (5, (SELECT b.id FROM brand b WHERE b.name = 'bmw'),
        (SELECT m.id FROM model m WHERE m.name = 'X7'), (SELECT c.id FROM category c WHERE c.name = 'BUSINESS'),
        'yellow', '2022', '9983SL-7', '6EaVJP9u5tip98oEoKHu', 'false'),
       (6, (SELECT b.id FROM brand b WHERE b.name = 'bmw'),
        (SELECT m.id FROM model m WHERE m.name = 'Z4'), (SELECT c.id FROM category c WHERE c.name = 'LUXURY'),
        'blue', '2015', '3562AS-7', 'TyHALJfzxxAbI3bWv4vu', 'false'),
       (7, (SELECT b.id FROM brand b WHERE b.name = 'citroen'),
        (SELECT m.id FROM model m WHERE m.name = 'Zsara'), (SELECT c.id FROM category c WHERE c.name = 'ECONOMY'),
        'green', '2019', '6753MM-7', 'vrG8tK2NmFruzTjMZ5X7',
        'false'),
       (8, (SELECT b.id FROM brand b WHERE b.name = 'citroen'),
        (SELECT m.id FROM model m WHERE m.name = 'Xantia'), (SELECT c.id FROM category c WHERE c.name = 'ECONOMY'),
        'black', '2016', '3425MM-7', 'ry4r95f5iCSVhhLtFrxi',
        'false'),
       (9, (SELECT b.id FROM brand b WHERE b.name = 'citroen'),
        (SELECT m.id FROM model m WHERE m.name = 'C8'), (SELECT c.id FROM category c WHERE c.name = 'BUSINESS'),
        'black', '2021', '7654LM-7', 'D0wIIzZG0M1gIapEVLck', 'false'),
       (10, (SELECT b.id FROM brand b WHERE b.name = 'honda'),
        (SELECT m.id FROM model m WHERE m.name = 'CR-V'), (SELECT c.id FROM category c WHERE c.name = 'BUSINESS'),
        'white', '2001', '3462LM-7', 'XgKb21CnAQmCDYBCWTOZ', 'false'),
       (11, (SELECT b.id FROM brand b WHERE b.name = 'honda'),
        (SELECT m.id FROM model m WHERE m.name = 'Accord'), (SELECT c.id FROM category c WHERE c.name = 'ECONOMY'),
        'red', '2017', '6784DS-7', 'zCwNgYF9AvMsRFnUIvFt', 'true'),
       (12, (SELECT b.id FROM brand b WHERE b.name = 'honda'),
        (SELECT m.id FROM model m WHERE m.name = 'Odyssey'), (SELECT c.id FROM category c WHERE c.name = 'LUXURY'),
        'black', '2022', '8769ES-7', 'i9b7a4Ut7O0bRlNFKiws',
        'false'),
       (13, (SELECT b.id FROM brand b WHERE b.name = 'ford'),
        (SELECT m.id FROM model m WHERE m.name = 'Mustang'), (SELECT c.id FROM category c WHERE c.name = 'SPORT'),
        'black', '2022', '8770ES-7', 'pJTvhGLZm6MdP2PnFErg',
        'false'),
       (14, (SELECT b.id FROM brand b WHERE b.name = 'nissan'),
        (SELECT m.id FROM model m WHERE m.name = 'Micra'), (SELECT c.id FROM category c WHERE c.name = 'ECONOMY'),
        'green', '2014', '5467AL-7', 'YbHdXw3iwuwg078TrHzh',
        'false'),
       (15, (SELECT b.id FROM brand b WHERE b.name = 'nissan'),
        (SELECT m.id FROM model m WHERE m.name = 'Almera'), (SELECT c.id FROM category c WHERE c.name = 'ECONOMY'),
        'blue', '2013', '5367Kl-7', 'xZHKoXpuHL2mUOVC5kaI',
        'false'),
       (16, (SELECT b.id FROM brand b WHERE b.name = 'opel'),
        (SELECT m.id FROM model m WHERE m.name = 'Omega'), (SELECT c.id FROM category c WHERE c.name = 'ECONOMY'),
        'black', '2017', '9076Kl-7', 'TvYjOVcvNn1slI7nLPrA',
        'false'),
       (17, (SELECT b.id FROM brand b WHERE b.name = 'skoda'),
        (SELECT m.id FROM model m WHERE m.name = 'Fabia'), (SELECT c.id FROM category c WHERE c.name = 'ECONOMY'),
        'red', '2019', '9874NM-7', 'he4tYZ2FvFhEd6ft4dGu', 'false'),
       (18, (SELECT b.id FROM brand b WHERE b.name = 'skoda'),
        (SELECT m.id FROM model m WHERE m.name = 'Octavia'), (SELECT c.id FROM category c WHERE c.name = 'BUSINESS'),
        'white', '2021', '9976BG-7', 'Rw6hl3GJmZ0WLXOerR1A',
        'false'),
       (19, (SELECT b.id FROM brand b WHERE b.name = 'toyota'),
        (SELECT m.id FROM model m WHERE m.name = 'Corolla'), (SELECT c.id FROM category c WHERE c.name = 'ECONOMY'),
        'black', '2020', '6732VA-7', '36ft6b3IKuNLteNp6yNJ',
        'false'),
       (20, (SELECT b.id FROM brand b WHERE b.name = 'toyota'),
        (SELECT m.id FROM model m WHERE m.name = 'Yaris'), (SELECT c.id FROM category c WHERE c.name = 'ECONOMY'),
        'blue', '2013', '7619FS-7', 'cJiqu4QwyBTwfi83skhN', 'false'),
       (21, (SELECT b.id FROM brand b WHERE b.name = 'volvo'),
        (SELECT m.id FROM model m WHERE m.name = 'XC90'), (SELECT c.id FROM category c WHERE c.name = 'LUXURY'),
        'black', '2021', '9845HN-7', 'GFlHws5QLhjJPH77WDct', 'false'),
       (22, (SELECT b.id FROM brand b WHERE b.name = 'mercedes'),
        (SELECT m.id FROM model m WHERE m.name = 'CLS'), (SELECT c.id FROM category c WHERE c.name = 'LUXURY'),
        'black', '2022', '9945CX-7', 'Wjj6yqGUTyC2UQ3gELpp', 'false'),
       (23, (SELECT b.id FROM brand b WHERE b.name = 'lexus'),
        (SELECT m.id FROM model m WHERE m.name = 'NX I'), (SELECT c.id FROM category c WHERE c.name = 'BUSINESS'),
        'red', '2022', '9949FX-7', 'qy823ZVYGQXlk08lYnGe', 'false'),
       (24, (SELECT b.id FROM brand b WHERE b.name = 'peugeot'),
        (SELECT m.id FROM model m WHERE m.name = '308'), (SELECT c.id FROM category c WHERE c.name = 'ECONOMY'),
        'white', '2018', '6748KM-7', 'UaaoXEefc7GUddmgLu6e', 'false'),
       (25, (SELECT b.id FROM brand b WHERE b.name = 'suzuki'),
        (SELECT m.id FROM model m WHERE m.name = 'Grand Vitara'), (SELECT c.id FROM category c WHERE c.name = 'ECONOMY'),
        'green', '2017', '4598DF-7', '9qz4CVJYJcGtRAx7lbRo',
        'false'),
       (26, (SELECT b.id FROM brand b WHERE b.name = 'suzuki'),
        (SELECT m.id FROM model m WHERE m.name = 'Solaris'), (SELECT c.id FROM category c WHERE c.name = 'ECONOMY'),
        'yellow', '2010', '3298DF-7', 'nm7UtHoNXTdZ0v3QJusb',
        'false');
SELECT SETVAL('car_rent.car_id_seq', (SELECT MAX(id) FROM car_rent.car));

INSERT INTO car_rent.orders (id, date, user_id, car_id, passport, insurance, order_status, sum)
VALUES (1, TO_TIMESTAMP('2022-07-01 10:30:00', 'YYYY-MM-DD HH24:MI:SS'),
        (SELECT u.id FROM users u WHERE u.email LIKE 'client@gmail.com'),
        (SELECT c.id FROM car c WHERE c.vin = 'i9b7a4Ut7O0bRlNFKiws'), 'MP1234567', 'true', 'PAYED', 1020),
       (2, TO_TIMESTAMP('2022-07-04 09:00:00', 'YYYY-MM-DD HH24:MI:SS'),
        (SELECT u.id FROM users u WHERE u.email LIKE 'client@gmail.com'),
        (SELECT c.id FROM car c WHERE c.vin = 'YbHdXw3iwuwg078TrHzh'), 'MP1234567', 'true', 'NOT_PAYED', 535),
       (3, TO_TIMESTAMP('2022-07-07 09:00:00', 'YYYY-MM-DD HH24:MI:SS'),
        (SELECT u.id FROM users u WHERE u.email LIKE 'client@gmail.com'),
        (SELECT c.id FROM car c WHERE c.vin = 'qy823ZVYGQXlk08lYnGe'), 'MP1234567', 'false', 'CONFIRMATION_WAIT', 700);
SELECT SETVAL('car_rent.orders_id_seq', (SELECT MAX(id) FROM car_rent.orders));

INSERT INTO car_rent.car_rental_time (id, order_id, start_rental_date, end_rental_date)
VALUES (1, (SELECT o.id FROM orders o WHERE o.order_status = 'PAYED'),
        TO_TIMESTAMP('2022-07-02 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
        TO_TIMESTAMP('2022-07-03 23:59:00', 'YYYY-MM-DD HH24:MI:SS')),
       (2, (SELECT o.id FROM orders o WHERE o.order_status = 'NOT_PAYED'),
        TO_TIMESTAMP('2022-07-04 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
        TO_TIMESTAMP('2022-07-04 23:59:00', 'YYYY-MM-DD HH24:MI:SS')),
       (3, (SELECT o.id FROM orders o WHERE o.order_status = 'CONFIRMATION_WAIT'),
        TO_TIMESTAMP('2022-07-07 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
        TO_TIMESTAMP('2022-07-08 23:59:00', 'YYYY-MM-DD HH24:MI:SS'));
SELECT SETVAL('car_rent.car_rental_time_id_seq', (SELECT MAX(id) FROM car_rent.car_rental_time));

INSERT INTO car_rent.accident (id, order_id, accident_date, description, damage)
VALUES (1, (SELECT o.id FROM orders o WHERE o.order_status = 'PAYED'),
        TO_TIMESTAMP('2022-07-02 16:34:00', 'YYYY-MM-DD HH24:MI:SS'),
        'faced tree', '75.50'),
       (2, (SELECT o.id FROM orders o WHERE o.order_status = 'NOT_PAYED'),
        TO_TIMESTAMP('2022-07-04 01:00:00', 'YYYY-MM-DD HH24:MI:SS'),
        'broke wheel', '10');
SELECT SETVAL('car_rent.accident_id_seq', (SELECT MAX(id) FROM car_rent.accident));