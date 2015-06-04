package edu.vsb.dais.appmonitoring.view;

import edu.vsb.dais.appmonitoring.service.LocationService;
import edu.vsb.dais.appmonitoring.service.models.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.List;

/**
 * Created by vasekric on 13. 5. 2015.
 */
@Component
@Scope("session")
public class LocationView {

    @Autowired private LocationService locationService;

    private List<Location> locations;
    private Location selectedLocation;

    @PostConstruct
    public void init() {
        locations = locationService.findAll();
        selectedLocation = new Location();
    }

    public void rowSelect() {
        System.out.println(selectedLocation);
    }

    public void update() {
        locationService.update(selectedLocation);
        locations = locationService.findAll();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Location was updated."));
        selectedLocation = new Location();
    }

    public void delete() {
        locationService.delete(selectedLocation.getId());
        locations = locationService.findAll();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Location was deleted."));
        selectedLocation = new Location();
    }


    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public Location getSelectedLocation() {
        return selectedLocation;
    }

    public void setSelectedLocation(Location selectedLocation) {
        this.selectedLocation = selectedLocation;
    }
}
