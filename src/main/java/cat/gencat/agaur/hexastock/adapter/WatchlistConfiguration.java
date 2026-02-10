package cat.gencat.agaur.hexastock.adapter;

import cat.gencat.agaur.hexastock.application.port.in.MarketMonitoringUseCase;
import cat.gencat.agaur.hexastock.application.port.in.WatchlistManagementUseCase;
import cat.gencat.agaur.hexastock.application.port.out.StockPriceProviderPort;
import cat.gencat.agaur.hexastock.application.port.out.WatchlistPort;
import cat.gencat.agaur.hexastock.application.service.MarketMonitoringService;
import cat.gencat.agaur.hexastock.application.service.WatchlistManagementService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Spring configuration for Watchlist feature.
 *
 * Wires together:
 * - Application services
 * - Ports and adapters
 */
//Hugo i Pol
@Configuration
@EnableScheduling  // Enable @Scheduled annotations
public class WatchlistConfiguration {

    @Bean
    public WatchlistManagementUseCase watchlistManagementUseCase(
            WatchlistPort watchlistPort
    ) {
        return new WatchlistManagementService(watchlistPort);
    }

    @Bean
    public MarketMonitoringUseCase marketMonitoringUseCase(
            WatchlistPort watchlistPort,
            StockPriceProviderPort stockPriceProvider
    ) {
        return new MarketMonitoringService(watchlistPort, stockPriceProvider);
    }
}