package inventorybean.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import oracle.net.aso.i;

import utils.NumberUtil;

import dao.SalesRecordDao;
import dao.StockOutVirtualSalesDao;
import dao.StoreDao;
import dao.WasteRecordDao;
import model.Product;
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
	private HashMap<String, LinkedHashMap<String, Integer>> salesRecordsMapForForecastOfStoreByProductForWeek;
	private HashMap<String, LinkedHashMap<String, Integer>> salesRecordsMapForForecastOfStoreByProductForMonth;

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
		initWasteRecordMap();
	}
	//Combine real sales and virtual sales <storeId, <recordsList>>
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

	//<productId, <dayOfWeekIndex, avgNum>>
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

	public List<ReplenishmentReportItem> calculateReplenishmentForEveryDay(){
		List<ReplenishmentReportItem> items = new ArrayList<ReplenishmentReportItem>();
		StoreDao storeDao = new StoreDao();
		List<StoreSellingItem> tempProducts = storeDao.queryStoreSellingItemsByStoreId(store.getStoreId());
		List<StoreSellingItem> products = new ArrayList<StoreSellingItem>();
		for(StoreSellingItem item : tempProducts){
			if(item.getRegionListItem()!=null && item.getRegionListItem().getProduct().getDeliveryFrequency().equals(Product.EVERYDAY)){
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
	//<productId, <dayOfWeekIndex, avgNum>>
    public void initSalesRecordsMapForForecastOfStoreByProductForWeek(){
		ArrayList<String> weekList = new ArrayList<String>();
		ArrayList<Date> firstDayOfWeek = new ArrayList<Date>();
		ArrayList<Date> endDayOfWeek = new ArrayList<Date>();

		Date today = new Date();
		for(int i = 34; i >=0 ; i --){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(today);
			calendar.add(Calendar.DATE, -1 * i);

			int modNum = i % 7;
			if(modNum == 6){
				Calendar calendar2 = Calendar.getInstance();
				calendar2.setTime(calendar.getTime());
				int firstDay = calendar2.get(Calendar.DAY_OF_MONTH);
				int firstMonth = calendar2.get(Calendar.MONTH);
				firstDayOfWeek.add(calendar2.getTime());

				calendar2.add(Calendar.DATE, 6);
				int lastDay = calendar2.get(Calendar.DAY_OF_MONTH);
				int lastMonth = calendar2.get(Calendar.MONTH);
				endDayOfWeek.add(calendar2.getTime());

				weekList.add(Integer.toString(firstMonth+1) + "." + Integer.toString(firstDay) + "-" + Integer.toString(lastMonth+1) + "." + Integer.toString(lastDay));
			}
		}


		StoreDao storeDao = new StoreDao();
		List<StoreSellingItem> tempProducts = storeDao.queryStoreSellingItemsByStoreId(store.getStoreId());
		List<StoreSellingItem> products = new ArrayList<StoreSellingItem>();
		for(StoreSellingItem item : tempProducts){
			if(item.getRegionListItem()!=null && item.getRegionListItem().getProduct().getDeliveryFrequency().equals(Product.EVERYWEEK)){
				products.add(item);
			}
		}

		ArrayList<String> productIdList = new ArrayList<String>();
		for(StoreSellingItem item : products){
			productIdList.add(item.getProductId());
		}

		List<SalesRecord> records = salesRecordsMapForForecast.get(store.getStoreId());
		salesRecordsMapForForecastOfStoreByProductForWeek = new HashMap<String, LinkedHashMap<String,Integer>>();

		for(SalesRecord record : records){
			if(productIdList.contains(record.getProductId())){
				if(salesRecordsMapForForecastOfStoreByProductForWeek.containsKey(record.getProductId())){
					LinkedHashMap<String, Integer> tempWeekList = salesRecordsMapForForecastOfStoreByProductForWeek.get(record.getProductId());
					int weekListIndex = getIndexOfWeekList(firstDayOfWeek, endDayOfWeek, record.getCreateTime());
					if(weekListIndex != -1){
						int sum = record.getSalesNumber() + tempWeekList.get(weekList.get(weekListIndex));
						tempWeekList.put(weekList.get(weekListIndex), sum);
					}
					salesRecordsMapForForecastOfStoreByProductForWeek.put(record.getProductId(), tempWeekList);
				}else {
					LinkedHashMap<String, Integer> tempWeekList = new LinkedHashMap<String, Integer>();
					for(int i = 0; i < 5; i++){
						tempWeekList.put(weekList.get(i), new Integer(0));
					}
					int weekListIndex = getIndexOfWeekList(firstDayOfWeek, endDayOfWeek, record.getCreateTime());
					if(weekListIndex != -1){
						int sum = record.getSalesNumber();
						tempWeekList.put(weekList.get(weekListIndex), sum);
					}
					salesRecordsMapForForecastOfStoreByProductForWeek.put(record.getProductId(), tempWeekList);
				}

			}
		}
    }

    private int getIndexOfWeekList(ArrayList<Date> firstDayOfWeekList, ArrayList<Date> lastDayOfWeekList, Date date){
    	date = clearTimePart(date);
    	if((date.after(firstDayOfWeekList.get(0)) && date.before(lastDayOfWeekList.get(0)))
    			|| date.equals(firstDayOfWeekList.get(0)) || date.equals(lastDayOfWeekList.get(0))){
    		return 0;
    	}
    	if((date.after(firstDayOfWeekList.get(1)) && date.before(lastDayOfWeekList.get(1)))
    			|| date.equals(firstDayOfWeekList.get(1)) || date.equals(lastDayOfWeekList.get(1))){
    		return 1;
    	}
    	if((date.after(firstDayOfWeekList.get(2)) && date.before(lastDayOfWeekList.get(2)))
    			|| date.equals(firstDayOfWeekList.get(2)) || date.equals(lastDayOfWeekList.get(2))){
    		return 2;
    	}
    	if((date.after(firstDayOfWeekList.get(3)) && date.before(lastDayOfWeekList.get(3)))
    			|| date.equals(firstDayOfWeekList.get(3)) || date.equals(lastDayOfWeekList.get(3))){
    		return 3;
    	}
    	if((date.after(firstDayOfWeekList.get(4)) && date.before(lastDayOfWeekList.get(4)))
    			|| date.equals(firstDayOfWeekList.get(4)) || date.equals(lastDayOfWeekList.get(4))){
    		return 4;
    	}
    	return -1;
    }

    private Date clearTimePart(Date date){
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
    	calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY , 0);
        return calendar.getTime();
    }

	public List<ReplenishmentReportItem> calculateReplenishmentForEveryWeek(){
		List<ReplenishmentReportItem> items = new ArrayList<ReplenishmentReportItem>();
		StoreDao storeDao = new StoreDao();
		List<StoreSellingItem> tempProducts = storeDao.queryStoreSellingItemsByStoreId(store.getStoreId());
		List<StoreSellingItem> products = new ArrayList<StoreSellingItem>();
		for(StoreSellingItem item : tempProducts){
			if(item.getRegionListItem()!=null && item.getRegionListItem().getProduct().getDeliveryFrequency().equals(Product.EVERYWEEK)){
				products.add(item);
			}
		}

		for(StoreSellingItem item : products){
			String productId = item.getProductId();
			Integer currentInventory = item.getCurrentInventory();
			Integer inventoryOnNext = item.getRegionListItem().getProduct().getMinInventory();

			Integer salesForecast = averageNumOfWeek(salesRecordsMapForForecastOfStoreByProductForWeek.get(item.getProductId()));
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

	private int averageNumOfWeek(LinkedHashMap<String, Integer> map){
		int sum = 0;
		Iterator<String> iterator = map.keySet().iterator();
		while(iterator.hasNext()){
			String key = iterator.next();
			sum = map.get(key) + sum;
		}
		return sum/5;
	}

	public void initSalesRecordsMapForForecastOfStoreByProductForMonth(){
		ArrayList<String> weekList = new ArrayList<String>();
		ArrayList<Date> firstDayOfWeek = new ArrayList<Date>();
		ArrayList<Date> endDayOfWeek = new ArrayList<Date>();

		Date today = new Date();
		for(int i = 34; i >=0 ; i --){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(today);
			calendar.add(Calendar.DATE, -1 * i);

			int modNum = i % 7;
			if(modNum == 6){
				Calendar calendar2 = Calendar.getInstance();
				calendar2.setTime(calendar.getTime());
				int firstDay = calendar2.get(Calendar.DAY_OF_MONTH);
				int firstMonth = calendar2.get(Calendar.MONTH);
				firstDayOfWeek.add(calendar2.getTime());

				calendar2.add(Calendar.DATE, 6);
				int lastDay = calendar2.get(Calendar.DAY_OF_MONTH);
				int lastMonth = calendar2.get(Calendar.MONTH);
				endDayOfWeek.add(calendar2.getTime());

				weekList.add(Integer.toString(firstMonth+1) + "." + Integer.toString(firstDay) + "-" + Integer.toString(lastMonth+1) + "." + Integer.toString(lastDay));
			}
		}

		StoreDao storeDao = new StoreDao();
		List<StoreSellingItem> tempProducts = storeDao.queryStoreSellingItemsByStoreId(store.getStoreId());
		List<StoreSellingItem> products = new ArrayList<StoreSellingItem>();
		for(StoreSellingItem item : tempProducts){
			if(item.getRegionListItem()!=null && item.getRegionListItem().getProduct().getDeliveryFrequency().equals(Product.EVERYWEEK)){
				products.add(item);
			}
		}

		ArrayList<String> productIdList = new ArrayList<String>();
		for(StoreSellingItem item : products){
			productIdList.add(item.getProductId());
		}

		List<SalesRecord> records = salesRecordsMapForForecast.get(store.getStoreId());
		for(SalesRecord record : records){
			if(productIdList.contains(record.getProductId())){

			}
		}

	}

	public List<ReplenishmentReportItem> calculateReplenishmentForEveryMonth(){
		List<ReplenishmentReportItem> items = new ArrayList<ReplenishmentReportItem>();
		StoreDao storeDao = new StoreDao();
		List<StoreSellingItem> tempProducts = storeDao.queryStoreSellingItemsByStoreId(store.getStoreId());
		List<StoreSellingItem> products = new ArrayList<StoreSellingItem>();
		for(StoreSellingItem item : tempProducts){
			if(item.getRegionListItem()!=null && item.getRegionListItem().getProduct().getDeliveryFrequency().equals(Product.EVERYWEEK)){
				products.add(item);
			}
		}


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
	public HashMap<String, LinkedHashMap<String, Integer>> getSalesRecordsMapForForecastOfStoreByProductForWeek() {
		return salesRecordsMapForForecastOfStoreByProductForWeek;
	}
	public void setSalesRecordsMapForForecastOfStoreByProductForWeek(
			HashMap<String, LinkedHashMap<String, Integer>> salesRecordsMapForForecastOfStoreByProductForWeek) {
		this.salesRecordsMapForForecastOfStoreByProductForWeek = salesRecordsMapForForecastOfStoreByProductForWeek;
	}
	public HashMap<String, LinkedHashMap<String, Integer>> getSalesRecordsMapForForecastOfStoreByProductForMonth() {
		return salesRecordsMapForForecastOfStoreByProductForMonth;
	}
	public void setSalesRecordsMapForForecastOfStoreByProductForMonth(
			HashMap<String, LinkedHashMap<String, Integer>> salesRecordsMapForForecastOfStoreByProductForMonth) {
		this.salesRecordsMapForForecastOfStoreByProductForMonth = salesRecordsMapForForecastOfStoreByProductForMonth;
	}
}
