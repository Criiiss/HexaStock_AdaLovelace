package cat.gencat.agaur.hexastock.application.port.out;

import cat.gencat.agaur.hexastock.model.Watchlist;

import java.util.List;
import java.util.Optional;

/**
 * Output port for Watchlist persistence.
 * Defines what the domain needs from persistence infrastructure.
 */
//Hugo i Pol
public interface WatchlistPort {

    /**
     * Saves a watchlist (create or update).
     */
    Watchlist save(Watchlist watchlist);

    /**
     * Finds a watchlist by its ID.
     */
    Optional<Watchlist> findById(String watchlistId);

    /**
     * Finds all watchlists for a specific owner.
     */
    List<Watchlist> findByOwner(String ownerName);

    /**
     * Retrieves all active watchlists.
     */
    List<Watchlist> findAll();

    /**
     * Deletes a watchlist by ID.
     */
    void deleteById(String watchlistId);
}