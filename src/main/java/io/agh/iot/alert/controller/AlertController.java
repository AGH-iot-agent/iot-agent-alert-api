package io.agh.iot.alert.controller;

import lombok.Data;
import org.springframework.web.bind.annotation.*;

@RestController
public class AlertController {

    @PostMapping("/evaluate")
    public AlertResponse evaluate(@RequestBody AlertRequest request) {
        double temperature = request.getTemperature();
        double threshold = request.getThreshold() != null ? request.getThreshold() : 30.0;
        boolean isAlert = temperature > threshold;

        return new AlertResponse(
            isAlert,
            temperature,
            threshold,
            isAlert ? "threshold exceeded" : "normal"
        );
    }

    @Data
    static class AlertRequest {
        private Double temperature;
        private Double threshold;
    }

    record AlertResponse(boolean alert, double temperature, double threshold, String message) {}
}
