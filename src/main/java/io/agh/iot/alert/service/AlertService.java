package io.agh.iot.alert.service;

import io.agh.iot.alert.controller.AlertController.AlertRequest;
import io.agh.iot.alert.controller.AlertController.AlertResponse;
import io.agh.iot.alert.model.AlertEvent;
import io.agh.iot.alert.repository.AlertRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlertService.class);

    private final AlertRepository alertRepository;

    public AlertService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    public AlertResponse evaluate(AlertRequest request) {
        double temperature = request.temperature() != null ? request.temperature() : 0.0;
        double threshold = request.threshold() != null ? request.threshold() : 30.0;
        boolean leak = Boolean.TRUE.equals(request.leak());
        boolean anomaly = Boolean.TRUE.equals(request.anomaly());
        double anomalyScore = request.anomalyScore() != null ? request.anomalyScore() : 0.0;

        boolean temperatureExceeded = temperature > threshold;
        boolean isAlert = leak || temperatureExceeded;

        String severity = leak ? "CRITICAL" : (temperatureExceeded ? "ALERT" : "INFO");
        String reason = leak ? "leak=true" : (temperatureExceeded ? "temperature-threshold" : "normal");

        LOGGER.info(
            "alert.evaluate device={} temp={} threshold={} leak={} anomaly={} anomalyScore={} zone={}",
            request.deviceId(),
            temperature,
            threshold,
            leak,
            anomaly,
            anomalyScore,
            request.krakowZone()
        );

        if (isAlert) {
            AlertEvent event = new AlertEvent();
            event.setTimestamp(System.currentTimeMillis());
            event.setDeviceId(request.deviceId() != null ? request.deviceId() : "unknown");
            event.setSeverity(severity);
            event.setReason(reason);
            event.setTemperature(temperature);
            event.setThreshold(threshold);
            event.setLeak(leak);
            event.setAnomaly(anomaly);
            event.setAnomalyScore(anomalyScore);
            event.setKrakowZone(request.krakowZone() != null ? request.krakowZone() : "UNKNOWN");
            event.setActive(true);
            alertRepository.save(event);
            LOGGER.warn("alert.fired device={} severity={} reason={} temp={} zone={}", event.getDeviceId(), severity, reason, temperature, event.getKrakowZone());
        }

        return new AlertResponse(
            isAlert,
            severity,
            reason,
            request.deviceId() != null ? request.deviceId() : "unknown",
            temperature,
            threshold,
            leak,
            anomaly,
            anomalyScore,
            request.krakowZone() != null ? request.krakowZone() : "UNKNOWN"
        );
    }

    public List<AlertEvent> recentAlerts() {
        return alertRepository.findTop100ByOrderByTimestampDesc();
    }

    public List<AlertEvent> activeAlerts() {
        return alertRepository.findTop100ByActiveTrueOrderByTimestampDesc();
    }
}
