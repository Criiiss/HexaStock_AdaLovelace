package cat.gencat.agaur.hexastock.model;

import cat.gencat.agaur.hexastock.model.exception.InvalidAmountException;

import java.util.UUID;

/**
 * Entity representing a single entry in a watchlist.
 * An entry consists of a ticker and a threshold price.
 */
//Hugo i Pol
public class WatchlistEntry {
    private final String id;
    private final Ticker ticker;
    private final Money thresholdPrice;

    public WatchlistEntry(Ticker ticker, Money thresholdPrice) {
        this.id = UUID.randomUUID().toString();
        this.ticker = ticker;
        this.thresholdPrice = validateThresholdPrice(thresholdPrice);
    }

    // Constructor for reconstitution from persistence
    public WatchlistEntry(String id, Ticker ticker, Money thresholdPrice) {
        this.id = id;
        this.ticker = ticker;
        this.thresholdPrice = validateThresholdPrice(thresholdPrice);
    }

    private Money validateThresholdPrice(Money price) {
        if (price.amount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Threshold price must be positive");
        }
        return price;
    }

    /**
     * Evaluates if current price triggers an alert for this entry.
     * Alert is triggered when current price is <= threshold price.
     */
    public boolean shouldTriggerAlert(Money currentPrice) {
        return currentPrice.amount().compareTo(thresholdPrice.amount()) <= 0;
    }

    // Getters
    public String getId() {
        return id;
    }

    public Ticker getTicker() {
        return ticker;
    }

    public Money getThresholdPrice() {
        return thresholdPrice;
    }
}