CREATE SCHEMA if not exists service;
set search_path To service;
CREATE TABLE IF NOT EXISTS chat_users(
    userId bigserial primary key,
    username varchar(20) not null unique,
    email text unique,
    role text
);

CREATE TABLE IF NOT EXISTS chat_room(
    roomId bigserial primary key,
    chat_name text not null,
    chat_description text,
    room_createdBy bigserial not null,
    FOREIGN KEY (room_createdBy) REFERENCES chat_users(userId) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS chat_participants(
    userId bigserial not null,
    roomId bigserial not null,
    PRIMARY KEY (userId,roomId),
    FOREIGN KEY (userId) REFERENCES chat_users(userId) ON DELETE CASCADE,
    FOREIGN KEY (roomId) REFERENCES chat_room(roomId) ON DELETE CASCADE
);