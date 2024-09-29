TRUNCATE TABLE service.chat_participants CASCADE;
TRUNCATE TABLE service.chat_room CASCADE;
TRUNCATE TABLE service.chat_users CASCADE;
SELECT setval((SELECT pg_get_serial_sequence('service.chat_users', 'userid') AS sequence_name), 1, false);
SELECT setval((SELECT pg_get_serial_sequence('service.chat_room', 'roomid') AS sequence_name), 1, false);
set search_path To service;
-- Добавление дополнительных тестовых Пользователей
INSERT INTO chat_users (username, email,role)
VALUES
    ('Sarah Lee', 'sarah.lee@example.com','admin'),
    ('Michael Chen', 'michael.chen@example.com','admin'),
    ('Emily Wilson', 'emily.wilson@example.com','user'),
    ('David Kim', 'david.kim@example.com','user'),
    ('Jennifer Park', 'jennifer.park@example.com','user');

-- Добавление дополнительных тестовых Чатов
INSERT INTO chat_room (chat_name, chat_description, room_createdby)
VALUES
    ('Photography Club', 'Photo discussion club',1),
    ('Book Club', 'Fantasy lovers club',2),
    ('Cooking Enthusiasts', 'Cooking recipe search club',1),
    ('Fitness Buddies', 'Sports lovers club',2),
    ('Gaming Group', 'Computer games, technology, humor',4);

-- Добавление связей "многие-ко-многим" между дополнительными Пользователями и Чатами
INSERT INTO chat_participants (userid, roomid)
VALUES
    (1, 3),
    (1, 4),
    (2, 4),
    (2, 5),
    (3, 5),
    (4, 1),
    (4, 2),
    (5, 2),
    (5, 3);
