CREATE SCHEMA IF NOT EXISTS auth;

CREATE SEQUENCE auth.refresh_tokens_id_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE auth.refresh_tokens(
    id BIGINT PRIMARY KEY DEFAULT nextval('auth.refresh_tokens_id_seq'),
    user_public_id UUID NOT NULL,
    token_hash VARCHAR(255) UNIQUE NOT NULL,
    device_info VARCHAR(255) NOT NULL,
    ip_address VARCHAR(100) NOT NULL,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    revoked BOOLEAN NOT NULL,
    replaced_by_token_hash VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE
);

CREATE INDEX idx_refresh_tokens_user_public_id ON auth.refresh_tokens(user_public_id);
CREATE INDEX idx_refresh_tokens_replaced_by_token_hash ON auth.refresh_tokens(replaced_by_token_hash);