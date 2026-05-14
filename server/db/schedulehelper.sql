-- DEPENDANT

DROP TABLE IF EXISTS shift_role CASCADE;

-- INDEPENDANT

DROP TABLE IF EXISTS employee CASCADE;
DROP TABLE IF EXISTS shift CASCADE;
DROP TABLE IF EXISTS role_type CASCADE;

-- INDEPENDANT

CREATE TABLE employee (
    id              SERIAL,
    first_name      TEXT NOT NULL,
    last_name       TEXT,

    PRIMARY KEY (id)
);

CREATE TABLE shift (
    id              SERIAL,
    start_time      TIMESTAMPTZ NOT NULL,
    end_time        TIMESTAMPTZ NOT NULL,

    PRIMARY KEY (id)
);

CREATE TABLE role_type (
    id              SERIAL,
    title           TEXT NOT NULL UNIQUE,

    PRIMARY KEY (id)
);

-- DEPENDANT

CREATE TABLE shift_role (
    id              SERIAL,
    shift_id        INTEGER NOT NULL,
    employee_id     INTEGER NOT NULL,
    role_id         INTEGER NOT NULL,
    performance     SMALLINT,

    CHECK (performance BETWEEN 0 AND 100),

    PRIMARY KEY (id),

    CONSTRAINT fk_shift_id FOREIGN KEY (shift_id) REFERENCES shift (id) ON DELETE CASCADE,
    CONSTRAINT fk_employee_id FOREIGN KEY (employee_id) REFERENCES employee (id) ON DELETE RESTRICT,
    CONSTRAINT fk_role_id FOREIGN KEY (role_id) REFERENCES role_type (id) ON DELETE RESTRICT
);
