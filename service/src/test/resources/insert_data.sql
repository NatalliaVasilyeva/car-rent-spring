INSERT INTO car_rent.users (login, email, password, role)
VALUES ('Admin', 'admin@gmail.com', 'VasilechekBel123!', 'ADMIN'),
       ('Client', 'client@gmail.com', 'VasilechekBel123!', 'CLIENT');
INSERT INTO car_rent.user_details (user_id, name, surname, address, phone, birthday, registration_date)
VALUES ('1', 'Ivan', 'Ivanov', 'Minsk', '+375 29 124 56 78', '1986-07-02 00:00:00', '2022-09-22 20:30:00'),
       ('2', 'Petia', 'Petrov', 'Minsk', '+375 29 124 56 79', '1989-03-12 00:00:00', '2022-09-22 20:31:00');
INSERT INTO car_rent.driver_license (user_details_id, number, issue_date, expired_date)
VALUES ('1', 'AB12345', '2015-03-02 00:00:00', '2025-03-01 00:00:00'),
       ('2', 'AB12346', '2014-03-02 00:00:00', '2024-12-01 00:00:00');
INSERT INTO car_rent.category (name, price)
VALUES ('ECONOMY', 50),
       ('BUSINESS', 100);
INSERT INTO car_rent.brand (name)
VALUES ('audi'),
       ('mercedes');
INSERT INTO car_rent.model (brand_id, category_id, name, transmission, engine_type)
VALUES ('1', '1', 'A8', 'MANUAL', 'FUEL'),
       ('2', '2', 'Benz', 'ROBOT', 'FUEL');
INSERT INTO car_rent.car (model_id, color, year, car_number, vin, repaired)
VALUES ('1', 'WHITE', '2020', '7865AE-7', 'AmhBHqJ8BgD0p3PRgkoi', 'false'),
       ('2', 'RED', '2022', '7834AE-7', 'AmhBdhjJ8BgD0p3PRgkoi', 'false');
INSERT INTO car_rent.orders (date, user_id, car_id, passport, insurance, order_status, sum)
VALUES ('2022-07-01 00:00:00', '1', '1', 'MP1234567', 'true', 'CONFIRMATION_WAIT', 1020),
       ('2022-07-02 00:00:00', '2', '2', 'MP1234589', 'true', 'PAYED', 300);
INSERT INTO car_rent.car_rental_time (order_id, start_rental_date, end_rental_date)
VALUES ('1', '2022-07-02 00:00:00', '2022-07-03 23:59:00'),
       ('2', '2022-09-02 00:00:00', '2022-09-04 23:59:00');
INSERT INTO car_rent.accident (order_id, accident_date, description, damage)
VALUES ('1', '2022-07-02 16:34:00', 'faced tree', 75.50),
       ('2', '2022-09-03 16:35:00', 'accident', 10.05);