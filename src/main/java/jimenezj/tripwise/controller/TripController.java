package jimenezj.tripwise.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jimenezj.tripwise.dto.trip.TripRequest;
import jimenezj.tripwise.dto.trip.TripResponse;
import jimenezj.tripwise.service.TripService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/trips")
public class TripController {

    private final TripService tripService;

    // Injecting dependencies
    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    // Get all trips by user id
    @GetMapping
    public ResponseEntity<Page<TripResponse>> getAllTrips(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<TripResponse> tripResponsePage = tripService.getAllTrips(page, size);
        return ResponseEntity.ok(tripResponsePage);
    }


    // Create trip for a user
    @PostMapping
    public ResponseEntity<TripResponse> createTrip(@RequestBody @Valid TripRequest request) {
        TripResponse tripResponse = tripService.createTrip(request);
        return ResponseEntity.ok(tripResponse);
    }

    // Get trip by trip id
    @GetMapping("/{id}")
    public ResponseEntity<TripResponse> getTripById(@PathVariable @Min(1) Long id) {
        TripResponse tripResponse = tripService.getTripById(id);
        return ResponseEntity.ok(tripResponse);
    }

    // Update trip by trip id
    @PutMapping("/{id}")
    public ResponseEntity<TripResponse> updateTrip(@PathVariable @Min(1) Long id,
            @RequestBody @Valid TripRequest request) {
        TripResponse tripResponse = tripService.updateTrip(id, request);
        return ResponseEntity.ok(tripResponse);
    }

    // Delete trip by trip id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrip(@PathVariable @Min(1) Long id) {
        tripService.deleteTrip(id);
        return ResponseEntity.noContent().build();
    }
}
