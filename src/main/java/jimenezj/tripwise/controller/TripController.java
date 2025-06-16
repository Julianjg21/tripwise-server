package jimenezj.tripwise.controller;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jimenezj.tripwise.dto.trip.TripRequest;
import jimenezj.tripwise.dto.trip.TripResponse;
import jimenezj.tripwise.service.TripService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/trips")
public class TripController {
    
    //Injecting trip service
    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    //Get all trips by user id
    @GetMapping
    public ResponseEntity<List<TripResponse>> getAllTrips() {
        List<TripResponse> tripsResponse = tripService.getAllTrips();
        return ResponseEntity.ok(tripsResponse);
    }
    
    //Create trip for a user
    @PostMapping
    public ResponseEntity<TripResponse> createTrip(@RequestBody @Valid TripRequest request) {
        TripResponse tripResponse = tripService.createTrip(request);
        return ResponseEntity.ok(tripResponse);
    }
    
    //Get trip by trip id
    @GetMapping("/{id}")
    public ResponseEntity<TripResponse> getTripById(@PathVariable @Min(1) Long id) {
        TripResponse tripResponse = tripService.getTripById(id);
        return ResponseEntity.ok(tripResponse);
    }
    
    //Update trip by trip id
    @PutMapping("/{id}")
    public ResponseEntity<TripResponse> updateTrip(@PathVariable  @Min(1) Long id, @RequestBody @Valid TripRequest request) {
        TripResponse tripResponse = tripService.updateTrip(id, request);
        return ResponseEntity.ok(tripResponse);
    }

    //Delete trip by trip id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrip(@PathVariable @Min(1) Long id) {
        tripService.deleteTrip(id);
        return ResponseEntity.noContent().build();
    }
}
