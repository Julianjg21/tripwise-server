package jimenezj.tripwise.service;

import jimenezj.tripwise.dto.trip.TripRequest;
import jimenezj.tripwise.dto.trip.TripResponse;
import org.springframework.data.domain.Page;



public interface TripService {
    // Get al trips of a user
    Page<TripResponse> getAllTrips(int page, int size);

    // Create a new trip for a user
    TripResponse createTrip(TripRequest request);

    // Get trip of a user by trip id
    TripResponse getTripById(Long id);

    // Update a trip of the user
    TripResponse updateTrip(Long id, TripRequest request);

    // Delete a trip of the user
    void deleteTrip(Long id);
}
