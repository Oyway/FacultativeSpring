INSERT INTO roles (id, name)
VALUES (1, 'ROLE_ADMIN'),
       (2, 'ROLE_STUDENT'),
       (3,	'ROLE_TEACHER');

INSERT INTO users(email, first_name, login, password, status, surname, role_id)
VALUES ('admin@gmail.com', 'admin', 'admin', '$2a$10$jjoSzfEu26ryu3iURxDd/.p2vhX7QcPWTMEYB39ecSkcv.5seoAvu', 1, 'admin', 1),
('user@gmail.com', 'Ivan', 'user', '$2a$10$7SdudOPnmeDsyx1xaaWxT.oDFOkHVmbzktLptVM4SWuBhWTOfB7o.', 1, 'Ivanov', 2),
('teacher@gmail.com', 'Anna', 'teacher', '$2a$10$Cle8ecZU23kjeFU5UTQxkOOCZRYSthEKWdsqzoRjwF5hycCSiUhJS', 1, 'Petrova', 3),
('teacher2@gmail.com', 'Petro', 'teacher2', '$2a$10$ECviYlEcVbg8WtqjCTVmo.WoZeQxYoKctXdiXqMsinqA5rOI5PYCO', 1, 'Rasputin', 3);


INSERT INTO topics(topic)
VALUES ('Java'),
('C#'),
('Android');

INSERT INTO courses(course, date_end, date_start, description, teacher_id, topic_id)
VALUES ('Java For Dummies', '2021-05-10', '2020-12-12', 'Dum', 3, 1),
('Java For Prof', '2021-05-21', '2021-04-12', 'D', 4, 1),
('Java', '2022-09-12', '2022-02-12', 'D', 3, 1),
('J', '2021-12-12', '2020-12-12', 'Lol', 4, 1),
('Je', '2021-12-12', '2020-12-12', 'Lol2', 3, 1),
('Je2', '2021-12-12', '2020-12-12', 'Lol3', 4, 1),
('Je2', '2021-12-12', '2020-12-12', 'Lol3', 3, 1),
('s', '2021-07-02', '2021-06-28', 'ss', 4, 3);

INSERT INTO users_courses (course_id, student_id)
VALUES (1, 2),
(2, 2);