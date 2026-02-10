package cat.gencat.agaur.hexastock.adapter.out.persistence.jpa.repository;

import cat.gencat.agaur.hexastock.adapter.out.persistence.jpa.entity.WatchlistJpaEntity;
import cat.gencat.agaur.hexastock.adapter.out.persistence.jpa.mapper.WatchlistMapper;
import cat.gencat.agaur.hexastock.adapter.out.persistence.jpa.springdatarepository.JpaWatchlistSpringDataRepository;
import cat.gencat.agaur.hexastock.application.port.out.WatchlistPort;
import cat.gencat.agaur.hexastock.model.Watchlist;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * JPA implementation of WatchlistPort.
 *
 * This adapter:
 * 1. Implements the output port interface
 * 2. Translates between domain and persistence models using mappers
 * 3. Delegates to Spring Data repository
 *
 * IMPORTANT: Contains NO business logic.
 */
//Hugo i Pol
@Repository
public class JpaWatchlistRepository implements WatchlistPort {

    private final JpaWatchlistSpringDataRepository springDataRepository;

    public JpaWatchlistRepository(JpaWatchlistSpringDataRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public Watchlist save(Watchlist watchlist) {
        WatchlistJpaEntity entity = WatchlistMapper.toJpaEntity(watchlist);
        WatchlistJpaEntity saved = springDataRepository.save(entity);
        return WatchlistMapper.toDomain(saved);
    }

    @Override
    public Optional<Watchlist> findById(String watchlistId) {
        return springDataRepository.findById(watchlistId)
                .map(WatchlistMapper::toDomain);
    }

    @Override
    public List<Watchlist> findByOwner(String ownerName) {
        return springDataRepository.findByOwnerName(ownerName)
                .stream()
                .map(WatchlistMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Watchlist> findAll() {
        return springDataRepository.findAll()
                .stream()
                .map(WatchlistMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String watchlistId) {
        springDataRepository.deleteById(watchlistId);
    }
}