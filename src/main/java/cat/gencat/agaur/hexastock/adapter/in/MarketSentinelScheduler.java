package cat.gencat.agaur.hexastock.adapter.in;

import cat.gencat.agaur.hexastock.application.port.in.MarketMonitoringUseCase;
import cat.gencat.agaur.hexastock.model.AlertTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Scheduled daemon that monitors the market for watchlist alerts.
 *
 * This is a DRIVING ADAPTER (input adapter):
 * - It initiates application flow periodically
 * - It interacts with the core through an Input Port
 * - It contains NO business logic
 *
 * The interval is configured in application.properties.
 */
//Hugo i Pol
@Component
public class MarketSentinelScheduler {

    private static final Logger logger = LoggerFactory.getLogger(MarketSentinelScheduler.class);

    private final MarketMonitoringUseCase marketMonitoringUseCase;

    public MarketSentinelScheduler(MarketMonitoringUseCase marketMonitoringUseCase) {
        this.marketMonitoringUseCase = marketMonitoringUseCase;
    }

    /**
     * Scheduled task that runs at the configured interval.
     *
     * The cron expression is read from application.properties:
     * watchlist.monitoring.cron
     *
     * Example: "0 */
    @Scheduled(cron = "${watchlist.monitoring.cron}")
    public void monitorMarket() {
        logger.info("Market Sentinel: Starting watchlist scan...");

        try {
            // Delegate to use case (NO business logic here)
            List<AlertTrigger> triggers = marketMonitoringUseCase.scanAndEvaluateAlerts();

            // Log triggered alerts (simulating notifications)
            if (triggers.isEmpty()) {
                logger.info("Market Sentinel: No alerts triggered");
            } else {
                logger.info("Market Sentinel: {} alerts triggered", triggers.size());
                for (AlertTrigger trigger : triggers) {
                    // This is the required log format from the exercise
                    System.out.println(trigger.formatMessage());
                }
            }
        } catch (Exception e) {
            logger.error("Market Sentinel: Error during market scan", e);
        }
    }
}