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

import oracle.net.aso.s;

import utils.DateUtil;
import utils.NumberUtil;

import dao.SalesRecordDao;
import dao.StockOutVirtualSalesDao;
import dao.WasteRecordDao;
import model.SalesRecord;
import model.StockOutVirtualSales;
import model.WasteRecord;
@ManagedBean(name="analysisUtil", eager = true)
@ApplicationScoped
public class AnalysisUtil {
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
