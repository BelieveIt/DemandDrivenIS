package inventorybean.store;

import inventorybean.utils.ChartUtil;
import inventorybean.utils.RequestUtil;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;
import org.primefaces.model.chart.LineChartModel;

import utils.DateUtil;
import utils.IdentityUtil;

import dao.DeliveryScheduleDao;
import dao.ReplenishmentReportDao;
import dao.StoreDao;

import model.DeliverySchedule;
import model.Product;
import model.ReplenishmentReport;
import model.ReplenishmentReportItem;
import model.Store;

@ManagedBean(name="storeRequest")
@ViewScoped
public class StoreRequest implements Serializable{
	private static final long serialVersionUID = -5273193227547183374L;
	private ReplenishmentReportDao replenishmentReportDao;
	private StoreDao storeDao;
	private DeliveryScheduleDao deliveryScheduleDao;

	private Store store;
	private ScheduleModel eventModel;

	@ManagedProperty(value="#{requestUtil}")
	private RequestUtil requestUtil;

	private List<ReplenishmentReport> reports;
	private ReplenishmentReport selectedReport;
	private ReplenishmentReport newReplenishmentReport;

	private ReplenishmentReportItem selectedItem;
	private LineChartModel selectedItemSalesLine;

	private Date weekDeliveryDate;
	private Date monthDeliveryDate;

	private Integer daysBeforeWeekDelivery;
	private Integer daysBeforeMonthDelivery;
	@PostConstruct
	public void init(){
		setReplenishmentReportDao(new ReplenishmentReportDao());
		storeDao = new StoreDao();
		deliveryScheduleDao = new DeliveryScheduleDao();
		//TODO
		store = storeDao.queryStoreById("1");
		initProgress();
		initDeliveryDate();
	}
	public void initProgress(){
		setNewReplenishmentReport(new ReplenishmentReport());
		reports = replenishmentReportDao.queryReplenishmentReportsByStoreId(store.getStoreId());
	}
	public void initDeliveryDate(){
		DeliverySchedule weekSchedule = new DeliverySchedule();
		weekSchedule = deliveryScheduleDao.queryDeliverySchedule(Product.EVERYWEEK);
		DeliverySchedule monthSchedule = new DeliverySchedule();
		monthSchedule = deliveryScheduleDao.queryDeliverySchedule(Product.EVERYMONTH);

		Integer weekMark = weekSchedule.getDeliveryMark();
		Integer monthMark = monthSchedule.getDeliveryMark();

		Calendar startC = Calendar.getInstance();

		daysBeforeWeekDelivery = 0;
		startC.setTime(new Date());
		while(true){
			if(startC.get(Calendar.DAY_OF_WEEK) == weekMark.intValue()){
				weekDeliveryDate = startC.getTime();
				break;
			}
			daysBeforeWeekDelivery++;
			startC.add(Calendar.DATE, 1);
		}

		daysBeforeMonthDelivery = 0;
		startC.setTime(new Date());
		while(true){
			if(startC.get(Calendar.DAY_OF_MONTH) == monthMark.intValue()){
				monthDeliveryDate = startC.getTime();
				break;
			}
			daysBeforeMonthDelivery++;
			startC.add(Calendar.DATE, 1);
		}
	}

	public void openViewSchedule(){
		eventModel = new DefaultScheduleModel();
		DeliverySchedule weekSchedule = new DeliverySchedule();
		weekSchedule = deliveryScheduleDao.queryDeliverySchedule(Product.EVERYWEEK);
		DeliverySchedule monthSchedule = new DeliverySchedule();
		monthSchedule = deliveryScheduleDao.queryDeliverySchedule(Product.EVERYMONTH);
        initialEvent(weekSchedule, monthSchedule);
        RequestContext.getCurrentInstance().execute("PF('viewSchedule').show();");
	}

	private void initialEvent(DeliverySchedule weekSchedule, DeliverySchedule monthSchedule){
		try {
		Date today = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		String todayYear = Integer.toString(calendar.get(Calendar.YEAR));
		String todayMonth = Integer.toString(calendar.get(Calendar.MONTH)+1);
		String todayDay = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
		String todayYearNext = Integer.toString(calendar.get(Calendar.YEAR)+1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startInString = todayYear + "-"+todayMonth+"-"+todayDay + " 5:0:0";
		String endInString = todayYearNext + "-01-01 00:00:00";

		Date start = sdf.parse(startInString);
		Date end = sdf.parse(endInString);

		Calendar startC = Calendar.getInstance();
		startC.setTime(start);

		Calendar endC = Calendar.getInstance();
		endC.setTime(end);

		for (; startC.before(endC); startC.add(Calendar.DATE, 1)) {
			if(startC.get(Calendar.DAY_OF_MONTH) == monthSchedule.getDeliveryMark().intValue()){
				startC.set(Calendar.HOUR_OF_DAY, monthSchedule.getDeliveryHour());
				eventModel.addEvent(new DefaultScheduleEvent("Delivery for EveryMonth Frequency", startC.getTime(),  startC.getTime()));
			}

			if(startC.get(Calendar.DAY_OF_WEEK) == weekSchedule.getDeliveryMark().intValue()){
				startC.set(Calendar.HOUR_OF_DAY, weekSchedule.getDeliveryHour());
				eventModel.addEvent(new DefaultScheduleEvent("Delivery for EveryWeek Frequency",  startC.getTime(),  startC.getTime()));
			}

		}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	public void openCreateDayRequest(){
		requestUtil.initSalesRecordsMapForForecastOfStoreByProduct();
		Date today = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		calendar.add(Calendar.DATE, 1);
		Date tomorrow = calendar.getTime();
		newReplenishmentReport.setDeliveryTime(tomorrow);
		newReplenishmentReport.setDeliveryType("everyday");

		List<ReplenishmentReportItem> replenishmentReportItems = new ArrayList<ReplenishmentReportItem>();
		replenishmentReportItems = requestUtil.calculateReplenishmentForEveryDay();
		newReplenishmentReport.setReplenishmentReportItems(replenishmentReportItems);

		newReplenishmentReport.setStoreId(store.getStoreId());
		RequestContext.getCurrentInstance().execute("PF('createRequest').show();");
	}

	public void openCreateWeekRequest(){
		requestUtil.initSalesRecordsMapForForecastOfStoreByProductForWeek();
		newReplenishmentReport.setDeliveryTime(weekDeliveryDate);
		newReplenishmentReport.setDeliveryType("everyweek");

		List<ReplenishmentReportItem> replenishmentReportItems = new ArrayList<ReplenishmentReportItem>();
		replenishmentReportItems = requestUtil.calculateReplenishmentForEveryWeek();
		newReplenishmentReport.setReplenishmentReportItems(replenishmentReportItems);

		newReplenishmentReport.setStoreId(store.getStoreId());
		RequestContext.getCurrentInstance().execute("PF('createRequest').show();");
	}

	public void openCreateMonthRequest(){
		newReplenishmentReport.setDeliveryTime(monthDeliveryDate);

		//TODO
		newReplenishmentReport.setDeliveryType("everymonth");
		List<ReplenishmentReportItem> replenishmentReportItems = new ArrayList<ReplenishmentReportItem>();
		replenishmentReportItems = requestUtil.calculateReplenishmentForEveryMonth();
		newReplenishmentReport.setReplenishmentReportItems(replenishmentReportItems);

		newReplenishmentReport.setStoreId(store.getStoreId());
		RequestContext.getCurrentInstance().execute("PF('createRequest').show();");
	}


	public void openViewItemForAdd(){
		if(newReplenishmentReport.getDeliveryType().equals(Product.EVERYDAY)){
			LinkedHashMap<String, LinkedHashMap<String, Double>> dataMap = new LinkedHashMap<String, LinkedHashMap<String,Double>>();
			HashMap<String, TreeMap<Integer, Integer>> salesRecordsMapForForecastOfStoreByProduct = requestUtil.getSalesRecordsMapForForecastOfStoreByProduct();
			TreeMap<Integer, Integer> dayOfWeekAvgMap = salesRecordsMapForForecastOfStoreByProduct.get(selectedItem.getProductId());
			LinkedHashMap<String, Double> dataMapValueMap = new LinkedHashMap<String, Double>();
			Iterator<Integer> iterator = dayOfWeekAvgMap.keySet().iterator();
			while(iterator.hasNext()){
				Integer key = iterator.next();
				dataMapValueMap.put(DateUtil.getWeekDay(key), new Double(Integer.toString(dayOfWeekAvgMap.get(key))));
			}
			dataMap.put(selectedItem.getRegionListItem().getProduct().getName(), dataMapValueMap);
			selectedItemSalesLine = ChartUtil.generateLineChartModel("Average Sales In Last 90 Days(Real Sales Data + Virtual Sales Data)", "Day of Week", "Sales Volume", dataMap);
			RequestContext.getCurrentInstance().execute("PF('viewItemForAdd').show();");
		}

		if(newReplenishmentReport.getDeliveryType().equals(Product.EVERYWEEK)){
			LinkedHashMap<String, LinkedHashMap<String, Double>> dataMap = new LinkedHashMap<String, LinkedHashMap<String,Double>>();
			HashMap<String, LinkedHashMap<String, Integer>> salesRecordsMapForForecastOfStoreByProductForWeek = requestUtil.getSalesRecordsMapForForecastOfStoreByProductForWeek();
			LinkedHashMap<String, Integer> weekListMap = salesRecordsMapForForecastOfStoreByProductForWeek.get(selectedItem.getProductId());
			LinkedHashMap<String, Double> dataMapValueMap = new LinkedHashMap<String, Double>();
			Iterator<String> iterator = weekListMap.keySet().iterator();
			while(iterator.hasNext()){
				String key = iterator.next();
				dataMapValueMap.put(key, new Double(Integer.toString(weekListMap.get(key))));
			}
			dataMap.put(selectedItem.getRegionListItem().getProduct().getName(), dataMapValueMap);
			selectedItemSalesLine = ChartUtil.generateLineChartModel("Sales Valume In Last 5 Weeks(Real Sales Data + Virtual Sales Data)", "Week", "Sales Volume", dataMap);
			RequestContext.getCurrentInstance().execute("PF('viewItemForAdd').show();");
		}

		if(newReplenishmentReport.getDeliveryType().equals(Product.EVERYMONTH)){

		}

	}

	public void confirmReplenishmentNumber(){
		RequestContext.getCurrentInstance().execute("PF('viewItemForAdd').hide();");
	}

	public void createRequest(){
		newReplenishmentReport.setCreateTime(new Date());
		newReplenishmentReport.setReportId(IdentityUtil.randomUUID());
		newReplenishmentReport.setStatus(ReplenishmentReport.WAITING_FOR_APPROVAL);
		for(ReplenishmentReportItem item : newReplenishmentReport.getReplenishmentReportItems()){
			item.setReportId(newReplenishmentReport.getReportId());
		}
		replenishmentReportDao.insertReplenishmentReport(newReplenishmentReport);
		initProgress();
		RequestContext.getCurrentInstance().execute("PF('createRequest').hide();");
	}

	public void openViewItem(){
		LinkedHashMap<String, LinkedHashMap<String, Double>> dataMap = new LinkedHashMap<String, LinkedHashMap<String,Double>>();
		HashMap<String, TreeMap<Integer, Integer>> salesRecordsMapForForecastOfStoreByProduct = requestUtil.getSalesRecordsMapForForecastOfStoreByProduct();
		TreeMap<Integer, Integer> dayOfWeekAvgMap = salesRecordsMapForForecastOfStoreByProduct.get(selectedItem.getProductId());
		LinkedHashMap<String, Double> dataMapValueMap = new LinkedHashMap<String, Double>();
		Iterator<Integer> iterator = dayOfWeekAvgMap.keySet().iterator();
		while(iterator.hasNext()){
			Integer key = iterator.next();
			dataMapValueMap.put(DateUtil.getWeekDay(key), new Double(Integer.toString(dayOfWeekAvgMap.get(key))));
		}
		dataMap.put(selectedItem.getRegionListItem().getProduct().getName(), dataMapValueMap);
		selectedItemSalesLine = ChartUtil.generateLineChartModel("Average Sales In Last 90 Days(Real Sales Data + Virtual Sales Data)", "Day of Week", "Sales Volume", dataMap);
		RequestContext.getCurrentInstance().execute("PF('viewItem').show();");
	}

	public void openViewReport(){
		RequestContext.getCurrentInstance().execute("PF('viewReport').show();");
	}

	public void openRejectedReason(){
		RequestContext.getCurrentInstance().execute("PF('rejectedReason').show();");
	}
	public List<ReplenishmentReport> getReports() {
		return reports;
	}
	public void setReports(List<ReplenishmentReport> reports) {
		this.reports = reports;
	}
	public ReplenishmentReport getSelectedReport() {
		return selectedReport;
	}
	public void setSelectedReport(ReplenishmentReport selectedReport) {
		this.selectedReport = selectedReport;
	}
	public ReplenishmentReport getNewReplenishmentReport() {
		return newReplenishmentReport;
	}
	public void setNewReplenishmentReport(ReplenishmentReport newReplenishmentReport) {
		this.newReplenishmentReport = newReplenishmentReport;
	}
	public Store getStore() {
		return store;
	}
	public void setStore(Store store) {
		this.store = store;
	}
	public RequestUtil getRequestUtil() {
		return requestUtil;
	}
	public void setRequestUtil(RequestUtil requestUtil) {
		this.requestUtil = requestUtil;
	}
	public ReplenishmentReportDao getReplenishmentReportDao() {
		return replenishmentReportDao;
	}
	public void setReplenishmentReportDao(ReplenishmentReportDao replenishmentReportDao) {
		this.replenishmentReportDao = replenishmentReportDao;
	}
	public ReplenishmentReportItem getSelectedItem() {
		return selectedItem;
	}
	public void setSelectedItem(ReplenishmentReportItem selectedItem) {
		this.selectedItem = selectedItem;
	}
	public LineChartModel getSelectedItemSalesLine() {
		return selectedItemSalesLine;
	}
	public void setSelectedItemSalesLine(LineChartModel selectedItemSalesLine) {
		this.selectedItemSalesLine = selectedItemSalesLine;
	}
	public DeliveryScheduleDao getDeliveryScheduleDao() {
		return deliveryScheduleDao;
	}
	public void setDeliveryScheduleDao(DeliveryScheduleDao deliveryScheduleDao) {
		this.deliveryScheduleDao = deliveryScheduleDao;
	}
	public Date getWeekDeliveryDate() {
		return weekDeliveryDate;
	}
	public void setWeekDeliveryDate(Date weekDeliveryDate) {
		this.weekDeliveryDate = weekDeliveryDate;
	}
	public Date getMonthDeliveryDate() {
		return monthDeliveryDate;
	}
	public void setMonthDeliveryDate(Date monthDeliveryDate) {
		this.monthDeliveryDate = monthDeliveryDate;
	}
	public Integer getDaysBeforeWeekDelivery() {
		return daysBeforeWeekDelivery;
	}
	public void setDaysBeforeWeekDelivery(Integer daysBeforeWeekDelivery) {
		this.daysBeforeWeekDelivery = daysBeforeWeekDelivery;
	}
	public Integer getDaysBeforeMonthDelivery() {
		return daysBeforeMonthDelivery;
	}
	public void setDaysBeforeMonthDelivery(Integer daysBeforeMonthDelivery) {
		this.daysBeforeMonthDelivery = daysBeforeMonthDelivery;
	}

}
