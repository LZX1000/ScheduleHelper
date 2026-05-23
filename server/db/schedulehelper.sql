-- ============================================================
-- File:    schedulehelper.sql
-- Author:  Xander White
-- Date:    2026-05-14
-- Desc:    Initial shift logging schema. Defines employees,
--          shifts, roles, and shift performance tracking.
-- ============================================================

-- DEPENDANT
DROP TABLE IF EXISTS shift_role CASCADE;

-- INDEPENDANT
DROP TABLE IF EXISTS employee CASCADE;
DROP TABLE IF EXISTS shift CASCADE;
DROP TABLE IF EXISTS role_type CASCADE;

-- ------------------------------------------------------------
-- Table:  employee
-- Desc:   Stores individual employee records.
--
-- Columns:
--   id          - Serial primary key.
--   first_name  - Employee's first name. Required.
--   last_name   - Employee's last name. Optional.
-- ------------------------------------------------------------
CREATE TABLE employee (
    id              SERIAL,
    first_name      TEXT NOT NULL,
    last_name       TEXT DEFAULT '',

    PRIMARY KEY (id)
);

-- ------------------------------------------------------------
-- Table:  shift
-- Desc:   Represents a scheduled shift with a start and end
--         time. Timezone-aware.
--
-- Columns:
--   id          - Serial primary key.
--   start_time  - Shift start time. Timezone-aware, must be
--                 less than end time. Required.
--   end_time    - Shift end time. Timezone-aware. Required.
-- ------------------------------------------------------------
CREATE TABLE shift (
    id              SERIAL,
    start_time      TIMESTAMPTZ NOT NULL,
    end_time        TIMESTAMPTZ NOT NULL,

    CHECK (start_time < end_time),

    PRIMARY KEY (id)
);

-- ------------------------------------------------------------
-- Table:  role_type
-- Desc:   Lookup table of possible roles an employee can fill
--         during a shift.
--
-- Columns:
--   id      - Serial primary key.
--   title   - Role name. Required. Must be unique.
-- ------------------------------------------------------------
CREATE TABLE role_type (
    id              SERIAL,
    title           TEXT NOT NULL UNIQUE,

    PRIMARY KEY (id)
);

-- ------------------------------------------------------------
-- Table:  shift_role
-- Desc:   Junction table linking an employee and a role to a
--         specific shift. Records per-shift performance for
--         use in skill and chemistry calculations.
--
-- Columns:
--   id          - Serial primary key.
--   shift_id    - FK to shift. Cascades on delete.
--   employee_id - FK to employee. Restricted on delete.
--   role_id     - FK to role_type. Restricted on delete.
--   performance - Score for this employee in this role on
--                 this shift. Integer 0-100. Optional.
-- ------------------------------------------------------------
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
