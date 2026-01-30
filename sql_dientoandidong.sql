CREATE DATABASE doanchatonl;
GO

UPDATE users
SET username = 'daominhson',
    password = '09092006'
WHERE username = 'admin';

UPDATE users
SET username = 'dms1',
    password = '09092006'
WHERE username = 'user';

SELECT * FROM users;
SELECT * FROM users;

USE doanchatonl;
GO

CREATE TABLE users (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(50)
);
GO

CREATE TABLE messages (
    id INT IDENTITY(1,1) PRIMARY KEY,
    content NVARCHAR(255)
);
GO

INSERT INTO users VALUES ('admin', '123');
INSERT INTO users VALUES ('user', '123');
