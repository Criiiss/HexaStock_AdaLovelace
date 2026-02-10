package cat.gencat.agaur.hexastock.application.port.in;

import cat.gencat.agaur.hexastock.model.AlertTrigger;

import java.util.List;

/**
 * Input port for market monitoring operations.
 * Used by the scheduled daemon to check for alerts.
 */
//Hugo i Pol
public interface MarketMonitoringUseCase {

    /**
     * Scans all active watchlists and evaluates alerts based on current market prices.
     * Returns all triggered alerts.
     */
    List<AlertTrigger> scanAndEvaluateAlerts();
}