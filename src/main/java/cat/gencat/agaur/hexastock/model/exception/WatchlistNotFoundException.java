package cat.gencat.agaur.hexastock.model.exception;

//Hugo i Pol
public class WatchlistNotFoundException extends DomainException {
    public WatchlistNotFoundException(String watchlistId) {
        super("Watchlist not found: " + watchlistId);
    }
}