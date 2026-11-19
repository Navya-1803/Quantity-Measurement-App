-- table storing every quantity measurement operation
CREATE TABLE IF NOT EXISTS quantity_measurement_entity (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    this_value              DOUBLE,
    this_unit               VARCHAR(50),
    this_measurement_type   VARCHAR(50),
    that_value              DOUBLE,
    that_unit               VARCHAR(50),
    that_measurement_type   VARCHAR(50),
    operation               VARCHAR(20) NOT NULL,
    result_string           VARCHAR(255),
    result_value            DOUBLE,
    result_unit             VARCHAR(50),
    result_measurement_type VARCHAR(50),
    error_message           VARCHAR(500),
    is_error                BOOLEAN DEFAULT FALSE,
    created_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- indexes for the common history queries
CREATE INDEX IF NOT EXISTS idx_operation ON quantity_measurement_entity (operation);
CREATE INDEX IF NOT EXISTS idx_measurement_type ON quantity_measurement_entity (this_measurement_type);
