package cat.gencat.agaur.hexastock.model;

import java.util.*;

/**
 * Watchlist Aggregate Root.
 * A watchlist belongs to a user and contains multiple entries (ticker + threshold price).
 *
 * Business Rules:
 * - A watchlist must have a name and owner
 * - A ticker can appear multiple times in different watchlists
 * - Entries are managed through the aggregate root
 */

//Hugo i Pol
public class Watchlist {
    private final String id;
    private final String ownerName;
    private final String name;
    private final List<WatchlistEntry> entries;

    // Constructor for creating new watchlist
    public Watchlist(String ownerName, String name) {
        this.id = UUID.randomUUID().toString();
        this.ownerName = validateOwnerName(ownerName);
        this.name = validateName(name);
        this.entries = new ArrayList<>();
    }

    // Constructor for reconstitution from persistence
    public Watchlist(String id, String ownerName, String name, List<WatchlistEntry> entries) {
        this.id = id;
        this.ownerName = validateOwnerName(ownerName);
        this.name = validateName(name);
        this.entries = new ArrayList<>(entries);
    }

    private String validateOwnerName(String ownerName) {
        if (ownerName == null || ownerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Owner name cannot be empty");
        }
        return ownerName;
    }

    private String validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Watchlist name cannot be empty");
        }
        return name;
    }

    /**
     * Adds a new entry to the watchlist.
     * Business rule: allows same ticker multiple times (different strategies).
     */
    public void addEntry(Ticker ticker, Money thresholdPrice) {
        WatchlistEntry entry = new WatchlistEntry(ticker, thresholdPrice);
        entries.add(entry);
    }

    /**
     * Removes an entry by its ID.
     */
    public void removeEntry(String entryId) {
        entries.removeIf(entry -> entry.getId().equals(entryId));
    }

    /**
     * Evaluates all entries against current market prices.
     * Returns list of triggered alerts.
     */
    public List<AlertTrigger> evaluateAlerts(Map<Ticker, Money> currentPrices) {
        List<AlertTrigger> triggers = new ArrayList<>();

        for (WatchlistEntry entry : entries) {
            Money currentPrice = currentPrices.get(entry.getTicker());
            if (currentPrice != null && entry.shouldTriggerAlert(currentPrice)) {
                triggers.add(new AlertTrigger(
                        ownerName,
                        name,
                        entry.getTicker(),
                        entry.getThresholdPrice(),
                        currentPrice
                ));
            }
        }

        return triggers;
    }

    /**
     * Returns all unique tickers in this watchlist.
     */
    public Set<Ticker> getAllTickers() {
        Set<Ticker> tickers = new HashSet<>();
        for (WatchlistEntry entry : entries) {
            tickers.add(entry.getTicker());
        }
        return tickers;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getName() {
        return name;
    }

    public List<WatchlistEntry> getEntries() {
        return Collections.unmodifiableList(entries);
    }
}