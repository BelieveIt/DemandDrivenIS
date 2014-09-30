package inventorybean.utils;

import java.util.Iterator;
import java.util.LinkedHashMap;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;


public class ChartUtil {
public static LineChartModel generateLineChartModel(String chartTitle, String xLabel, String yLabel,
		LinkedHashMap<String, LinkedHashMap<String, Double>> dataMap){
	LineChartModel lineChartModel = new LineChartModel();
    Iterator<String> iterator1 = dataMap.keySet().iterator();

    while (iterator1.hasNext()) {
    	ChartSeries chartSeries = new ChartSeries();
    	String currentDataLabel = iterator1.next();
    	chartSeries.setLabel(currentDataLabel);

    	LinkedHashMap<String, Double> currentData = dataMap.get(currentDataLabel);
    	Iterator<String> iterator2 = currentData.keySet().iterator();
        while (iterator2.hasNext()) {
        	String currentXValue = iterator2.next();
        	chartSeries.set(currentXValue, currentData.get(currentXValue));
    	}
        lineChartModel.addSeries(chartSeries);
    }

    lineChartModel.setTitle(chartTitle);
    lineChartModel.setLegendPosition("e");
    lineChartModel.setShowPointLabels(true);
    lineChartModel.getAxes().put(AxisType.X, new CategoryAxis(xLabel));
    Axis yAxis  = lineChartModel.getAxis(AxisType.Y);
    yAxis.setLabel(yLabel);
    yAxis.setMin(0);
    return lineChartModel;
}
}
