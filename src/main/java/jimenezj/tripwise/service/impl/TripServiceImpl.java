package jimenezj.tripwise.service.impl;

import jimenezj.tripwise.dto.trip.TripRequest;
import jimenezj.tripwise.dto.trip.TripResponse;
import jimenezj.tripwise.exception.BadRequestException;
import jimenezj.tripwise.exception.ResourceNotFoundException;
import jimenezj.tripwise.exception.UnauthorizedException;
import jimenezj.tripwise.model.Trip;
import jimenezj.tripwise.model.User;
import jimenezj.tripwise.repository.TripRepository;
import jimenezj.tripwise.security.impl.UserDetailsServiceImpl;
import jimenezj.tripwise.service.TripService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    // Injecting dependencies
    public TripServiceImpl(TripRepository tripRepository, UserDetailsServiceImpl userDetailsServiceImpl) {
        this.tripRepository = tripRepository;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    // Get all trips by user id
    @Override
    public List<TripResponse> getAllTrips() {
        Long userId = userDetailsServiceImpl.getAuthenticatedUser().getId(); // Get authenticated user Id
        List<Trip> trips = tripRepository.findAllByUserId(userId); // get all trips by userId

        if (trips.isEmpty()) {
            throw new ResourceNotFoundException("No trips were found for the user with ID:: " + userId);
        }

        // Return trips list
        return trips.stream()
                .map(trip -> new TripResponse(trip.getId(), trip.getName(), trip.getDestination(), trip.getStartDate(),
                        trip.getEndDate()))
                .collect(Collectors.toList());
    }

    // Create a new trip
    @Override
    public TripResponse createTrip(TripRequest request) {
        User user = (User) userDetailsServiceImpl.getAuthenticatedUser().getUser(); // Get authenticated user

        if (request.startDate().isAfter(request.endDate())) {
            throw new BadRequestException("The start date cannot be after the end date");
        }

        // Build new trip
        Trip buildTrip = new Trip(
                request.name(),
                request.destiny(),
                request.startDate(),
                request.endDate(),
                user);

        Trip trip = tripRepository.save(buildTrip);

        // Return trip response
        return new TripResponse(trip.getId(), trip.getName(), trip.getDestination(), trip.getStartDate(),
                trip.getEndDate());
    }

    // Geet trip by id
    @Override
    public TripResponse getTripById(Long id) {
        Long userId = userDetailsServiceImpl.getAuthenticatedUser().getId(); // Get authenticated user id

        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with ID:: " + id));

        if (!trip.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You do not have access to this trip.");
        }

        return new TripResponse(trip.getId(), trip.getName(), trip.getDestination(), trip.getStartDate(),
                trip.getEndDate());
    }

    // Update trip
    @Override
    public TripResponse updateTrip(Long id, TripRequest request) {
        long userId = userDetailsServiceImpl.getAuthenticatedUser().getId(); // Get authenticated user id

        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));

        if (!trip.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You have no permission to edit this trip");
        }

        // Build trip with new data
        trip.setName(request.name());
        trip.setDestination(request.destiny());
        trip.setStartDate(request.startDate());
        trip.setEndDate(request.endDate());

        Trip updatedTrip = tripRepository.save(trip);

        return new TripResponse(
                updatedTrip.getId(),
                updatedTrip.getName(),
                updatedTrip.getDestination(),
                updatedTrip.getStartDate(),
                updatedTrip.getEndDate());
    }

    // Delete trip by id
    @Override
    public void deleteTrip(Long id) {
        long userId = userDetailsServiceImpl.getAuthenticatedUser().getId(); // Get authenticated user id
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));

        if (!trip.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You have no permission to delete this trip");
        }
        tripRepository.deleteById(id);
    }
}
