package inventorybean.region;

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

import model.DeliveryReport;
import model.DeliveryReportItem;
import model.Product;
import model.ReplenishmentReport;
import model.ReplenishmentReportItem;
import model.Store;

import dao.DeliveryReportDao;
import dao.ReplenishmentReportDao;
import dao.StoreDao;

@ManagedBean(name="regionViewRequest")
@ViewScoped
public class RegionViewRequest implements Serializable{
	private static final long serialVersionUID = -8990520541780137709L;
	private ReplenishmentReportDao replenishmentReportDao;
	private StoreDao storeDao;
	private DeliveryReportDao deliveryReportDao;

	@ManagedProperty(value="#{requestUtil}")
	private RequestUtil requestUtil;

	private List<ReplenishmentReport> reports;
	private ReplenishmentReport selectedReport;

	private ReplenishmentReportItem selectedItem;
	private LineChartModel selectedItemSalesLine;

	private Integer waitingNum;
	@PostConstruct
	public void init(){
		replenishmentReportDao = new ReplenishmentReportDao();
		deliveryReportDao = new DeliveryReportDao();
		storeDao = new StoreDao();
		initProcess();
	}
	public void initProcess(){
		reports = replenishmentReportDao.queryReplenishmentReports();
		if(reports != null){
			for(ReplenishmentReport report : reports){
				Store store = storeDao.queryStoreById(report.getStoreId());
				report.setStore(store);
			}
		}

		waitingNum = countWaitingNum(reports);
	}

	private Integer countWaitingNum(List<ReplenishmentReport> list){
		int count = 0 ;
		for(ReplenishmentReport report : list){
			if(report.getStatus().equals(ReplenishmentReport.WAITING_FOR_APPROVAL)){
				count++;
			}
		}
		if(count == 0) return null;
		return count;
	}

	public void openViewItem(){
		if(selectedReport.getDeliveryType().equals(Product.EVERYDAY)){
			requestUtil.initSalesRecordsMapForForecastOfStoreByProduct();
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
		if(selectedReport.getDeliveryType().equals(Product.EVERYWEEK)){
			requestUtil.initSalesRecordsMapForForecastOfStoreByProductForWeek();
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
			RequestContext.getCurrentInstance().execute("PF('viewItem').show();");
		}

		if(selectedReport.getDeliveryType().equals(Product.EVERYMONTH)){
			requestUtil.initSalesRecordsMapForForecastOfStoreByProductForMonth();
			LinkedHashMap<String, LinkedHashMap<String, Double>> dataMap = new LinkedHashMap<String, LinkedHashMap<String,Double>>();
			HashMap<String, LinkedHashMap<String, Integer>> salesRecordsMapForForecastOfStoreByProductForMonth = requestUtil.getSalesRecordsMapForForecastOfStoreByProductForMonth();
			LinkedHashMap<String, Integer> weekListMap = salesRecordsMapForForecastOfStoreByProductForMonth.get(selectedItem.getProductId());
			LinkedHashMap<String, Double> dataMapValueMap = new LinkedHashMap<String, Double>();
			Iterator<String> iterator = weekListMap.keySet().iterator();
			while(iterator.hasNext()){
				String key = iterator.next();
				dataMapValueMap.put(key, new Double(Integer.toString(weekListMap.get(key))));
			}
			dataMap.put(selectedItem.getRegionListItem().getProduct().getName(), dataMapValueMap);
			selectedItemSalesLine = ChartUtil.generateLineChartModel("Sales Valume In Last 5 Months(Real Sales Data + Virtual Sales Data)", "Month", "Sales Volume", dataMap);
			RequestContext.getCurrentInstance().execute("PF('viewItem').show();");
		}
	}
	public void openViewReport(){
		if(selectedReport.getDeliveryType().equals("everyday")){
			requestUtil.initSalesRecordsMapForForecastOfStoreByProduct();
			List<ReplenishmentReportItem> replenishmentReportItems = new ArrayList<ReplenishmentReportItem>();
			replenishmentReportItems = requestUtil.calculateReplenishmentForEveryDay();
			for(ReplenishmentReportItem item : selectedReport.getReplenishmentReportItems()){
				for(ReplenishmentReportItem item2 : replenishmentReportItems){
					if(item2.getProductId().equals(item.getProductId())){
						item.setAutoCalculatedReplenishmentNumber(item2.getAutoCalculatedReplenishmentNumber());
					}
				}
			}
			RequestContext.getCurrentInstance().execute("PF('viewReport').show();");	
		}

		if(selectedReport.getDeliveryType().equals("everyweek")){
			requestUtil.initSalesRecordsMapForForecastOfStoreByProductForWeek();
			List<ReplenishmentReportItem> replenishmentReportItems = new ArrayList<ReplenishmentReportItem>();
			replenishmentReportItems = requestUtil.calculateReplenishmentForEveryWeek();
			for(ReplenishmentReportItem item : selectedReport.getReplenishmentReportItems()){
				for(ReplenishmentReportItem item2 : replenishmentReportItems){
					if(item2.getProductId().equals(item.getProductId())){
						item.setAutoCalculatedReplenishmentNumber(item2.getAutoCalculatedReplenishmentNumber());
					}
				}
			}
			RequestContext.getCurrentInstance().execute("PF('viewReport').show();");	
		}

		if(selectedReport.getDeliveryType().equals("everymonth")){
			requestUtil.initSalesRecordsMapForForecastOfStoreByProductForMonth();
			List<ReplenishmentReportItem> replenishmentReportItems = new ArrayList<ReplenishmentReportItem>();
			replenishmentReportItems = requestUtil.calculateReplenishmentForEveryMonth();
			for(ReplenishmentReportItem item : selectedReport.getReplenishmentReportItems()){
				for(ReplenishmentReportItem item2 : replenishmentReportItems){
					if(item2.getProductId().equals(item.getProductId())){
						item.setAutoCalculatedReplenishmentNumber(item2.getAutoCalculatedReplenishmentNumber());
					}
				}
			}
			RequestContext.getCurrentInstance().execute("PF('viewReport').show();");	
		}
	}

	public void openApprove(){
		RequestContext.getCurrentInstance().execute("PF('approve').show();");
	}

	public void approve(){
		selectedReport.setStatus(ReplenishmentReport.APPROVED);
		replenishmentReportDao.updateReplenishmentReport(selectedReport);
		DeliveryReport deliveryReport = new DeliveryReport();
		deliveryReport.setCreateTime(new Date());
		deliveryReport.setDeliveryTime(selectedReport.getDeliveryTime());
		deliveryReport.setReportId(IdentityUtil.randomUUID());
		deliveryReport.setStoreId(selectedReport.getStoreId());

		ArrayList<DeliveryReportItem> deliveryReportItems = new ArrayList<DeliveryReportItem>();
		List<ReplenishmentReportItem> list = selectedReport.getReplenishmentReportItems();
		for(ReplenishmentReportItem item : list){
			DeliveryReportItem deliveryReportItem = new DeliveryReportItem();
			deliveryReportItem.setDeliveryNumber(item.getReplenishmentNumber());
			deliveryReportItem.setProductId(item.getProductId());
			deliveryReportItem.setReportId(deliveryReport.getReportId());
			deliveryReportItems.add(deliveryReportItem);
		}
		deliveryReport.setDeliveryReportItems(deliveryReportItems);
		deliveryReportDao.insertDeliverReport(deliveryReport);
		initProcess();
		RequestContext.getCurrentInstance().execute("PF('approve').hide();");
	}
	public void approveCancel(){
		RequestContext.getCurrentInstance().execute("PF('approve').hide();");
	}

	public void openReject(){
		RequestContext.getCurrentInstance().execute("PF('reject').show();");
	}

	public void reject(){
		selectedReport.setStatus(ReplenishmentReport.REJECTED);
		replenishmentReportDao.updateReplenishmentReport(selectedReport);
		initProcess();
		RequestContext.getCurrentInstance().execute("PF('reject').hide();");
	}
	public void rejectCancel(){
		RequestContext.getCurrentInstance().execute("PF('reject').hide();");
	}

	public List<ReplenishmentReport> getReports() {
		return reports;
	}
	public void setReports(List<ReplenishmentReport> reports) {
		this.reports = reports;
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
	public ReplenishmentReport getSelectedReport() {
		return selectedReport;
	}
	public void setSelectedReport(ReplenishmentReport selectedReport) {
		this.selectedReport = selectedReport;
	}

	public Integer getWaitingNum() {
		return waitingNum;
	}

	public void setWaitingNum(Integer waitingNum) {
		this.waitingNum = waitingNum;
	}

	public RequestUtil getRequestUtil() {
		return requestUtil;
	}
	public void setRequestUtil(RequestUtil requestUtil) {
		this.requestUtil = requestUtil;
	}
}
