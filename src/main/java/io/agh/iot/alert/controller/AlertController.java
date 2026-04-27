package io.agh.iot.alert.controller;

import io.agh.iot.alert.model.AlertEvent;
import io.agh.iot.alert.service.AlertService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @PostMapping("/evaluate")
    public AlertResponse evaluate(@RequestBody AlertRequest request) {
        return alertService.evaluate(request);
    }

    @GetMapping("/alerts/recent")
    public List<AlertEvent> recentAlerts() {
        return alertService.recentAlerts();
    }

    @GetMapping("/alerts/active")
    public List<AlertEvent> activeAlerts() {
        return alertService.activeAlerts();
    }

    public record AlertRequest(
        String deviceId,
        Double temperature,
        Double threshold,
        Boolean leak,
        Boolean anomaly,
        Double anomalyScore,
        String krakowZone
    ) {
    }

    public record AlertResponse(
        boolean alert,
        String severity,
        String reason,
        String deviceId,
        double temperature,
        double threshold,
        boolean leak,
        boolean anomaly,
        double anomalyScore,
        String krakowZone
    ) {
    }
}
