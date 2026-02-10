package cat.gencat.agaur.hexastock.adapter.in.webmodel;

import java.util.List;

/**
 * DTO for watchlist response.
 */
//Hugo i Pol
public record WatchlistResponseDTO(
        String id,
        String ownerName,
        String name,
        List<WatchlistEntryResponseDTO> entries
) {
}