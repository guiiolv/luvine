CREATE SCHEMA IF NOT EXISTS users;

CREATE SEQUENCE users.user_credentials_id_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE users.user_credentials(
    id BIGINT PRIMARY KEY default nextval('users.user_credentials_id_seq'),
    public_id UUID UNIQUE NOT NULL,
    email VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    hashed_password VARCHAR(255) NOT NULL,
    role VARCHAR(30) NOT NULL,
    active BOOLEAN NOT NULL,
    email_verified BOOLEAN NOT NULL,
    last_verification_email_sent_at TIMESTAMP WITH TIME ZONE,
    verification_email_request_count INTEGER NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT chk_user_credentials_role CHECK (role IN ('CUSTOMER', 'ADMIN'))
);

CREATE UNIQUE INDEX idx_user_credentials_email ON users.user_credentials(email) WHERE active = TRUE;