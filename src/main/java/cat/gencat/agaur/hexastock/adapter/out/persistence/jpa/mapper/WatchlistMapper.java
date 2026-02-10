package cat.gencat.agaur.hexastock.adapter.out.persistence.jpa.mapper;

import cat.gencat.agaur.hexastock.adapter.out.persistence.jpa.entity.WatchlistEntryJpaEntity;
import cat.gencat.agaur.hexastock.adapter.out.persistence.jpa.entity.WatchlistJpaEntity;
import cat.gencat.agaur.hexastock.model.*;
import java.util.Currency;
import java.util.ArrayList;
import java.util.List;

/**
 * Mapper between Domain Watchlist and JPA Watchlist Entity.
 *
 * CRITICAL: This mapper ensures complete separation between domain and persistence.
 * - Domain objects have NO JPA annotations
 * - JPA entities exist only in the adapter layer
 */
//Hugo i Pol
public class WatchlistMapper {

    /**
     * Converts domain Watchlist to JPA entity.
     */
    public static WatchlistJpaEntity toJpaEntity(Watchlist watchlist) {
        WatchlistJpaEntity entity = new WatchlistJpaEntity(
                watchlist.getId(),
                watchlist.getOwnerName(),
                watchlist.getName()
        );


        // Map entries
        for (WatchlistEntry entry : watchlist.getEntries()) {
            WatchlistEntryJpaEntity entryEntity = new WatchlistEntryJpaEntity(
                    entry.getId(),
                    entry.getTicker().value(),
                    entry.getThresholdPrice().amount(),
                    entry.getThresholdPrice().currency().getCurrencyCode()  // Currency → String
            );
            entity.addEntry(entryEntity);
        }

        return entity;
    }

    /**
     * Converts JPA entity to domain Watchlist.
     */
    public static Watchlist toDomain(WatchlistJpaEntity entity) {
        List<WatchlistEntry> entries = new ArrayList<>();

        for (WatchlistEntryJpaEntity entryEntity : entity.getEntries()) {
            WatchlistEntry entry = new WatchlistEntry(
                    entryEntity.getId(),
                    Ticker.of(entryEntity.getTicker()),
                    new Money(
                            Currency.getInstance(entryEntity.getCurrency()),  // String → Currency (primero)
                            entryEntity.getThresholdPrice()                   // BigDecimal (segundo)
                    )
            );
            entries.add(entry);
        }

        return new Watchlist(
                entity.getId(),
                entity.getOwnerName(),
                entity.getName(),
                entries
        );
    }
}