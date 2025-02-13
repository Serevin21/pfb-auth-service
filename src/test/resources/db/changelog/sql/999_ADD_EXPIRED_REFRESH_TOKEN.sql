
-- liquibase formatted sql

-- changeset serevin21:123607743299
INSERT INTO users (id, username, email, password, enabled, role)
VALUES(
    '11111111-1111-1111-1111-111111111111',
    'testuser123123',
    'test21312312@example.com',
    '$2a$10$zJ0',
    true,
    'NOVICE');


INSERT INTO refresh_token (id, token, user_id, expiry_at, device_agent)
VALUES(
    '22222222-2222-2222-2222-222222222222',
    'expired_token_example',
    '11111111-1111-1111-1111-111111111111',
    now() - INTERVAL '1 day',
    'Test Device');

