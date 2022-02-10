-- noinspection SqlWithoutWhereForFile
-- noinspection SqlResolveForFile

DELETE FROM users;
INSERT INTO users (email, version, full_name, enabled, email_confirmed)
VALUES ('john.doe@mail.com', 1, 'John Doe', true, true);
INSERT INTO users (email, version, full_name, enabled, email_confirmed)
VALUES ('jane.doe@mail.com', 1, 'Jane Doe', false, false);

DELETE FROM tasks_tags;

DELETE FROM task;

INSERT INTO task (user_id, title, status) VALUES (1, 'Unprocessed task 1', 'UNPROCESSED');
INSERT INTO task (user_id, title, status) VALUES (1, 'Unprocessed task 2', 'UNPROCESSED');
INSERT INTO task (user_id, title, status) VALUES (1, 'Unprocessed task 3', 'UNPROCESSED');

INSERT INTO task (user_id, title, status, deadline) VALUES (1, 'Processed task 1', 'PROCESSED', NULL);
INSERT INTO task (user_id, title, status, deadline) VALUES (1, 'Processed task 2', 'PROCESSED', '2022-01-01 00:00:00');
INSERT INTO task (user_id, title, status, deadline) VALUES (1, 'Processed task 3', 'PROCESSED', '2022-01-02 23:59:00');

INSERT INTO task (user_id, title, status, completed_at)
VALUES (1, 'Completed task 1', 'COMPLETED', NULL);
INSERT INTO task (user_id, title, status, completed_at)
VALUES (1, 'Completed task 2', 'COMPLETED', '2022-01-01 00:00:00');
INSERT INTO task (user_id, title, status, completed_at)
VALUES (1, 'Completed task 3', 'COMPLETED', '2022-01-02 23:59:00');

DELETE FROM tag;
INSERT INTO tag (user_id, name) VALUES (1, 'Test tag');

DELETE FROM tasks_tags;
INSERT INTO tasks_tags (task_id, tag_id) VALUES (1, 1);

DELETE FROM email_confirmation_token;
INSERT INTO email_confirmation_token (user_id, email, token_value, created_at, expires_at)
VALUES (2, 'jane.doe@mail.com', '4b1f7955-a406-4d36-8cbe-d6c61f39e27d', '2020-01-01 00:00:00', '9999-01-01 00:00:00');

DELETE FROM password_reset_confirmation_token;
INSERT INTO password_reset_confirmation_token (user_id, email, token_value, created_at, expires_at)
VALUES (1, 'john.doe@mail.com', 'cf575578-cddf-4773-b1e0-5f37cbb0a8d9', '2020-01-01 00:00:00', '9999-01-01 00:00:00');
