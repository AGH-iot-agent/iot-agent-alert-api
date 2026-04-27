CREATE TABLE IF NOT EXISTS alerts (
    id BIGSERIAL PRIMARY KEY,
    timestamp BIGINT NOT NULL,
    device_id VARCHAR(128) NOT NULL,
    severity VARCHAR(32) NOT NULL,
    reason VARCHAR(128) NOT NULL,
    temperature DOUBLE PRECISION NOT NULL,
    threshold DOUBLE PRECISION NOT NULL,
    leak BOOLEAN NOT NULL,
    anomaly BOOLEAN NOT NULL,
    anomaly_score DOUBLE PRECISION NOT NULL,
    krakow_zone VARCHAR(64) NOT NULL,
    active BOOLEAN NOT NULL
);
