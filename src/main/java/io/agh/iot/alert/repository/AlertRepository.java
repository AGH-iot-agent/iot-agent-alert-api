package io.agh.iot.alert.repository;

import io.agh.iot.alert.model.AlertEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRepository extends JpaRepository<AlertEvent, Long> {
    List<AlertEvent> findTop100ByOrderByTimestampDesc();

    List<AlertEvent> findTop100ByActiveTrueOrderByTimestampDesc();
}
