package cat.gencat.agaur.hexastock.adapter.in.webmodel;

/**
 * DTO for creating a new watchlist.
 */
//Hugo i Pol
public record CreateWatchlistDTO(
        String ownerName,
        String name
) {
}