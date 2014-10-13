package inventorybean.store;

import inventorybean.utils.ChartUtil;
import inventorybean.utils.RequestUtil;

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
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.model.chart.LineChartModel;

import utils.DateUtil;
import utils.IdentityUtil;

import dao.ReplenishmentReportDao;
import dao.StoreDao;

import model.ReplenishmentReport;
import model.ReplenishmentReportItem;
import model.Store;

@ManagedBean(name="storeRequest")
@ViewScoped
public class StoreRequest implements Serializable{
	private static final long serialVersionUID = -5273193227547183374L;
	private ReplenishmentReportDao replenishmentReportDao;
	private StoreDao storeDao;

	private Store store;

	@ManagedProperty(value="#{requestUtil}")
	private RequestUtil requestUtil;

	private List<ReplenishmentReport> reports;
	private ReplenishmentReport selectedReport;
	private ReplenishmentReport newReplenishmentReport;

	private ReplenishmentReportItem selectedItem;
	private LineChartModel selectedItemSalesLine;
	@PostConstruct
	public void init(){
		setReplenishmentReportDao(new ReplenishmentReportDao());
		storeDao = new StoreDao();
		//TODO
		store = storeDao.queryStoreById("1");
		initProgress();
	}
	public void initProgress(){
		setNewReplenishmentReport(new ReplenishmentReport());
		reports = replenishmentReportDao.queryReplenishmentReportsByStoreId(store.getStoreId());
	}

	public void openCreateRequest(){
		RequestContext.getCurrentInstance().execute("PF('createRequest').show();");
	}
	public void openCreateRequest2(){
		Date today = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		calendar.add(Calendar.DATE, 1);
		Date tomorrow = calendar.getTime();
		newReplenishmentReport.setDeliveryTime(tomorrow);

		List<ReplenishmentReportItem> replenishmentReportItems = new ArrayList<ReplenishmentReportItem>();
		if(newReplenishmentReport.getDeliveryType().equals("everyday"))replenishmentReportItems = requestUtil.calculateReplenishmentForEveryDay();
		if(newReplenishmentReport.getDeliveryType().equals("everyweek"))replenishmentReportItems = requestUtil.calculateReplenishmentForEveryDay();
		if(newReplenishmentReport.getDeliveryType().equals("everymonth"))replenishmentReportItems = requestUtil.calculateReplenishmentForEveryDay();
		newReplenishmentReport.setReplenishmentReportItems(replenishmentReportItems);

		newReplenishmentReport.setStoreId(store.getStoreId());
		RequestContext.getCurrentInstance().execute("PF('createRequest').hide();");
		RequestContext.getCurrentInstance().execute("PF('createRequest2').show();");
	}

	public void openViewItemForAdd(){
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

	public void confirmReplenishmentNumber(){
		RequestContext.getCurrentInstance().execute("PF('viewItemForAdd').hide();");
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

	public void createRequest(){
		newReplenishmentReport.setCreateTime(new Date());
		newReplenishmentReport.setReportId(IdentityUtil.randomUUID());
		newReplenishmentReport.setStatus(ReplenishmentReport.WAITING_FOR_APPROVAL);
		for(ReplenishmentReportItem item : newReplenishmentReport.getReplenishmentReportItems()){
			item.setReportId(newReplenishmentReport.getReportId());
		}
		replenishmentReportDao.insertReplenishmentReport(newReplenishmentReport);
		initProgress();
		RequestContext.getCurrentInstance().execute("PF('createRequest2').hide();");
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

}
