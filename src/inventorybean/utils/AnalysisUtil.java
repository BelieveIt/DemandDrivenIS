package inventorybean.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import utils.DateUtil;
import utils.NumberUtil;

import dao.SalesRecordDao;
import dao.StockOutVirtualSalesDao;
import dao.WasteRecordDao;
import model.SalesRecord;
import model.StockOutVirtualSales;
import model.StoreSellingItem;
import model.WasteRecord;
@ManagedBean(name="analysisUtil", eager = true)
@ApplicationScoped
public class AnalysisUtil {
	public static final String CATEGORY_INCLUDE_NONE = "CATEGORY_INCLUDE_NONE";
	public static final String CATEGORY_INCLUDE_CATEGORY = "CATEGORY_INCLUDE_CATEGORY";
	public static final String CATEGORY_INCLUDE_PRODUCT = "CATEGORY_INCLUDE_PRODUCT";
	public static final String PRODUCT = "PRODUCT";

	private SalesRecordDao salesRecordDao;
	private StockOutVirtualSalesDao stockOutVirtualSalesDao;
	private WasteRecordDao wasteRecordDao;

	private HashMap<String, ArrayList<SalesRecord>> salesRecordsMap;
	private HashMap<String, ArrayList<StockOutVirtualSales>> virtualSalesRecordsMap;
	private HashMap<String, ArrayList<SalesRecord>> salesRecordsMapForForecast;
	private HashMap<String, ArrayList<WasteRecord>> wasteRecordsMap;

	@PostConstruct
	public void init(){
		salesRecordDao = new SalesRecordDao();
		stockOutVirtualSalesDao = new StockOutVirtualSalesDao();
		wasteRecordDao = new WasteRecordDao();

		initSalesRecordMap();
		initVirtualSalesRecordMap();
		initSalesRecordRorForecastMap();

		initWasteRecordMap();
	}

	public void initSalesRecordMap(){
		salesRecordsMap = salesRecordDao.querySalesRecords();
	}

	public void initVirtualSalesRecordMap(){
		virtualSalesRecordsMap = stockOutVirtualSalesDao.queryStockOutVirtualSales();
	}
	//Combine real sales and virtual sales
	public void initSalesRecordRorForecastMap(){
		salesRecordsMapForForecast = salesRecordDao.querySalesRecords();
		Iterator<String> iterator = virtualSalesRecordsMap.keySet().iterator();
		while (iterator.hasNext()) {
			String currentKey = iterator.next();
			ArrayList<StockOutVirtualSales> tempArrayList  = virtualSalesRecordsMap.get(currentKey);
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

	public void initWasteRecordMap(){
		wasteRecordsMap = wasteRecordDao.queryWasteRecords();
	}

	public LinkedHashMap<String, String> queryYearsOfStores(String storeId){
		ArrayList<SalesRecord> allRecordsOfStore = salesRecordsMap.get(storeId);
		ArrayList<String> years = new ArrayList<String>();
		if(allRecordsOfStore != null){
			for(SalesRecord salesRecord : allRecordsOfStore){
				int year = DateUtil.getYearOfDate(salesRecord.getCreateTime());
				String yearString = Integer.toString(year);
				if(!years.contains(yearString))years.add(yearString);
			}
		}
		
		Collections.sort(years, new Comparator<String>() {
	        @Override
	        public int compare(String  str1, String  str2)
	        {
	        	return  str1.compareTo(str2);
	        }
	    });
		
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		for(String year : years){
			map.put(year, year);
		}
		return map;
	}
	
	public ArrayList<SalesRecord> querySalesRecords(String storeId, String productId){
		ArrayList<SalesRecord> allRecordsOfStore = salesRecordsMap.get(storeId);
		ArrayList<SalesRecord> records = new ArrayList<SalesRecord>();

		if(allRecordsOfStore == null)return records;
		for(SalesRecord salesRecord : allRecordsOfStore){
			if(salesRecord.getProductId().equals(productId)){
				records.add(salesRecord);
			}
		}
		return records;
	}

	public ArrayList<StockOutVirtualSales> queryVirtualSalesRecords(String storeId, String productId){
		ArrayList<StockOutVirtualSales> allRecordsOfStore = virtualSalesRecordsMap.get(storeId);
		ArrayList<StockOutVirtualSales> records = new ArrayList<StockOutVirtualSales>();
		if(allRecordsOfStore == null)return records;
		for(StockOutVirtualSales salesRecord : allRecordsOfStore){
			if(salesRecord.getProductId().equals(productId)){
				records.add(salesRecord);
			}
		}
		return records;
	}

	public ArrayList<WasteRecord> queryWasteRecords(String storeId, String productId){
		ArrayList<WasteRecord> allRecordsOfStore = wasteRecordsMap.get(storeId);
		ArrayList<WasteRecord> records = new ArrayList<WasteRecord>();
		if(allRecordsOfStore == null)return records;
		for(WasteRecord wasteRecord : allRecordsOfStore){
			if(wasteRecord.getProductId().equals(productId)){
				records.add(wasteRecord);
			}
		}
		return records;
	}

	public LinkedHashMap<Integer, Double> weekdayAverSales(Date date1, Date date2, String storeId, String productId){
		LinkedHashMap<Integer, Double> weekdayAverMap = new LinkedHashMap<Integer, Double>();
		LinkedHashMap<Integer, Integer> weekdaySumMap = new LinkedHashMap<Integer, Integer>();
		LinkedHashMap<Integer, Integer> weekdaySumDaysMap = new LinkedHashMap<Integer, Integer>();

		ArrayList<SalesRecord> salesRecordsForecast =  salesRecordsMapForForecast.get(storeId);
		if(salesRecordsForecast == null) salesRecordsForecast = new ArrayList<SalesRecord>();
		List<SalesRecord> limitedSalesRecords = getRecordsByLimit(date1, date2, salesRecordsForecast, productId);
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

		Calendar start = Calendar.getInstance();
		start.setTime(date1);
		DateUtil.setToZero(start);
		Calendar end = Calendar.getInstance();
		end.setTime(date2);
		DateUtil.setToZero(end);
		while(!start.after(end)){
			int currentWeekDay = start.get(Calendar.DAY_OF_WEEK);
			weekdaySumDaysMap.put(currentWeekDay, weekdaySumDaysMap.get(currentWeekDay) + 1);
			start.add(Calendar.DATE, 1);
		}
		for(SalesRecord salesRecord : limitedSalesRecords){
			Calendar c = Calendar.getInstance();
			c.setTime(salesRecord.getCreateTime());
			int currentWeekDay = c.get(Calendar.DAY_OF_WEEK);
			int currentSum = weekdaySumMap.get(currentWeekDay);
			weekdaySumMap.put(currentWeekDay, salesRecord.getSalesNumber() + currentSum);
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

	//date1 ~ date2
	private List<SalesRecord> getRecordsByLimit(Date date1, Date date2, List<SalesRecord> records, String productId){
		Calendar calendar1  = Calendar.getInstance();
		calendar1.setTime(date1);
		DateUtil.setToZero(calendar1);
		Calendar calendar2  = Calendar.getInstance();
		calendar2.setTime(date2);
		DateUtil.setToZero(calendar2);

		List<SalesRecord> list = new ArrayList<SalesRecord>();
		Calendar calendar  = Calendar.getInstance();
		for(SalesRecord salesRecord : records){
			calendar.setTime(salesRecord.getCreateTime());
			DateUtil.setToZero(calendar);

			if((calendar.getTime().equals(calendar1.getTime()) || calendar.getTime().after(calendar1.getTime()))
					&& (calendar.getTime().equals(calendar2.getTime()) || calendar.getTime().before(calendar2.getTime())) && salesRecord.getProductId().equals(productId)){
				list.add(salesRecord);
			}
		}
		return list;
	}

//Return data of one category's items
	public LinkedHashMap<String, Double> getSalesTrendDataByYear(List<StoreSellingItem> items, String storeId, String currentYear){
		LinkedHashMap<String, Double> chartDataMap = new LinkedHashMap<String, Double>();
		initChartDataMap(chartDataMap);
		ArrayList<SalesRecord> records = salesRecordsMap.get(storeId);
		LinkedHashMap<String, StoreSellingItem> productHashMap = new LinkedHashMap<String, StoreSellingItem>();
		for(StoreSellingItem sellingItem : items){
			productHashMap.put(sellingItem.getProductId(), sellingItem);
		}
		
		for(SalesRecord record : records){
			if(productHashMap.containsKey(record.getProductId())){
				int year = DateUtil.getYearOfDate(record.getCreateTime());
				int month = DateUtil.getMonthOfDate(record.getCreateTime());
				BigDecimal salesValue = productHashMap.get(record.getProductId()).getRegionListItem().getProduct().getPrice().multiply(new BigDecimal(record.getSalesNumber()));
				Double salesValueDouble = salesValue.doubleValue();
				if(Integer.toString(year).equals(currentYear)){
					Double currentValue = chartDataMap.get(DateUtil.month[month-1]);
					chartDataMap.put(DateUtil.month[month-1], currentValue + salesValueDouble);
				}
			}
		}
		return chartDataMap;
	}
//Return data for one product analysis
	public LinkedHashMap<String, Double> getForecastSalesDataForYear(String storeId, String productId){
		ArrayList<SalesRecord> records = salesRecordsMap.get(storeId);
		TreeMap<String, Double> chartDataMap = new TreeMap<String, Double>();
		for(SalesRecord record : records){
			if(record.getProductId().equals(productId)){
				int year = DateUtil.getYearOfDate(record.getCreateTime());
				String yearString = Integer.toString(year);
				if(chartDataMap.containsKey(yearString)){
					Double currentNum = chartDataMap.get(yearString);
					chartDataMap.put(yearString, currentNum + record.getSalesNumber());
				}else {
					chartDataMap.put(yearString, new Double(0) + record.getSalesNumber());
				}
			}
		}	
		return new LinkedHashMap<String, Double>(chartDataMap);
	}
	
	public LinkedHashMap<String, Double> getForecastSalesDataForMonth(String storeId, String productId, String currentYear){
		ArrayList<SalesRecord> records = salesRecordsMap.get(storeId);
		LinkedHashMap<String, Double> chartDataMap = new LinkedHashMap<String, Double>();
		initChartDataMap(chartDataMap);
		for(SalesRecord record : records){
			if(record.getProductId().equals(productId)){
				int year = DateUtil.getYearOfDate(record.getCreateTime());
				int month = DateUtil.getMonthOfDate(record.getCreateTime());
				if(Integer.toString(year).equals(currentYear)){
					Double currentValue = chartDataMap.get(DateUtil.month[month-1]);
					chartDataMap.put(DateUtil.month[month-1], currentValue + record.getSalesNumber());
				}
			}
		}	
		return chartDataMap;
	}
	public LinkedHashMap<String, Double> getForecastSalesDataForMonthForForecast(String storeId, String productId){
		ArrayList<SalesRecord> records = salesRecordsMap.get(storeId);
		TreeMap<String, Double> chartDataMap = new TreeMap<String, Double>();
		ArrayList<String> lastTwelveDate = DateUtil.getLastYearAndMonthList(new Date(),6);
		for(int i = 0; i <= 6; i++){
			chartDataMap.put(lastTwelveDate.get(i), new Double(0));
		}

		for(SalesRecord record : records){
			if(record.getProductId().equals(productId)){
				String yearAndMonthString = DateUtil.getYearAndMonth(record.getCreateTime());
				if(lastTwelveDate.contains(yearAndMonthString)){
					if(chartDataMap.containsKey(yearAndMonthString)){
						Double currentValue = chartDataMap.get(yearAndMonthString);
						chartDataMap.put(yearAndMonthString, currentValue + record.getSalesNumber());
					}else {
						chartDataMap.put(yearAndMonthString, new Double(0) + record.getSalesNumber());
					}
				}
			}
		}	
		return new LinkedHashMap<String, Double>(chartDataMap);
	}	
	
	
//Return number of one category's items
	public Integer getSalesShareDataByYear(List<StoreSellingItem> items, String storeId, String currentYear){
		ArrayList<SalesRecord> records = salesRecordsMap.get(storeId);
		LinkedHashMap<String, StoreSellingItem> productHashMap = new LinkedHashMap<String, StoreSellingItem>();
		for(StoreSellingItem sellingItem : items){
			productHashMap.put(sellingItem.getProductId(), sellingItem);
		}
		
		Integer number = 0;
		for(SalesRecord record : records){
			int year = DateUtil.getYearOfDate(record.getCreateTime());
			if(Integer.toString(year).equals(currentYear)){
				if(productHashMap.containsKey(record.getProductId())){
					number = number + record.getSalesNumber();
				}
			}
		}
		return number;
	}

	public LinkedHashMap<StoreSellingItem, Integer> getSalesLowingMoveDataByYear(List<StoreSellingItem> items, String storeId, String currentYear, Integer number){
		ArrayList<SalesRecord> records = salesRecordsMap.get(storeId);
		LinkedHashMap<String, StoreSellingItem> productHashMap = new LinkedHashMap<String, StoreSellingItem>();
		for(StoreSellingItem sellingItem : items){
			productHashMap.put(sellingItem.getProductId(), sellingItem);
		}
		
		HashMap<String, Integer> salesSumById = new HashMap<String, Integer>();
		
		for(SalesRecord record : records){
			if(productHashMap.containsKey(record.getProductId())){
				int year = DateUtil.getYearOfDate(record.getCreateTime());
				if(Integer.toString(year).equals(currentYear)){
					if(salesSumById.containsKey(record.getProductId())){
						Integer currentSalesVolume = salesSumById.get(record.getProductId());
						salesSumById.put(record.getProductId(), currentSalesVolume + record.getSalesNumber());
					}else {
						salesSumById.put(record.getProductId(), record.getSalesNumber());
					}
				}

			}
		}	
		@SuppressWarnings({ "rawtypes", "unchecked" })
		ValueComparator comparator =  new ValueComparator(salesSumById);
		@SuppressWarnings("unchecked")
		TreeMap<String,Integer> sorted_map = new TreeMap<String,Integer>(comparator);
		sorted_map.putAll(salesSumById);

		Iterator<String> iterator = sorted_map.keySet().iterator();
		LinkedHashMap<StoreSellingItem, Integer> lowMovingList = new LinkedHashMap<StoreSellingItem, Integer>();
		int index = 0;
		while(iterator.hasNext()){
			String id = iterator.next();
			lowMovingList.put(productHashMap.get(id), sorted_map.get(id));
			index++;
			if(index >= number){
				break;
			}
		}
		return lowMovingList;
	}
	

	
	
	@SuppressWarnings("rawtypes")
	static class ValueComparator<K, V extends Comparable> implements Comparator<K> {
		  
		  private final Map<K,V> map;
		  public ValueComparator(Map<K,V> map) {
		  this.map = map;
		  }
		  
		  public int compare(K key1, K key2) {
		   V v1 = this.map.get(key1);
		   V v2 = this.map.get(key2);
		   @SuppressWarnings("unchecked")
		   int c = v1.compareTo(v2);
		   if (c != 0) {
		    return c;
		   }
		   Integer hashCode1 = key1.hashCode();
		   Integer hashCode2 = key2.hashCode();
		   return hashCode1.compareTo(hashCode2);
		  }
		 }
	private void initChartDataMap(LinkedHashMap<String, Double> chartDataMap){
		for(int i = 0; i <= 11; i++){
			chartDataMap.put(DateUtil.month[i], new Double(0));
		}
	}
	

	
	public HashMap<String, ArrayList<StockOutVirtualSales>> getVirtualSalesRecordsMap() {
		return virtualSalesRecordsMap;
	}

	public void setVirtualSalesRecordsMap(HashMap<String, ArrayList<StockOutVirtualSales>> virtualSalesRecordsMap) {
		this.virtualSalesRecordsMap = virtualSalesRecordsMap;
	}

	public HashMap<String, ArrayList<SalesRecord>> getSalesRecordsMap() {
		return salesRecordsMap;
	}

	public void setSalesRecordsMap(HashMap<String, ArrayList<SalesRecord>> salesRecordsMap) {
		this.salesRecordsMap = salesRecordsMap;
	}

	public HashMap<String, ArrayList<WasteRecord>> getWasteRecordsMap() {
		return wasteRecordsMap;
	}

	public void setWasteRecordsMap(HashMap<String, ArrayList<WasteRecord>> wasteRecordsMap) {
		this.wasteRecordsMap = wasteRecordsMap;
	}


}
