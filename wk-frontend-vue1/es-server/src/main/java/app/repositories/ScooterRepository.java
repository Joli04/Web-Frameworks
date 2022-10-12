package app.repositories;

import app.models.Scooter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface ScooterRepository {
    List<Scooter> findAll();
    Scooter findById(long id);

    Scooter save(Scooter scooter);

    Scooter deleteById(Long id);
}
