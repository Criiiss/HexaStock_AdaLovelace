package cat.gencat.agaur.hexastock.application.port.in;

import cat.gencat.agaur.hexastock.model.Money;
import cat.gencat.agaur.hexastock.model.Ticker;
import cat.gencat.agaur.hexastock.model.Watchlist;

import java.util.List;

/**
 * Input port for watchlist management operations.
 * Defines the contract between driving adapters and application layer.
 */
//Hugo i Pol
public interface WatchlistManagementUseCase {

    /**
     * Creates a new watchlist for a user.
     */
    Watchlist createWatchlist(String ownerName, String watchlistName);

    /**
     * Adds an entry (ticker + threshold) to an existing watchlist.
     */
    Watchlist addEntryToWatchlist(
            String watchlistId,
            Ticker ticker,
            Money thresholdPrice
    );

    /**
     * Removes an entry from a watchlist.
     */
    Watchlist removeEntryFromWatchlist(String watchlistId, String entryId);

    /**
     * Retrieves a watchlist by ID.
     */
    Watchlist getWatchlist(String watchlistId);

    /**
     * Retrieves all watchlists for a specific owner.
     */
    List<Watchlist> getWatchlistsByOwner(String ownerName);

    /**
     * Deletes a watchlist.
     */
    void deleteWatchlist(String watchlistId);
}