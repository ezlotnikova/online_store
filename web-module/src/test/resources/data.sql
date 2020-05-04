INSERT INTO user (email, password, role, is_enabled) VALUES ('test@test.shop', '$2y$12$SMWiA4gqXfKxy3mhTK2wUe6BLlMe82I4.l7fJMt9lPuiz4XuRWFvG', 'ADMINISTRATOR', 1);
INSERT INTO user_details (user_id, last_name, first_name, patronymic_name) VALUES ((SELECT id FROM user WHERE email = 'test@test.shop'), 'TestLastName', 'TestFirstName', 'TestPatronymicName');
INSERT INTO article (user_id, header, content, date) VALUES ((SELECT id FROM user WHERE email = 'test@test.shop'), 'TestHeader', 'TestContent', '2019-09-09 00:00:00');
