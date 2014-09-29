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
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import merchandise.utils.CategoryUtil;
import model.Region;
import model.RegionListCategory;
import model.SalesRecord;
import model.StockOutVirtualSales;
import model.Store;
import model.StoreSellingItem;
import model.WasteReason;
import model.WasteRecord;

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
import dao.StockOutVirtualSalesDao;
import dao.StoreDao;
import dao.WasteRecordDao;

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
	private StockOutVirtualSalesDao stockOutVirtualSalesDao;
	private WasteRecordDao wasteRecordDao;

	private Store currentStore;
	private Region currentRegion;

	private TreeNode rootNode;
	private TreeNode selectedNode;

	private List<StoreSellingItem> storeSellingItems;
	private StoreSellingItem selectedRegionListItem;
	private List<StoreSellingItem> selectedItems;
	private List<StoreSellingItem> filteredItems;
	private SelectedItemRecord selectedItemRecord;

	private StoreSellingItem selectedItem;

	private Integer vitualSales;
	private LineChartModel weekdayAverSalesModel;
	private Date startDate;
	private Date endDate;

	private Integer wasteVolume;
	private String wasteReasonId;
	@PostConstruct
	public void init(){
		storeDao = new StoreDao();
		regionDao = new RegionDao();
		categoryDao = new RegionListCategoryDao();
		salesRecordDao = new SalesRecordDao();
		stockOutVirtualSalesDao = new StockOutVirtualSalesDao();
		wasteRecordDao = new WasteRecordDao();
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
		selectedItemRecord = new SelectedItemRecord();
		selectedItemRecord.setSalesRecords(analysisUtil.querySalesRecords(currentStore.getStoreId(), selectedItem.getProductId()));
		selectedItemRecord.setStockOutVirtualSalesRecords(analysisUtil.queryVirtualSalesRecords(currentStore.getStoreId(), selectedItem.getProductId()));
		selectedItemRecord.setWasteRecords(analysisUtil.queryWasteRecords(currentStore.getStoreId(), selectedItem.getProductId()));
		RequestContext.getCurrentInstance().execute("PF('viewProduct').show();");
	}
	//Open Virtual Sales
	public void openAddVirtualSales(){
		selectedItem.setStockoutDuration(DateUtil.formatDuring(new Date(), selectedItem.getStockoutOccurrenceTime()));
		endDate = DateUtil.minusDay(selectedItem.getStockoutOccurrenceTime(), 1);
		startDate = DateUtil.minusMonth(endDate, 3);
		refreshSalesStatistics();
		RequestContext.getCurrentInstance().execute("PF('addVirtualSales').show();");
	}

	public void addVirtualSales(){
		StockOutVirtualSales stockOutVirtualSales = new StockOutVirtualSales();
		stockOutVirtualSales.setCreateTime(new Date());
		stockOutVirtualSales.setProductId(selectedItem.getProductId());
		stockOutVirtualSales.setStockoutNumber(vitualSales);
		stockOutVirtualSales.setStoreId(currentStore.getStoreId());
		stockOutVirtualSalesDao.insertStockOutVirtualSales(stockOutVirtualSales);
		RequestContext.getCurrentInstance().execute("PF('addVirtualSales').hide();");
	}

	public void refreshSalesStatistics(){
			weekdayAverSalesModel = new LineChartModel();
			LinkedHashMap<Integer, Double> weekdayAverSalesMap = analysisUtil.weekdayAverSales(startDate,endDate,currentStore.getStoreId(),selectedItem.getProductId());
	        ChartSeries averageSales = new ChartSeries();
	        averageSales.setLabel("Average Sales Volume");
	        Iterator<Integer> iterator = weekdayAverSalesMap.keySet().iterator();
	        double maxValue = 0;
	        while (iterator.hasNext()) {
	        	Integer currentWeekdayId = iterator.next();
				averageSales.set(DateUtil.getWeekDay(currentWeekdayId), weekdayAverSalesMap.get(currentWeekdayId));
				if(weekdayAverSalesMap.get(currentWeekdayId) > maxValue) maxValue = weekdayAverSalesMap.get(currentWeekdayId);
			}

	        weekdayAverSalesModel.addSeries(averageSales);
	        weekdayAverSalesModel.setTitle("Sales Volume Statistics - " + selectedItem.getRegionListItem().getProduct().getName()
	        		+ " (ProductId:" + selectedItem.getProductId() + ")");
	        weekdayAverSalesModel.setLegendPosition("e");
	        weekdayAverSalesModel.setShowPointLabels(true);
	        weekdayAverSalesModel.getAxes().put(AxisType.X, new CategoryAxis("Weekday"));
	        Axis yAxis  = weekdayAverSalesModel.getAxis(AxisType.Y);
	        yAxis.setLabel("Sales Volume");
	        yAxis.setMin(0);
	}

	public void openAddWasteRecord(){
		RequestContext.getCurrentInstance().execute("PF('addWasteRecord').show();");
	}

	public void addWasteRecord(){
		if(wasteVolume > selectedItem.getCurrentInventory()){
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Notice", "Exceed Current Inventory!");
	        FacesContext.getCurrentInstance().addMessage(null, message);
	        return;
		}
		WasteRecord wasteRecord = new WasteRecord();
		wasteRecord.setCreateTime(new Date());
		wasteRecord.setProductId(selectedItem.getProductId());
		wasteRecord.setStoreId(currentStore.getStoreId());
		wasteRecord.setWasteNumber(wasteVolume);
		WasteReason wasteReason = new WasteReason();
		wasteReason.setReasonId(wasteReasonId);
		wasteRecord.setWasteReason(wasteReason);
		wasteRecordDao.insertWasteRecord(wasteRecord);
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Notice", "Saved Successfully!");
        FacesContext.getCurrentInstance().addMessage(null, message);
		RequestContext.getCurrentInstance().execute("PF('addWasteRecord').hide();");
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

	public Integer getVitualSales() {
		return vitualSales;
	}

	public void setVitualSales(Integer vitualSales) {
		this.vitualSales = vitualSales;
	}

	public SalesRecordDao getSalesRecordDao() {
		return salesRecordDao;
	}

	public void setSalesRecordDao(SalesRecordDao salesRecordDao) {
		this.salesRecordDao = salesRecordDao;
	}

	public SelectedItemRecord getSelectedItemRecord() {
		return selectedItemRecord;
	}

	public void setSelectedItemRecord(SelectedItemRecord selectedItemRecord) {
		this.selectedItemRecord = selectedItemRecord;
	}

	public Integer getWasteVolume() {
		return wasteVolume;
	}

	public void setWasteVolume(Integer wasteVolume) {
		this.wasteVolume = wasteVolume;
	}

}
