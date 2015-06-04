package edu.vsb.dais.appmonitoring.service;

import edu.vsb.dais.appmonitoring.database.repositories.LocationRepository;
import edu.vsb.dais.appmonitoring.service.models.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by vasekric on 13. 5. 2015.
 */
@Service
public class LocationService {

    @Autowired private LocationRepository locationRepository;

    public Location save(Location location) {
        final Location savedLocation = locationRepository.save(location);

        return savedLocation;
    }

    public List<Location> findAll() {
        final List<Location> allLocations = locationRepository.findAll();

        return allLocations;
    }

    public void update(Location location) {
        locationRepository.update(location);
    }

    public void delete(Integer id) {
        locationRepository.delete(id);
    }
}
