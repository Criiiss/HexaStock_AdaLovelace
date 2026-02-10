package cat.gencat.agaur.hexastock.adapter.in.webmodel;

import java.math.BigDecimal;

/**
 * DTO for adding an entry to a watchlist.
 */
public record AddWatchlistEntryDTO(
        String ticker,
        BigDecimal thresholdPrice,
        String currency
) {
}