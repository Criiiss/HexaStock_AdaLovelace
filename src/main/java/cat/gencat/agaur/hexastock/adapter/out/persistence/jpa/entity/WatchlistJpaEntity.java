package cat.gencat.agaur.hexastock.adapter.out.persistence.jpa.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

//Hugo i Pol
@Entity
@Table(name = "watchlists")
public class WatchlistJpaEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String ownerName;

    @Column(nullable = false)
    private String name;

    @OneToMany(
            mappedBy = "watchlist",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<WatchlistEntryJpaEntity> entries = new ArrayList<>();

    // Constructors
    public WatchlistJpaEntity() {
    }

    public WatchlistJpaEntity(String id, String ownerName, String name) {
        this.id = id;
        this.ownerName = ownerName;
        this.name = name;
    }

    // Helper methods for bidirectional relationship
    public void addEntry(WatchlistEntryJpaEntity entry) {
        entries.add(entry);
        entry.setWatchlist(this);
    }

    public void removeEntry(WatchlistEntryJpaEntity entry) {
        entries.remove(entry);
        entry.setWatchlist(null);
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<WatchlistEntryJpaEntity> getEntries() {
        return entries;
    }

    public void setEntries(List<WatchlistEntryJpaEntity> entries) {
        this.entries = entries;
    }
}