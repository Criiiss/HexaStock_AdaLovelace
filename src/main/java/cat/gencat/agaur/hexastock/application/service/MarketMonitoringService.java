package cat.gencat.agaur.hexastock.application.service;

import cat.gencat.agaur.hexastock.application.port.in.MarketMonitoringUseCase;
import cat.gencat.agaur.hexastock.application.port.out.StockPriceProviderPort;
import cat.gencat.agaur.hexastock.application.port.out.WatchlistPort;
import cat.gencat.agaur.hexastock.model.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * Application Service (Orchestrator) for market monitoring.
 *
 * IMPORTANT: This service orchestrates but contains NO business logic.
 * Business logic for alert evaluation is in the Watchlist domain entity.
 */
//Hugo i Pol
public class MarketMonitoringService implements MarketMonitoringUseCase {

    private final WatchlistPort watchlistPort;
    private final StockPriceProviderPort stockPriceProvider;

    public MarketMonitoringService(
            WatchlistPort watchlistPort,
            StockPriceProviderPort stockPriceProvider
    ) {
        this.watchlistPort = watchlistPort;
        this.stockPriceProvider = stockPriceProvider;
    }

    @Override
    public List<AlertTrigger> scanAndEvaluateAlerts() {
        // 1. Retrieve all active watchlists
        List<Watchlist> allWatchlists = watchlistPort.findAll();

        if (allWatchlists.isEmpty()) {
            return Collections.emptyList();
        }

        // 2. Collect all unique tickers across all watchlists
        Set<Ticker> allTickers = new HashSet<>();
        for (Watchlist watchlist : allWatchlists) {
            allTickers.addAll(watchlist.getAllTickers());
        }

        // 3. Fetch current prices for all tickers (batch operation)
        Map<Ticker, Money> currentPrices = fetchCurrentPrices(allTickers);

        // 4. Evaluate alerts for each watchlist (DOMAIN LOGIC)
        List<AlertTrigger> allTriggers = new ArrayList<>();
        for (Watchlist watchlist : allWatchlists) {
            // Delegate to domain entity - this is where business logic lives
            List<AlertTrigger> triggers = watchlist.evaluateAlerts(currentPrices);
            allTriggers.addAll(triggers);
        }

        return allTriggers;
    }

    private Map<Ticker, Money> fetchCurrentPrices(Set<Ticker> tickers) {
        Map<Ticker, Money> prices = new HashMap<>();

        for (Ticker ticker : tickers) {
            try {
                // CORRECCIÓN 1: Cambiar getStockPrice por fetchStockPrice
                StockPrice stockPrice = stockPriceProvider.fetchStockPrice(ticker);

                // CORRECCIÓN 2: Crear Money correctamente desde StockPrice
                Money price = new Money(
                        Currency.getInstance(stockPrice.currency()),  // String → Currency
                        BigDecimal.valueOf(stockPrice.price())        // double → BigDecimal
                );
                prices.put(ticker, price);
            } catch (Exception e) {
                System.err.println("Failed to fetch price for " + ticker.value() + ": " + e.getMessage());
            }
        }

        return prices;
    }
}