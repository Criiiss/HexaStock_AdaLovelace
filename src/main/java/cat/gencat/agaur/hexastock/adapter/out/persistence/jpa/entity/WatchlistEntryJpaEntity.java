package cat.gencat.agaur.hexastock.adapter.out.persistence.jpa.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

//Hugo i Pol
@Entity
@Table(name = "watchlist_entries")
public class WatchlistEntryJpaEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String ticker;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal thresholdPrice;

    @Column(nullable = false, length = 3)
    private String currency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "watchlist_id", nullable = false)
    private WatchlistJpaEntity watchlist;

    // Constructors
    public WatchlistEntryJpaEntity() {
    }

    public WatchlistEntryJpaEntity(
            String id,
            String ticker,
            BigDecimal thresholdPrice,
            String currency
    ) {
        this.id = id;
        this.ticker = ticker;
        this.thresholdPrice = thresholdPrice;
        this.currency = currency;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public BigDecimal getThresholdPrice() {
        return thresholdPrice;
    }

    public void setThresholdPrice(BigDecimal thresholdPrice) {
        this.thresholdPrice = thresholdPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public WatchlistJpaEntity getWatchlist() {
        return watchlist;
    }

    public void setWatchlist(WatchlistJpaEntity watchlist) {
        this.watchlist = watchlist;
    }
}