insert into esgschema.roles(id, name)
VALUES (1, 'ADMIN');
insert into esgschema.roles(id, name)
VALUES (2, 'USER');

INSERT INTO esgschema.categories (name)
VALUES ('Панели'),
       ('Инверторы'),
       ('Система крепления'),
       ('Солнечный кабель'),
       ('Кабель-каналы'),
       ('Щитовая IP65 DC'),
       ('Щитовая IP65 AC'),
       ('Кабеля и провода'),
       ('Учет и измерение'),
       ('Монтаж / Пусконаладка / Доставка'),
       ('Пакеты документов'),
       ('Спецтехника'),
       ('Подстанционные работы'),
       ('Трасса / Опоры / Земляные работы'),
       ('Щитовые работы и замена');

INSERT INTO esgschema.users (email, first_name, last_name, password)
VALUES ('esgroupandrei@gmail.com',
        'Андрей',
        'Яцко',
        '$2a$10$RA7gVQdQ4gudgZmbuOrzbe1FnWbw1LjBHtQVOSjo.li1tQspLnFI.');

INSERT INTO esgschema.account(name, balance)
values ('Cashbox', 0);

INSERT INTO esgschema.account(name, balance)
values ('Bank', 0)