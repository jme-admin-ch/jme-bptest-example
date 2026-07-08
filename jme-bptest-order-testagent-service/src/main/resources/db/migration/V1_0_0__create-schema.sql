CREATE TABLE test_instance
(
    test_id           VARCHAR PRIMARY KEY,
    created_at        TIMESTAMP WITH TIME ZONE,
    test_case         VARCHAR NOT NULL,
    callback_base_url VARCHAR NOT NULL
);

CREATE TABLE parameters
(
    test_id VARCHAR,
    name    VARCHAR,
    value   VARCHAR
);


