package cat.gencat.agaur.hexastock.adapter.in;

import cat.gencat.agaur.hexastock.adapter.in.webmodel.*;
import cat.gencat.agaur.hexastock.application.port.in.WatchlistManagementUseCase;
import cat.gencat.agaur.hexastock.model.Money;
import cat.gencat.agaur.hexastock.model.Ticker;
import cat.gencat.agaur.hexastock.model.Watchlist;
import cat.gencat.agaur.hexastock.model.WatchlistEntry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for Watchlist management.
 *
 * Responsibilities:
 * 1. HTTP request handling
 * 2. Input validation
 * 3. DTO <-> Domain translation
 * 4. Delegation to use case
 *
 * IMPORTANT: Contains NO business logic.
 */
//Hugo i Pol
@RestController
@RequestMapping("/api/watchlists")
public class WatchlistRestController {

    private final WatchlistManagementUseCase watchlistManagementUseCase;

    public WatchlistRestController(WatchlistManagementUseCase watchlistManagementUseCase) {
        this.watchlistManagementUseCase = watchlistManagementUseCase;
    }

    /**
     * POST /api/watchlists
     * Creates a new watchlist.
     */
    @PostMapping
    public ResponseEntity<WatchlistResponseDTO> createWatchlist(
            @RequestBody CreateWatchlistDTO request
    ) {
        Watchlist watchlist = watchlistManagementUseCase.createWatchlist(
                request.ownerName(),
                request.name()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponseDTO(watchlist));
    }

    /**
     * POST /api/watchlists/{watchlistId}/entries
     * Adds an entry to an existing watchlist.
     */
    @PostMapping("/{watchlistId}/entries")
    public ResponseEntity<WatchlistResponseDTO> addEntry(
            @PathVariable String watchlistId,
            @RequestBody AddWatchlistEntryDTO request
    ) {
        Watchlist watchlist = watchlistManagementUseCase.addEntryToWatchlist(
                watchlistId,
                Ticker.of(request.ticker()),
                new Money(
                        Currency.getInstance(request.currency()),  // String → Currency (primero)
                        request.thresholdPrice()                   // BigDecimal (segundo)
                )
        );

        return ResponseEntity.ok(toResponseDTO(watchlist));
    }

    /**
     * DELETE /api/watchlists/{watchlistId}/entries/{entryId}
     * Removes an entry from a watchlist.
     */
    @DeleteMapping("/{watchlistId}/entries/{entryId}")
    public ResponseEntity<WatchlistResponseDTO> removeEntry(
            @PathVariable String watchlistId,
            @PathVariable String entryId
    ) {
        Watchlist watchlist = watchlistManagementUseCase.removeEntryFromWatchlist(
                watchlistId,
                entryId
        );

        return ResponseEntity.ok(toResponseDTO(watchlist));
    }

    /**
     * GET /api/watchlists/{watchlistId}
     * Retrieves a watchlist by ID.
     */
    @GetMapping("/{watchlistId}")
    public ResponseEntity<WatchlistResponseDTO> getWatchlist(
            @PathVariable String watchlistId
    ) {
        Watchlist watchlist = watchlistManagementUseCase.getWatchlist(watchlistId);
        return ResponseEntity.ok(toResponseDTO(watchlist));
    }

    /**
     * GET /api/watchlists?owner={ownerName}
     * Retrieves all watchlists for an owner.
     */
    @GetMapping
    public ResponseEntity<List<WatchlistResponseDTO>> getWatchlistsByOwner(
            @RequestParam String owner
    ) {
        List<Watchlist> watchlists = watchlistManagementUseCase.getWatchlistsByOwner(owner);

        List<WatchlistResponseDTO> response = watchlists.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/watchlists/{watchlistId}
     * Deletes a watchlist.
     */
    @DeleteMapping("/{watchlistId}")
    public ResponseEntity<Void> deleteWatchlist(@PathVariable String watchlistId) {
        watchlistManagementUseCase.deleteWatchlist(watchlistId);
        return ResponseEntity.noContent().build();
    }

    // DTO Mappers
    private WatchlistResponseDTO toResponseDTO(Watchlist watchlist) {
        List<WatchlistEntryResponseDTO> entryDTOs = watchlist.getEntries().stream()
                .map(this::toEntryResponseDTO)
                .collect(Collectors.toList());

        return new WatchlistResponseDTO(
                watchlist.getId(),
                watchlist.getOwnerName(),
                watchlist.getName(),
                entryDTOs
        );
    }

    private WatchlistEntryResponseDTO toEntryResponseDTO(WatchlistEntry entry) {
        return new WatchlistEntryResponseDTO(
                entry.getId(),
                entry.getTicker().value(),
                entry.getThresholdPrice().amount(),
                entry.getThresholdPrice().currency().getCurrencyCode()  // Currency → String
        );
    }
}