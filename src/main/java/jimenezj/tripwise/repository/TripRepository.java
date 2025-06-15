package jimenezj.tripwise.repository;

import jimenezj.tripwise.dto.trip.TripResponse;
import jimenezj.tripwise.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findAllByUserId(Long userId);
}
