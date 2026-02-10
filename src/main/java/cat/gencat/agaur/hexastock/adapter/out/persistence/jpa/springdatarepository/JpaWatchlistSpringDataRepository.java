package cat.gencat.agaur.hexastock.adapter.out.persistence.jpa.springdatarepository;

import cat.gencat.agaur.hexastock.adapter.out.persistence.jpa.entity.WatchlistJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaWatchlistSpringDataRepository extends JpaRepository<WatchlistJpaEntity, String> {

    List<WatchlistJpaEntity> findByOwnerName(String ownerName);
}