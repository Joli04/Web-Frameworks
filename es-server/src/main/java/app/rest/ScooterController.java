package app.rest;

import app.Exceptions.PreConditionFailedException;
import app.Exceptions.ResourceNotFoundException;
import app.Views.IView;
import app.models.Scooter;
import app.models.Trip;
import app.repositories.EntityRepository;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/scooters")
public class ScooterController {

    @Autowired // injects an implementation of ScooterRepository here.
    private EntityRepository<Scooter> scooterRepository;

    @Autowired // injects an implementation of TripRepository here.
    private EntityRepository<Trip> tripRepository;

    @GetMapping("/test")
    public List<Scooter> getTestScooters() {
        return
                List.of(
                        new Scooter("Test-scooter-A"),
                        new Scooter("Test-scooter-B")
                );

    }

    @GetMapping("")
    public List<Scooter> getAllScooters() {
        return scooterRepository.findAll();
    }

    //test
    @GetMapping("/trips")
    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    @GetMapping("{id}")
    public Scooter getScooterById(@PathVariable long id) throws Exception {
        Scooter scooter = scooterRepository.findById(id);

        if (scooter != null)
            return scooterRepository.findById(id);
        else {
            throw new ResourceNotFoundException(id);
        }
    }

    @GetMapping("/qStatus")
    public List<Scooter> getScooterByStatus(@RequestParam("status") String status) throws Exception {

        List<Scooter> statusScooters = scooterRepository.findByQuery("Scooter_find_by_status", status);
        if (statusScooters != null && Objects.equals(status, "IDLE")) {
            return statusScooters;
        } else {
            throw new ResourceNotFoundException(status);
        }
    }
    //todo misschien werkt dit wel, zou je ff moeten testen.
//    public ResponseEntity<List<Scooter>> getScooterByStatus(@RequestParam("status") String status) {
//
//        List<Scooter> statusScooters = scooterRepository.findByQuery("Scooter_find_by_status", status);
//        if (statusScooters != null && Objects.equals(status, "IDLE")) {
//            return ResponseEntity.ok(statusScooters);
//        } else {
//            return ResponseEntity.badRequest().build().;
//        }

    @GetMapping("/qBattery")
    public List<Scooter> getScooterByBattery(@RequestParam("battery") int batteryCharge) {

        List<Scooter> batteryScooters = scooterRepository.findByQuery("Scooter_find_by_battery", batteryCharge);
        if (batteryScooters != null) {
            return batteryScooters;
        }
        return null;
    }

    @PostMapping("")
    public ResponseEntity<Object> createScooter(@RequestBody Scooter scooter) {
        Scooter savedScooter = scooterRepository.save(scooter);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedScooter.getId()).toUri();

        return ResponseEntity
                .created(location)
                .header(
                        "Nieuwe scooter aangemaakt!",
                        "Je hebt een scooter aangemaakt en er is niks fout gegaan."
                )
                .body(scooterRepository.findById(savedScooter.getId()));
    }


    // todo localDateTimeFormat check trip.java
    @Transactional
    @PostMapping("{scooterId}/trip")
    public void addTripToScooter(@RequestBody Trip trip, @PathVariable long scooterId) throws Exception {
        Scooter scooterDetail = scooterRepository.findById(scooterId);

        if (scooterDetail == null) {
            throw new ResourceNotFoundException(scooterId);
        }

        if (!scooterDetail.getStatus().equals("IDLE") || scooterDetail.getBatteryCharge() < 10) {
            throw new PreConditionFailedException(scooterId, scooterDetail.getId());
        } else {
            Trip savedTrip = tripRepository.save(trip);
            savedTrip.associateScooter(scooterDetail);
            if (savedTrip.getStartDateTime() == null) {
                savedTrip.setStartDateTime(LocalDateTime.now());
            }
            scooterDetail.setGpsLocation(scooterDetail.getGpsLocation());
            scooterDetail.setStatus("IN_USE");
        }
    }

    @PutMapping("{id}")
    public void updateScooter(@PathVariable long id, @RequestBody Scooter scooterDetails) throws Exception {
        Scooter updateScooter = scooterRepository.findById(id);
        if (updateScooter == null) {
            throw new ResourceNotFoundException(id);
        } else if (id != scooterDetails.getId() && scooterDetails.getId() != 0) {
            throw new PreConditionFailedException(id, scooterDetails.getId());
        } else {
            updateScooter.setTag(scooterDetails.getTag());
            updateScooter.setBatteryCharge(scooterDetails.getBatteryCharge());
            updateScooter.setStatus(scooterDetails.getStatus());
            updateScooter.setGpsLocation(scooterDetails.getGpsLocation());
//            updateScooter.setMileage(scooterDetails.getMileage());
            scooterRepository.save(updateScooter);
        }
    }

    @DeleteMapping("{id}")
    public void deleteScooter(@PathVariable long id) throws Exception {
        Scooter scooterId = scooterRepository.findById(id);
        if (scooterId == null) {
            throw new ResourceNotFoundException(id);
        } else {
            scooterRepository.deleteById(id);
        }
    }

    @JsonView(value = {IView.SummaryView.class})
    @GetMapping("/summary")
    public List<Scooter> getScootersSummary() {
        return scooterRepository.findAll();
    }
}

