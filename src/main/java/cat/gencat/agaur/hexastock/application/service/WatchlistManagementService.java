package cat.gencat.agaur.hexastock.application.service;

import cat.gencat.agaur.hexastock.application.port.in.WatchlistManagementUseCase;
import cat.gencat.agaur.hexastock.application.port.out.WatchlistPort;
import cat.gencat.agaur.hexastock.model.Money;
import cat.gencat.agaur.hexastock.model.Ticker;
import cat.gencat.agaur.hexastock.model.Watchlist;
import cat.gencat.agaur.hexastock.model.exception.WatchlistNotFoundException;

import java.util.List;

/**
 * Application Service (Orchestrator) for watchlist management.
 *
 * IMPORTANT: This service contains NO business logic.
 * It only orchestrates the flow:
 * 1. Retrieves domain objects from repositories
 * 2. Delegates business operations to rich domain entities
 * 3. Persists the results
 */
//Hugo i Pol
public class WatchlistManagementService implements WatchlistManagementUseCase {

    private final WatchlistPort watchlistPort;

    public WatchlistManagementService(WatchlistPort watchlistPort) {
        this.watchlistPort = watchlistPort;
    }

    @Override
    public Watchlist createWatchlist(String ownerName, String watchlistName) {
        // Domain object creates itself with business rules validation
        Watchlist watchlist = new Watchlist(ownerName, watchlistName);

        // Persist and return
        return watchlistPort.save(watchlist);
    }

    @Override
    public Watchlist addEntryToWatchlist(
            String watchlistId,
            Ticker ticker,
            Money thresholdPrice
    ) {
        // Retrieve domain object
        Watchlist watchlist = watchlistPort.findById(watchlistId)
                .orElseThrow(() -> new WatchlistNotFoundException(watchlistId));

        // Delegate business operation to domain entity
        watchlist.addEntry(ticker, thresholdPrice);

        // Persist and return
        return watchlistPort.save(watchlist);
    }

    @Override
    public Watchlist removeEntryFromWatchlist(String watchlistId, String entryId) {
        Watchlist watchlist = watchlistPort.findById(watchlistId)
                .orElseThrow(() -> new WatchlistNotFoundException(watchlistId));

        watchlist.removeEntry(entryId);

        return watchlistPort.save(watchlist);
    }

    @Override
    public Watchlist getWatchlist(String watchlistId) {
        return watchlistPort.findById(watchlistId)
                .orElseThrow(() -> new WatchlistNotFoundException(watchlistId));
    }

    @Override
    public List<Watchlist> getWatchlistsByOwner(String ownerName) {
        return watchlistPort.findByOwner(ownerName);
    }

    @Override
    public void deleteWatchlist(String watchlistId) {
        if (!watchlistPort.findById(watchlistId).isPresent()) {
            throw new WatchlistNotFoundException(watchlistId);
        }
        watchlistPort.deleteById(watchlistId);
    }
}