TRUNCATE TABLE auth.audit;
TRUNCATE TABLE auth.users;
INSERT INTO auth.audit (id, entity_type, operation_type, created_by, modified_by, created_at, modified_at, new_entity_json, entity_json)
VALUES (1, '', '', 'test1', '', '2023-06-16 10:00:00', '2023-06-16 10:00:00', '', ''),
       (2, '', '', 'test2', '', '2023-06-16 10:00:00', '2023-06-16 10:00:00', '', '');
SELECT SETVAL ('auth.audit_id_seq', (SELECT MAX(id) FROM auth.audit));

INSERT INTO auth.users (id, role, profile_id, password)
VALUES (1, 'test1', 123, 'test1'),
       (2, 'test2', 1234, 'test2');
SELECT SETVAL ('auth.users_id_seq', (SELECT MAX(id) FROM auth.users));
