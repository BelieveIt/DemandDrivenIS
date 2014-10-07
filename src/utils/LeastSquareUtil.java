package utils;

import java.util.Iterator;
import java.util.LinkedHashMap;

import org.apache.commons.math3.stat.regression.SimpleRegression;


public class LeastSquareUtil {
	public static SimpleRegression generateForecast(LinkedHashMap<String, Double> historyData){
		SimpleRegression regression = new SimpleRegression();
		Iterator<String> iterator = historyData.keySet().iterator();
		while(iterator.hasNext()){
			String xData = iterator.next();
			Double yData = historyData.get(xData);
			double xDouble = new Double(xData).doubleValue();
			double yDouble = yData.doubleValue();
			regression.addData(xDouble, yDouble);
		}
		return regression;
	}
}
