DELETE FROM user_roles;
DELETE FROM votes;
DELETE FROM dishes;
DELETE FROM restaurants;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (email, password)
VALUES ('user@gmail.com', '{noop}user'),
       ('admin@gmail.com', '{noop}admin');

INSERT INTO user_roles (role, user_id)
VALUES ('ROLE_USER', 100000),
       ('ROLE_ADMIN', 100001),
       ('ROLE_USER', 100001);

INSERT INTO restaurants (name)
VALUES ('McDonalds'),
       ('KFC'),
       ('Subway');

INSERT INTO dishes (date, name, price, restaurant_id)
VALUES ('2020-04-05', 'Hamburger', 150, 100002),
       ('2020-04-05', 'Cheeseburger', 150, 100002),
       ('2020-04-05', 'Fruit salad', 100, 100003),
       ('2020-04-05', 'Salad', 100, 100003),
       ('2020-04-05', 'French fries', 100, 100004),
       ('2020-04-05', 'Chicken nuggets', 150, 100004),
       ('2020-04-07', 'Apple pie', 100, 100002),
       ('2020-04-07', 'Cookie', 100, 100002),
       ('2020-04-07', 'Croissant', 150, 100003),
       ('2020-04-07', 'Hamburger', 160, 100003),
       ('2020-04-07', 'Cheeseburger', 160, 100004),
       ('2020-04-07', 'Fruit salad', 110, 100004);

INSERT INTO dishes (name, price, restaurant_id)
VALUES ('Hamburger', 150, 100002),
       ('Cheeseburger', 150, 100002),
       ('Fruit salad', 100, 100002),
       ('Salad', 100, 100003),
       ('French fries', 100, 100003),
       ('Chicken nuggets', 150, 100003),
       ('Apple pie', 100, 100004),
       ('Cookie', 100, 100004),
       ('Croissant', 150, 100004);

INSERT INTO votes (date, user_id, restaurant_id)
VALUES ('2020-04-05', 100000, 100002),
       ('2020-04-05', 100001, 100002),
       ('2020-04-07', 100000, 100003),
       ('2020-04-07', 100001, 100004);

INSERT INTO votes (user_id, restaurant_id)
VALUES (100001, 100002);
