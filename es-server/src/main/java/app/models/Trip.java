package app.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Trip {
    @Id
    @GeneratedValue
    private long id;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime startDateTime;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime endDateTime;

    private String startPosition;

    private String endPosition;

    private double mileage;

    private double cost;

    @OneToMany(mappedBy = "trip", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JsonBackReference
    private Set<Scooter> scooters;

    public Trip(long id, LocalDateTime startDateTime, LocalDateTime endDateTime, String startPosition,
                String endPosition, double mileage, double cost) {
        this.id = id;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.mileage = mileage;
        this.cost = cost;
        scooters = new HashSet<>();
    }

    public Trip(@JsonProperty long id) {
        this.id = id;
        scooters = new HashSet<>();
    }

    public Trip() {
        scooters = new HashSet<>();
    }

    public static Trip createSampleTrip(long id) {
        LocalDateTime[] localDateTime = {LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(6),
                LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(5)};
        double latitude = 52.3702157;
        double longitude = 4.895167899999933;

        Trip trip = new Trip(id);
        trip.mileage = (int) (Math.random() * 95) + 5;
        trip.cost = (int) (Math.random() * 10_000);
        trip.startDateTime = localDateTime[(int) (Math.random() * localDateTime.length - 1)];
        trip.endDateTime = localDateTime[(int) (Math.random() * localDateTime.length - 1)].plusWeeks(2);
        trip.startPosition = latitude + " " + longitude;
        trip.endPosition = latitude + " " + longitude;
        return trip;
    }

    public boolean associateScooter(Scooter scooter) {
        if (scooter != null && scooter.getTrip() == null) {
            return this.getScooters().add(scooter) && scooter.associateTrip(this);
        }
        return false;
    }

    public boolean dissociateTrip(Trip trip) {
        return false;
    }

    public Set<Scooter> getScooters() {
        return scooters;
    }

    public void setScooters(Set<Scooter> scooters) {
        this.scooters = scooters;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(String startPosition) {
        this.startPosition = startPosition;
    }

    public String getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(String endPosition) {
        this.endPosition = endPosition;
    }

    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}