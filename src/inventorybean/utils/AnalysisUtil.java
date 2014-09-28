package inventorybean.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import utils.NumberUtil;

import dao.SalesRecordDao;
import dao.StockOutVirtualSalesDao;
import model.SalesRecord;
import model.StockOutVirtualSales;
@ManagedBean(name="analysisUtil", eager = true)
@ApplicationScoped
public class AnalysisUtil {
	private SalesRecordDao salesRecordDao;
	private StockOutVirtualSalesDao stockOutVirtualSalesDao;
	private HashMap<String, ArrayList<SalesRecord>> salesRecordsMapForForecast;
	@PostConstruct
	public void init(){
		salesRecordDao = new SalesRecordDao();
		stockOutVirtualSalesDao = new StockOutVirtualSalesDao();
		initSalesRecordsMap();
	}
	private void initSalesRecordsMap(){
		salesRecordsMapForForecast = salesRecordDao.querySalesRecords();
		HashMap<String, ArrayList<StockOutVirtualSales>> tempMap = stockOutVirtualSalesDao.queryStockOutVirtualSales();
		Iterator<String> iterator = tempMap.keySet().iterator();
		while (iterator.hasNext()) {
			String currentKey = iterator.next();
			ArrayList<StockOutVirtualSales> tempArrayList  = tempMap.get(currentKey);
			for(StockOutVirtualSales stockOutVirtualSales : tempArrayList){
				SalesRecord salesRecord = new SalesRecord();
				salesRecord.setCreateTime(stockOutVirtualSales.getCreateTime());
				salesRecord.setProductId(stockOutVirtualSales.getProductId());
				salesRecord.setSalesNumber(stockOutVirtualSales.getStockoutNumber());
				salesRecord.setStoreId(stockOutVirtualSales.getStoreId());
				salesRecordsMapForForecast.get(currentKey).add(salesRecord);
			}
		}
	}

	//date1 ~ date2
	public List<SalesRecord> getRecordsByLimit(Date date1, Date date2, List<SalesRecord> records, String productId){
		List<SalesRecord> list = new ArrayList<SalesRecord>();
		for(SalesRecord salesRecord : records){
			if((salesRecord.getCreateTime().equals(date1) || salesRecord.getCreateTime().after(date1))
					&& (salesRecord.getCreateTime().equals(date2) || salesRecord.getCreateTime().before(date2)) && salesRecord.getProductId().equals(productId)){
				list.add(salesRecord);
				
			}
		}
		return list;
	}

	public LinkedHashMap<Integer, Double> weekdayAverSales(Date date1, Date date2, String storeId, String productId){
		LinkedHashMap<Integer, Double> weekdayAverMap = new LinkedHashMap<Integer, Double>();
		LinkedHashMap<Integer, Integer> weekdaySumMap = new LinkedHashMap<Integer, Integer>();
		LinkedHashMap<Integer, Integer> weekdaySumDaysMap = new LinkedHashMap<Integer, Integer>();
        Integer sum = 0;

		List<SalesRecord> limitedSalesRecords = getRecordsByLimit(date1, date2, salesRecordsMapForForecast.get(storeId), productId);
		weekdayAverMap.put(Calendar.SUNDAY, new Double(1));
		weekdayAverMap.put(Calendar.MONDAY, new Double(1));
		weekdayAverMap.put(Calendar.TUESDAY, new Double(1));
		weekdayAverMap.put(Calendar.WEDNESDAY	, new Double(1));
		weekdayAverMap.put(Calendar.THURSDAY, new Double(1));
		weekdayAverMap.put(Calendar.FRIDAY, new Double(1));
		weekdayAverMap.put(Calendar.SATURDAY, new Double(1));

		weekdaySumMap.put(Calendar.SUNDAY, new Integer(0));
		weekdaySumMap.put(Calendar.MONDAY, new Integer(0));
		weekdaySumMap.put(Calendar.TUESDAY, new Integer(0));
		weekdaySumMap.put(Calendar.WEDNESDAY	, new Integer(0));
		weekdaySumMap.put(Calendar.THURSDAY, new Integer(0));
		weekdaySumMap.put(Calendar.FRIDAY, new Integer(0));
		weekdaySumMap.put(Calendar.SATURDAY, new Integer(0));

		weekdaySumDaysMap.put(Calendar.SUNDAY, new Integer(0));
		weekdaySumDaysMap.put(Calendar.MONDAY, new Integer(0));
		weekdaySumDaysMap.put(Calendar.TUESDAY, new Integer(0));
		weekdaySumDaysMap.put(Calendar.WEDNESDAY, new Integer(0));
		weekdaySumDaysMap.put(Calendar.THURSDAY, new Integer(0));
		weekdaySumDaysMap.put(Calendar.FRIDAY, new Integer(0));
		weekdaySumDaysMap.put(Calendar.SATURDAY, new Integer(0));
		
		
		for(SalesRecord salesRecord : limitedSalesRecords){
			Calendar c = Calendar.getInstance();
			c.setTime(salesRecord.getCreateTime());
			int currentWeekDay = c.get(Calendar.DAY_OF_WEEK);
			int currentSum = weekdaySumMap.get(currentWeekDay);
			weekdaySumMap.put(currentWeekDay, salesRecord.getSalesNumber() + currentSum);
			sum = sum + salesRecord.getSalesNumber();
			weekdaySumDaysMap.put(currentWeekDay, weekdaySumDaysMap.get(currentWeekDay) + 1);
		}

		for(int i = 1; i <= 7; i++){
			if(weekdaySumDaysMap.get(i) != 0){
				double averageVolumeOfCertainday = NumberUtil.divide(weekdaySumMap.get(i), weekdaySumDaysMap.get(i));
				weekdayAverMap.put(i, averageVolumeOfCertainday);
			}else {
				weekdayAverMap.put(i, (double) 0);
			}
		}
		return weekdayAverMap;
	}


}
