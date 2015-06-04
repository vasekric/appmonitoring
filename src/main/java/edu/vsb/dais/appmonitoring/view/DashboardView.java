package edu.vsb.dais.appmonitoring.view;

import edu.vsb.dais.appmonitoring.service.DashboardService;
import edu.vsb.dais.appmonitoring.service.models.EntityStatusPanel;
import edu.vsb.dais.appmonitoring.view.factory.PanelFactory;
import org.primefaces.component.dashboard.Dashboard;
import org.primefaces.component.panel.Panel;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

/**
 * Created by vasekric on 8. 5. 2015.
 */
@Component
@Scope("request")
public class DashboardView implements Serializable {

    private Dashboard dashboard;
    private int columns = 3;
    @Autowired private DashboardService dashboardService;
    @Autowired private PanelFactory panelFactory;

    @PostConstruct
    public void init() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Application application = fc.getApplication();

        dashboard = (Dashboard) application.createComponent(fc, "org.primefaces.component.Dashboard", "org.primefaces.component.DashboardRenderer");
        dashboard.setId("dashboard");

        DashboardModel model = new DefaultDashboardModel();
        for( int i = 0, n = columns; i < n; i++ ) {
            DashboardColumn column = new DefaultDashboardColumn();
            model.addColumn(column);
        }
        dashboard.setModel(model);

        int userId = 1;
        final List<EntityStatusPanel> dashboardData = dashboardService.getDashboardData(userId);

        final List<Panel> panels = panelFactory.createPanels(dashboardData, fc);

        for(int i = 0; i < panels.size(); i++) {
            final Panel panel = panels.get(i);
            if(panel == null) {
                continue;
            }
            getDashboard().getChildren().add(panel);
            DashboardColumn column = model.getColumn(i%columns);
            column.addWidget(panel.getId());
        };
    }

    public Dashboard getDashboard() {
        return dashboard;
    }

    public void setDashboard(Dashboard dashboard) {
        this.dashboard = dashboard;
    }


}