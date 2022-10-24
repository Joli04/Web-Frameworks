package app.repositories;

import app.models.Scooter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ScootersRepositoryMock implements ScooterRepository {
    private List<Scooter> scootersList;
    private int count = 0;

    public ScootersRepositoryMock() {
        scootersList = new ArrayList<>();
//        scootersList.addAll(getRandomScooters());
    }

    @Bean
    public List<Scooter> getTestScooters() {
        return null;
    }

    public List<Scooter> getRandomScooters() {
        long id = 30000;
        for (int i = 0; i < 7; i++) {
            Scooter scooter;
            scooter = Scooter.createSampleScooter(id);
            scootersList.add(scooter);
            id++;
        }
        return scootersList;
    }

    @Override
    public List<Scooter> findAll() {
        if(count == 0)
            scootersList = getRandomScooters();
        count++;
//        if(count == 0)
//            scootersList.addAll(getRandomScooters());
//        count++;
        return scootersList;
    }

    @Override
    public Scooter findById(long id) {
        for (Scooter scooter : scootersList) {
            if (scooter != null && scooter.getId() == id) {
                return scooter;
            }
        }
        return null;
    }

    @Override
    public Scooter save(Scooter scooter) {
        if (scooter.getId() == 0) {
            scooter.setId((int) (Math.random() * 1000) + 30000);
        }
        scootersList.add(scooter);
        return scooter;
    }

    @Override
    public Scooter deleteById(Long id) {
        for (int i = 0; i < scootersList.size(); i++) {
            if (scootersList.get(i).getId() == id) {
                scootersList.remove(scootersList.get(i));
            }
        }
        return null;
    }
}
