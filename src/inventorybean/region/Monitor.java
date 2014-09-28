package inventorybean.region;

import inventorybean.utils.AnalysisUtil;
import inventorybean.utils.MonitorUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import merchandise.utils.CategoryUtil;
import model.Region;
import model.RegionListCategory;
import model.SalesRecord;
import model.Store;
import model.StoreSellingItem;

import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.TreeNode;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

import utils.DateUtil;

import dao.RegionDao;
import dao.RegionListCategoryDao;
import dao.SalesRecordDao;
import dao.StoreDao;

@ManagedBean(name="monitor")
@ViewScoped
public class Monitor implements Serializable{
	private static final long serialVersionUID = -4596394594859307482L;
	@ManagedProperty(value="#{analysisUtil}")
	private AnalysisUtil analysisUtil;

	private StoreDao storeDao;
	private RegionDao regionDao;
	private RegionListCategoryDao categoryDao;
	private SalesRecordDao salesRecordDao;

	private Store currentStore;
	private Region currentRegion;

	private TreeNode rootNode;
	private TreeNode selectedNode;

	private List<StoreSellingItem> storeSellingItems;
	private StoreSellingItem selectedRegionListItem;
	private List<StoreSellingItem> selectedItems;
	private List<StoreSellingItem> filteredItems;

	private StoreSellingItem selectedItem;
	
	private LineChartModel weekdayAverSalesModel;
	private Date startDate;
	private Date endDate;
	@PostConstruct
	public void init(){
		storeDao = new StoreDao();
		regionDao = new RegionDao();
		categoryDao = new RegionListCategoryDao();
		salesRecordDao = new SalesRecordDao();
		//TODO
		String storeId = "1";
		currentStore = storeDao.queryStoreById(storeId);
		currentRegion = regionDao.queryRegionById(currentStore.getRegionId());

		List<RegionListCategory> categories = categoryDao.queryCategoriesByVersionId("head", currentRegion.getRegionId());
		rootNode = CategoryUtil.generateTreeForProduct(categories);
		if(rootNode !=null){
			CategoryUtil.expandAllTree(rootNode);
			rootNode.getChildren().get(0).setSelected(true);
			selectedNode = rootNode.getChildren().get(0);
		}
		setItemsBySelectedNode();
		
		//TODO
		startDate=new Date();
		endDate=new Date();
	}

	private void setItemsBySelectedNode(){
	        storeSellingItems = MonitorUtil.generateStoreSellingItemsBySelectedNode(selectedNode, currentStore);
	}

	public void onNodeSelect(NodeSelectEvent event) {
        selectedNode = event.getTreeNode();
        setItemsBySelectedNode();
        initTable();
    }

	//View Product
	public void openViewProduct(){
		RequestContext.getCurrentInstance().execute("PF('viewProduct').show();");
	}
	//Open Virtual Sales
	public void openAddVirtualSales(){
		selectedItem.setStockoutDuration(DateUtil.formatDuring(new Date(), selectedItem.getStockoutOccurrenceTime()));
		HashMap<String, ArrayList<SalesRecord>> map = salesRecordDao.querySalesRecords();
		Date date1 = map.get("1").get(0).getCreateTime();
		Date date2 = map.get("1").get(2).getCreateTime();
		analysisUtil.weekdayAverSales(date2, date1, currentStore.getStoreId(), selectedItem.getProductId());
		endDate = DateUtil.minusDay(selectedItem.getStockoutOccurrenceTime(), 1);
		startDate = DateUtil.minusMonth(endDate, 3);
		refreshSalesStatistics();
		RequestContext.getCurrentInstance().execute("PF('addVirtualSales').show();");
	}
	public void setVirtualSales(){
		RequestContext.getCurrentInstance().execute("PF('addVirtualSales').hide();");
	}

	public void refreshSalesStatistics(){
			weekdayAverSalesModel = new LineChartModel();
			
			LinkedHashMap<Integer, Double> weekdayAverSalesMap = analysisUtil.weekdayAverSales(startDate,endDate,currentStore.getStoreId(),selectedItem.getProductId());
	        ChartSeries averageSales = new ChartSeries();
	        averageSales.setLabel("Average Sales Volume");
	        Iterator<Integer> iterator = weekdayAverSalesMap.keySet().iterator();
	        while (iterator.hasNext()) {
	        	Integer currentWeekdayId = iterator.next();
				averageSales.set(DateUtil.getWeekDay(currentWeekdayId), weekdayAverSalesMap.get(currentWeekdayId));
				System.out.println(DateUtil.getWeekDay(currentWeekdayId) + " " +weekdayAverSalesMap.get(currentWeekdayId));
			}
	       
			
	        weekdayAverSalesModel.addSeries(averageSales);
	        weekdayAverSalesModel.setTitle("Sales Volume Statistics - " + selectedItem.getRegionListItem().getProduct().getName() 
	        		+ "(" + selectedItem.getProductId() + ")");
	        weekdayAverSalesModel.setLegendPosition("e");
	        weekdayAverSalesModel.setShowPointLabels(true);
	        weekdayAverSalesModel.getAxes().put(AxisType.X, new CategoryAxis("Weekday"));
	        Axis yAxis  = weekdayAverSalesModel.getAxis(AxisType.Y);
	        yAxis.setLabel("Sales Volume");
	        yAxis.setMin(0);
	        yAxis.setMax(200);	
	}
	
	private void initTable(){
		selectedItems = null;
		filteredItems = null;
	}
	public TreeNode getRootNode() {
		return rootNode;
	}
	public void setRootNode(TreeNode rootNode) {
		this.rootNode = rootNode;
	}
	public TreeNode getSelectedNode() {
		return selectedNode;
	}
	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}

	public RegionListCategoryDao getCategoryDao() {
		return categoryDao;
	}
	public void setCategoryDao(RegionListCategoryDao categoryDao) {
		this.categoryDao = categoryDao;
	}

	public Region getCurrentRegion() {
		return currentRegion;
	}
	public void setCurrentRegion(Region currentRegion) {
		this.currentRegion = currentRegion;
	}

	public StoreDao getStoreDao() {
		return storeDao;
	}
	public void setStoreDao(StoreDao storeDao) {
		this.storeDao = storeDao;
	}
	public RegionDao getRegionDao() {
		return regionDao;
	}
	public void setRegionDao(RegionDao regionDao) {
		this.regionDao = regionDao;
	}
	public Store getCurrentStore() {
		return currentStore;
	}
	public void setCurrentStore(Store currentStore) {
		this.currentStore = currentStore;
	}
	public List<StoreSellingItem> getStoreSellingItems() {
		return storeSellingItems;
	}
	public void setStoreSellingItems(List<StoreSellingItem> storeSellingItems) {
		this.storeSellingItems = storeSellingItems;
	}
	public StoreSellingItem getSelectedRegionListItem() {
		return selectedRegionListItem;
	}
	public void setSelectedRegionListItem(StoreSellingItem selectedRegionListItem) {
		this.selectedRegionListItem = selectedRegionListItem;
	}
	public List<StoreSellingItem> getSelectedItems() {
		return selectedItems;
	}
	public void setSelectedItems(List<StoreSellingItem> selectedItems) {
		this.selectedItems = selectedItems;
	}
	public List<StoreSellingItem> getFilteredItems() {
		return filteredItems;
	}
	public void setFilteredItems(List<StoreSellingItem> filteredItems) {
		this.filteredItems = filteredItems;
	}

	public StoreSellingItem getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(StoreSellingItem selectedItem) {
		this.selectedItem = selectedItem;
	}

	public AnalysisUtil getAnalysisUtil() {
		return analysisUtil;
	}

	public void setAnalysisUtil(AnalysisUtil analysisUtil) {
		this.analysisUtil = analysisUtil;
	}


	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public LineChartModel getWeekdayAverSalesModel() {
		return weekdayAverSalesModel;
	}

	public void setWeekdayAverSalesModel(LineChartModel weekdayAverSalesModel) {
		this.weekdayAverSalesModel = weekdayAverSalesModel;
	}

}
