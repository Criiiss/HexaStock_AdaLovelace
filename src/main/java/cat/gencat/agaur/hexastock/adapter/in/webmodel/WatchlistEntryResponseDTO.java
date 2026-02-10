package cat.gencat.agaur.hexastock.adapter.in.webmodel;

import java.math.BigDecimal;

/**
 * DTO for watchlist entry response.
 */
//Hugo i Pol
public record WatchlistEntryResponseDTO(
        String id,
        String ticker,
        BigDecimal thresholdPrice,
        String currency
) {
}