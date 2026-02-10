package cat.gencat.agaur.hexastock.model;

/**
 * Value Object representing an alert that has been triggered.
 * Contains all information needed for notification.
 */

//Hugo i Pol
public record AlertTrigger(
        String ownerName,
        String watchlistName,
        Ticker ticker,
        Money thresholdPrice,
        Money currentPrice
) {
    public String formatMessage() {
        return String.format(
                "[BUY SIGNAL] User: %s | List: %s | Ticker: %s | Target price: %s | Current price: %s",
                ownerName,
                watchlistName,
                ticker.value(),
                thresholdPrice,
                currentPrice
        );
    }
}