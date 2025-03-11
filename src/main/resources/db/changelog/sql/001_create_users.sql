-- liquibase formatted sql

-- changeset serevin21:123607743200
CREATE TABLE users
(
    id                                               UUID    NOT NULL,
    username                                         VARCHAR NOT NULL,
    email                                            VARCHAR NOT NULL,
    password                                         VARCHAR,
    enabled                                          BOOL NOT NULL DEFAULT true,
    status                                           VARCHAR(255) NOT NULL DEFAULT 'UNVERIFIED',
    role                                             VARCHAR NOT NULL,
    activation_code                                  VARCHAR,
    activation_code_last_sent_at                     TIMESTAMP WITHOUT TIME ZONE,
    activation_code_sent_times                       INTEGER DEFAULT 0 NOT NULL,
    invalid_activation_code_entered_times            INTEGER DEFAULT 0 NOT NULL,
    invalid_activation_code_entered_last_time_at     TIMESTAMP WITHOUT TIME ZONE,
    reset_password_code                              VARCHAR,
    reset_password_code_last_sent_at                 TIMESTAMP WITHOUT TIME ZONE,
    reset_password_sent_times                        INTEGER DEFAULT 0 NOT NULL,
    invalid_reset_password_code_entered_times        INTEGER DEFAULT 0 NOT NULL,
    invalid_reset_password_code_entered_last_time_at TIMESTAMP WITHOUT TIME ZONE,
    invalid_password_entered_times                   INTEGER DEFAULT 0 NOT NULL,
    invalid_password_entered_last_time_at            TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT uc_74165e195b2f7b25de690d14a UNIQUE (email);

ALTER TABLE users
    ADD CONSTRAINT uc_77584fbe74cc86922be2a3560 UNIQUE (username);

-- changeset serevin21:123607743200-3
CREATE TABLE refresh_token
(
    id          UUID    NOT NULL,
    token       VARCHAR NOT NULL UNIQUE,
    user_id     UUID    NOT NULL,
    expiry_at   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    device_agent VARCHAR,
    CONSTRAINT pk_refresh_token PRIMARY KEY (id),
    CONSTRAINT fk_refresh_token_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
