package edu.vsb.dais.appmonitoring.view;

import edu.vsb.dais.appmonitoring.service.LocationService;
import edu.vsb.dais.appmonitoring.service.models.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * Created by vasekric on 13. 5. 2015.
 */
@Component
@Scope("request")
public class LocationCreateView {

    @Autowired private LocationService locationService;

    private Location location = new Location();

    public void save() {
        locationService.save(location);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Location was created."));
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
