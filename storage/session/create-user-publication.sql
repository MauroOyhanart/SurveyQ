CREATE PUBLICATION app_user_pub
    FOR TABLE app_user (id, email)
    WITH (publish = 'insert, update, delete');