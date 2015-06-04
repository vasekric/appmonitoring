package edu.vsb.dais.appmonitoring.view.factory;

import edu.vsb.dais.appmonitoring.service.models.EntityStatusPanel;
import org.primefaces.component.chart.Chart;
import org.primefaces.component.column.Column;
import org.primefaces.component.panel.Panel;
import org.primefaces.component.panelgrid.PanelGrid;
import org.primefaces.component.row.Row;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.springframework.stereotype.Component;

import javax.faces.application.Application;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by vasekric on 8. 5. 2015.
 */
@Component
public class PanelFactory {

    public List<Panel> createPanels(List<EntityStatusPanel> entities, FacesContext facesContext) {
        List<Panel> panels = new ArrayList<>();
        for(int i = 0; i < entities.size(); i++) {
            final Panel panel = createPanel(entities.get(i), facesContext, i);
            panels.add(panel);
        }
        return panels;
    }

    public Panel createPanel(EntityStatusPanel entity, FacesContext facesContext, int num) {
        if(entity == null) {
            return null;
        }
        final Application application = facesContext.getApplication();
        Panel panel = (Panel) application.createComponent(facesContext, "org.primefaces.component.Panel", "org.primefaces.component.PanelRenderer");
        panel.setId("entity"+num);
        panel.setHeader(entity.getEntityName());

        final PanelGrid panelGrid = new PanelGrid();

        final Row firstRow = new Row();
        final Column measured = new Column();
        final HtmlOutputText measuredText = new HtmlOutputText();
        long timeDiff = -1;
        if(entity.getTimestamp() != null) {
             timeDiff = ((new Date().getTime() - entity.getTimestamp().getTime()) / 1000);
        }
        measuredText.setValue("Measured: "+timeDiff+"s ago");
        measuredText.setId("measured" + num + "-" + 1);
        measured.getChildren().add(measuredText);
        measured.setColspan(3);
        firstRow.getChildren().add(measured);
        final Column up = new Column();
        final HtmlOutputText actualText = new HtmlOutputText();
        String aStatus = "^UP";
        String statusColor = "green";
        if(entity.getActualStatus() == 2) {
            aStatus = "WARN";
            statusColor = "orange";
        }
        else if(entity.getActualStatus() == 3) {
            aStatus = "vDOWN";
            statusColor = "red";
        }
        actualText.setStyle("color:"+statusColor+"; font-size:xx-large;");
        actualText.setValue(aStatus);
        actualText.setId("actualstatus" + num + "-" + 2);
        up.getChildren().add(actualText);
        up.setRowspan(3);
        firstRow.getChildren().add(up);
        panelGrid.getChildren().add(firstRow);

        final Row secondRow = new Row();
        final Column responseTime = new Column();
        responseTime.setColspan(3);
        final HtmlOutputText responseTimeText = new HtmlOutputText();
        responseTimeText.setValue("Last Response Time: " + entity.getLastResponseTime());
        responseTimeText.setId("responseTimeText" + num + "-" + 3);
        responseTime.getChildren().add(responseTimeText);
        secondRow.getChildren().add(responseTime);
        panelGrid.getChildren().add(secondRow);

        final Row thirdRow = new Row();
        final Column response = new Column();
        response.setColspan(3);
        final HtmlOutputText responseText = new HtmlOutputText();
        responseText.setValue("Last Response: " + entity.getLastResponse());
        responseText.setId("responseText" + num + "-" + 34);
        response.getChildren().add(responseText);
        thirdRow.getChildren().add(response);
        panelGrid.getChildren().add(thirdRow);

        final Row fourthRow = new Row();
        final Column status = new Column();
        status.setColspan(3);
        final HtmlOutputText statusText = new HtmlOutputText();
        statusText.setValue("Last Status: " + entity.getLastStatus());
        statusText.setId("statusText" + num + "-" + 4);
        status.getChildren().add(statusText);
        fourthRow.getChildren().add(status);
        panelGrid.getChildren().add(fourthRow);

        final Row fifthRow = new Row();
        final Column last24 = new Column();
        final HtmlOutputText last24Text = new HtmlOutputText();
        last24Text.setValue("Last 24h");
        last24Text.setId("last24Text" + num + "-" + 5);
        last24.getChildren().add(last24Text);
        final Column up24 = new Column();
        final HtmlOutputText upText = new HtmlOutputText();
        upText.setStyle("color:green;");
        upText.setValue("UP:" + entity.getUpPercent()+"%");
        upText.setId("upText" + num + "-" + 5);
        up24.getChildren().add(upText);
        final Column warn24 = new Column();
        final HtmlOutputText warnText = new HtmlOutputText();
        warnText.setStyle("color:orange;");
        warnText.setValue("WARN:" + entity.getWarnPercent()+"%");
        warnText.setId("warnText" + num + "-" + 5);
        warn24.getChildren().add(warnText);
        final Column err24 = new Column();
        final HtmlOutputText errText = new HtmlOutputText();
        errText.setStyle("color:red;");
        errText.setValue("ERR:" + entity.getErrPercent() + "%");
        errText.setId("errText" + num + "-" + 5);
        err24.getChildren().add(errText);
        fifthRow.getChildren().add(last24);
        fifthRow.getChildren().add(up24);
        fifthRow.getChildren().add(warn24);
        fifthRow.getChildren().add(err24);
        panelGrid.getChildren().add(fifthRow);

        panel.getChildren().add(panelGrid);

        final Chart chart = (Chart) application.createComponent(facesContext, "org.primefaces.component.Chart", "org.primefaces.component.ChartRenderer");
        chart.setType("line");
        chart.setStyle("height:200px;");
        final LineChartSeries chartSeries = new LineChartSeries();
        final LineChartSeries chartGreenSeries = new LineChartSeries();
        final LineChartSeries chartYellowSeries = new LineChartSeries();
        for(int i = 0; i < entity.getResponseTimes().size(); i++) {
            chartGreenSeries.set(i, entity.getThresholdWarning());
            chartYellowSeries.set(i, entity.getThresholdError());
            chartSeries.set(i, entity.getResponseTimes().get(i));
        }
        chartSeries.setShowMarker(false);
        chartGreenSeries.setShowMarker(false);
        chartYellowSeries.setShowMarker(false);
        chartSeries.setLabel(entity.getEntityName());
        final LineChartModel lineChartModel = new LineChartModel();
        lineChartModel.setExtender("chartExtender");
        lineChartModel.setTitle("Response Times Last 24h");
        lineChartModel.setLegendPosition("e");
        Axis yAxis = lineChartModel.getAxis(AxisType.Y);
        yAxis.setMin(0);
        yAxis.setLabel("Response Time[ms]");
        Axis xAxis = lineChartModel.getAxis(AxisType.X);
        xAxis.setMin(0);
        xAxis.setMax(entity.getResponseTimes().size() - 1);
        lineChartModel.addSeries(chartYellowSeries);
        lineChartModel.addSeries(chartGreenSeries);
        lineChartModel.addSeries(chartSeries);
        lineChartModel.setSeriesColors("E69999, E6E699, 3399FF");
        chart.setModel(lineChartModel);
        chart.setId("chart"+num);

        panel.getChildren().add(chart);

        return panel;
    }

}
