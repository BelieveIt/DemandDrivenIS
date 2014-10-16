package inventorybean.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import utils.NumberUtil;

import dao.SalesRecordDao;
import dao.StockOutVirtualSalesDao;
import dao.StoreDao;
import dao.WasteRecordDao;

import model.ReplenishmentReportItem;
import model.SalesRecord;
import model.StockOutVirtualSales;
import model.Store;
import model.StoreSellingItem;
import model.WasteRecord;
@ManagedBean(name="requestUtil")
@ViewScoped
public class RequestUtil implements Serializable{
	private static final long serialVersionUID = 1598461370936129633L;


	private SalesRecordDao salesRecordDao;
	private StockOutVirtualSalesDao stockOutVirtualSalesDao;
	private WasteRecordDao wasteRecordDao;
	private StoreDao storeDao;

	private Store store;

	private HashMap<String, ArrayList<SalesRecord>> salesRecordsMapForForecast;
	private HashMap<String, TreeMap<Integer, Integer>> salesRecordsMapForForecastOfStoreByProduct;

	private HashMap<String, ArrayList<WasteRecord>> wasteRecordsMap;

	@PostConstruct
	public void init(){
		salesRecordDao = new SalesRecordDao();
		stockOutVirtualSalesDao = new StockOutVirtualSalesDao();
		wasteRecordDao = new WasteRecordDao();
		storeDao = new StoreDao();
		//TODO
		store = storeDao.queryStoreById("1");

		initSalesRecordRorForecastMap();
		initSalesRecordsMapForForecastOfStoreByProduct();

		initWasteRecordMap();
	}
	//Combine real sales and virtual sales
	public void initSalesRecordRorForecastMap(){
		HashMap<String, ArrayList<StockOutVirtualSales>> virtualSalesRecordsMap = stockOutVirtualSalesDao.queryStockOutVirtualSales();
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

	public void initSalesRecordsMapForForecastOfStoreByProduct(){
		List<SalesRecord> records = salesRecordsMapForForecast.get(store.getStoreId());
		HashMap<String, TreeMap<Integer, Integer>> mapForSum = new HashMap<String, TreeMap<Integer,Integer>>();
		HashMap<String, TreeMap<Integer, Integer>> mapForDayNum = new HashMap<String, TreeMap<Integer,Integer>>();

		Date todayDate  = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(todayDate);
		calendar.add(Calendar.DATE, -90);
		Date earliestDate = calendar.getTime();

		Calendar calendarForDayOfWeek = Calendar.getInstance();
		for(SalesRecord record : records){
			if(record.getCreateTime().after(earliestDate)){

				calendarForDayOfWeek.setTime(record.getCreateTime());
				int dayOfWeek = calendarForDayOfWeek.get(Calendar.DAY_OF_WEEK);
				if(mapForSum.containsKey(record.getProductId())){
					TreeMap<Integer, Integer> dayOfWeekMapOfSum = mapForSum.get(record.getProductId());
					TreeMap<Integer, Integer> dayOfWeekMapofNum = mapForDayNum.get(record.getProductId());
					if(dayOfWeekMapOfSum.containsKey(dayOfWeek)){
						Integer currentVolume = dayOfWeekMapOfSum.get(dayOfWeek);
						dayOfWeekMapOfSum.put(dayOfWeek, currentVolume+record.getSalesNumber());

						Integer currentNum = dayOfWeekMapofNum.get(dayOfWeek);
						dayOfWeekMapofNum.put(dayOfWeek, currentNum + 1);
					}else {
						dayOfWeekMapOfSum.put(dayOfWeek, record.getSalesNumber());
						dayOfWeekMapofNum.put(dayOfWeek, 1);
					}
				}else {
					TreeMap<Integer, Integer> dayOfWeekMapofSum = new TreeMap<Integer, Integer>();
					mapForSum.put(record.getProductId(), dayOfWeekMapofSum);
					dayOfWeekMapofSum.put(dayOfWeek, record.getSalesNumber());

					TreeMap<Integer, Integer> dayOfWeekMapofNum = new TreeMap<Integer, Integer>();
					mapForDayNum.put(record.getProductId(), dayOfWeekMapofNum);
					dayOfWeekMapofNum.put(dayOfWeek, 1);
				}

			}
		}
		salesRecordsMapForForecastOfStoreByProduct = new HashMap<String, TreeMap<Integer,Integer>>();
		Iterator<String> iterator = mapForSum.keySet().iterator();
		while(iterator.hasNext()){
			String productId = iterator.next();
			if(salesRecordsMapForForecastOfStoreByProduct.containsKey(productId)){

			}else {
				salesRecordsMapForForecastOfStoreByProduct.put(productId, getInitedDayOfWeekMap());
				for(int i = 1; i <= 7; i++){
					Integer sum = mapForSum.get(productId).get(i);
					Integer num = mapForDayNum.get(productId).get(i);
					if(sum!=null && num!=null){
						salesRecordsMapForForecastOfStoreByProduct.get(productId).put(i, new Double(NumberUtil.divide(sum, num)).intValue());
					}
				}
			}
		}

	}
	private TreeMap<Integer, Integer> getInitedDayOfWeekMap(){
		TreeMap<Integer, Integer> dayOfWeekMap = new TreeMap<Integer, Integer>();
		for(int i = 1; i <= 7; i++){
			dayOfWeekMap.put(i, 0);
		}
		return dayOfWeekMap;
	}

	public void initWasteRecordMap(){
		wasteRecordsMap = wasteRecordDao.queryWasteRecords();
	}

	public List<ReplenishmentReportItem> calculateReplenishmentForEveryDay(){

		List<ReplenishmentReportItem> items = new ArrayList<ReplenishmentReportItem>();
		StoreDao storeDao = new StoreDao();
		List<StoreSellingItem> tempProducts = storeDao.queryStoreSellingItemsByStoreId(store.getStoreId());
		List<StoreSellingItem> products = new ArrayList<StoreSellingItem>();
		for(StoreSellingItem item : tempProducts){
			if(item.getRegionListItem()!=null && item.getRegionListItem().getProduct().getDeliveryFrequency().equals("everyday")){
				products.add(item);
			}
		}

		Date today = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		calendar.add(Calendar.DATE, 1);

		int periodDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		for(StoreSellingItem item : products){
			String productId = item.getProductId();
			Integer currentInventory = item.getCurrentInventory();
			Integer inventoryOnNext = item.getRegionListItem().getProduct().getMinInventory();

			Integer salesForecast = salesRecordsMapForForecastOfStoreByProduct.get(item.getProductId()).get(periodDayOfWeek);
			Integer wasteForecast = 0;

			Integer replenishmentVolume = salesForecast + wasteForecast + inventoryOnNext - currentInventory;
			if(replenishmentVolume > 0){
				ReplenishmentReportItem replenishmentReportItem = new ReplenishmentReportItem();
				replenishmentReportItem.setCurrentInventory(currentInventory);
				replenishmentReportItem.setMinInventoryOnNextDelivery(inventoryOnNext);
				replenishmentReportItem.setProductId(productId);
				replenishmentReportItem.setRegionListItem(item.getRegionListItem());
				replenishmentReportItem.setReplenishmentNumber(replenishmentVolume);
				replenishmentReportItem.setAutoCalculatedReplenishmentNumber(replenishmentVolume);
				replenishmentReportItem.setSalesForecast(salesForecast);
				replenishmentReportItem.setWasteForecast(wasteForecast);
				items.add(replenishmentReportItem);
			}
		}
		return items;
	}



	public List<ReplenishmentReportItem> calculateReplenishmentForEveryWeek(String storeId){
		List<ReplenishmentReportItem> items = new ArrayList<ReplenishmentReportItem>();
		return items;
	}

	public List<ReplenishmentReportItem> calculateReplenishmentForEveryMonth(String storeId){
		List<ReplenishmentReportItem> items = new ArrayList<ReplenishmentReportItem>();
		return items;
	}


	public HashMap<String, ArrayList<WasteRecord>> getWasteRecordsMap() {
		return wasteRecordsMap;
	}
	public void setWasteRecordsMap(HashMap<String, ArrayList<WasteRecord>> wasteRecordsMap) {
		this.wasteRecordsMap = wasteRecordsMap;
	}
	public HashMap<String, ArrayList<SalesRecord>> getSalesRecordsMapForForecast() {
		return salesRecordsMapForForecast;
	}
	public void setSalesRecordsMapForForecast(
			HashMap<String, ArrayList<SalesRecord>> salesRecordsMapForForecast) {
		this.salesRecordsMapForForecast = salesRecordsMapForForecast;
	}

	public HashMap<String, TreeMap<Integer, Integer>> getSalesRecordsMapForForecastOfStoreByProduct() {
		return salesRecordsMapForForecastOfStoreByProduct;
	}
	public void setSalesRecordsMapForForecastOfStoreByProduct(
			HashMap<String, TreeMap<Integer, Integer>> salesRecordsMapForForecastOfStoreByProduct) {
		this.salesRecordsMapForForecastOfStoreByProduct = salesRecordsMapForForecastOfStoreByProduct;
	}
}
