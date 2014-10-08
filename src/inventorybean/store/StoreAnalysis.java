package inventorybean.store;

import inventorybean.utils.AnalysisUtil;
import inventorybean.utils.ChartUtil;
import inventorybean.utils.MonitorUtil;
import inventorybean.utils.SalesVolumeLowMoving;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.TreeNode;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.PieChartModel;

import utils.DateUtil;
import utils.LeastSquareUtil;

import dao.RegionDao;
import dao.RegionListCategoryDao;
import dao.SalesRecordDao;
import dao.StoreDao;

import merchandise.utils.CategoryUtil;
import model.Category;
import model.Region;
import model.RegionListCategory;
import model.Store;
import model.StoreSellingItem;

@ManagedBean(name="storeAnalysis")
@ViewScoped
public class StoreAnalysis implements Serializable{
	private static final long serialVersionUID = 5265661455745070133L;
	@ManagedProperty(value="#{analysisUtil}")
	private AnalysisUtil analysisUtil;
//analysisOfRegion
	private StoreDao storeDao;
	private RegionDao regionDao;
	private RegionListCategoryDao categoryDao;
	private SalesRecordDao salesRecordDao;


	private Store currentStore;
	private Region currentRegion;

	private TreeNode rootNode;
	private TreeNode selectedNode;

	private List<StoreSellingItem> storeSellingItems;
	private List<StoreSellingItem> selectedItems;
	private List<StoreSellingItem> filteredItems;

	private StoreSellingItem selectedItem;

	private String analysisType;

	private String currentYear;
	private Integer currentNum;
	private LinkedHashMap<String, String> years;

	private PieChartModel salesShareForCategoryIncludeCategory;
	private LineChartModel salesTrendForCategoryIncludeCategory;
	private ArrayList<SalesVolumeLowMoving> salesLowMovingForCategory;


	private String productAnalysisType;
	private boolean isForecast;
	private LineChartModel salesTrendForCategoryIncludeProduct;

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
			if(rootNode.getChildCount() > 1)rootNode.getChildren().get(1).setExpanded(true);
			rootNode.getChildren().get(0).setSelected(true);
			selectedNode = rootNode.getChildren().get(0);
		}
		setItemsBySelectedNode();
		initChartsForSelectedNode();
		if(years != null){
			years = analysisUtil.queryYearsOfStores(currentStore.getStoreId());
			currentYear = years.keySet().iterator().next();
		}

		currentNum = 10;

		productAnalysisType = "forYear";
		isForecast = false;
	}

	private void setItemsBySelectedNode(){
        storeSellingItems = MonitorUtil.generateStoreSellingItemsBySelectedNode(selectedNode, currentStore);
}
	public void onNodeSelect(NodeSelectEvent event) {
        selectedNode = event.getTreeNode();
        setItemsBySelectedNode();
        initTable();
        initChartsForSelectedNode();
    }

	private void initChartsForSelectedNode(){
        if(selectedNode.getChildCount() > 0){
        	analysisType = AnalysisUtil.CATEGORY_INCLUDE_CATEGORY;
        	refreshShareOfSalesForCategoryIncludeCategory();
        	refreshSalesTrendForCategoryIncludeCategory();
        	refreshLowMovingForCategoryIncludeCategory();
        	return;
        }
        if(storeSellingItems.size() > 0){
        	analysisType = AnalysisUtil.CATEGORY_INCLUDE_PRODUCT;
        	refreshLowMovingListForCategoryIncludeProduct();
        	return;
        }
        if (storeSellingItems.size() == 0) {
			analysisType = AnalysisUtil.CATEGORY_INCLUDE_NONE;
			return;
		}
	}
	public void viewProductAnalysis(){
		analysisType = AnalysisUtil.PRODUCT;
		refreshSalesVolumeTrendForProduct();
	}

	//Show share of categories
	public void refreshShareOfSalesForCategoryIncludeCategory(){
		List<TreeNode> nodes = selectedNode.getChildren();
		LinkedHashMap<String, Integer> dataMap = new LinkedHashMap<String, Integer>();
		for(int i = 0; i < nodes.size(); i++){
			List<StoreSellingItem> items  = MonitorUtil.generateStoreSellingItemsBySelectedNode(nodes.get(i), currentStore);
			Integer number = analysisUtil.getSalesShareDataByYear(items, currentStore.getStoreId(), currentYear);
			dataMap.put(((Category) nodes.get(i).getData()).getCategoryName(), number);
		}
		salesShareForCategoryIncludeCategory = ChartUtil.generatePieChartModel("Sales Share Of Categories", dataMap);
	}
	//Show sales trend of categories
	public void refreshSalesTrendForCategoryIncludeCategory(){
		List<TreeNode> nodes = selectedNode.getChildren();
		LinkedHashMap<String, LinkedHashMap<String, Double>> dataMap = new LinkedHashMap<String, LinkedHashMap<String,Double>>();
		for(int i = 0; i < nodes.size(); i++){
			List<StoreSellingItem> items  = MonitorUtil.generateStoreSellingItemsBySelectedNode(nodes.get(i), currentStore);
			LinkedHashMap<String, Double> chartDataMap = analysisUtil.getSalesTrendDataByYear(items, currentStore.getStoreId(), currentYear);
			dataMap.put(((Category) nodes.get(i).getData()).getCategoryName(), chartDataMap);
		}
		salesTrendForCategoryIncludeCategory = ChartUtil.generateLineChartModel("Sales Trend of Categories", "Month", "Sales", dataMap);
	}
	//Show Low-Moving of products
	public void refreshLowMovingForCategoryIncludeCategory(){
		List<TreeNode> nodes = selectedNode.getChildren();
		ArrayList<StoreSellingItem> allItems = new ArrayList<StoreSellingItem>();
		for(int i = 0; i < nodes.size(); i++){
			List<StoreSellingItem> items  = MonitorUtil.generateStoreSellingItemsBySelectedNode(nodes.get(i), currentStore);
			allItems.addAll(items);
		}
		//TODO
		LinkedHashMap<StoreSellingItem, Integer> linkedHashMap = analysisUtil.getSalesLowingMoveDataByYear(allItems, currentStore.getStoreId(), currentYear, currentNum);
		Iterator<StoreSellingItem> iterator = linkedHashMap.keySet().iterator();
		salesLowMovingForCategory = new ArrayList<SalesVolumeLowMoving>();
		while(iterator.hasNext()){
			StoreSellingItem key = iterator.next();
			SalesVolumeLowMoving salesVolumeLowMoving = new SalesVolumeLowMoving();
			salesVolumeLowMoving.setSalesVolume(linkedHashMap.get(key));
			salesVolumeLowMoving.setSellingItem(key);
			salesLowMovingForCategory.add(salesVolumeLowMoving);
		}
	}

	public void refreshCategoryIncludeCategory(){
		refreshShareOfSalesForCategoryIncludeCategory();
		refreshSalesTrendForCategoryIncludeCategory();
		refreshLowMovingForCategoryIncludeCategory();
	}

	//Show Low-Moving of products
	public void refreshLowMovingListForCategoryIncludeProduct(){
		List<StoreSellingItem> items  = MonitorUtil.generateStoreSellingItemsBySelectedNode(selectedNode, currentStore);
		LinkedHashMap<StoreSellingItem, Integer> linkedHashMap = analysisUtil.getSalesLowingMoveDataByYear(items, currentStore.getStoreId(), currentYear, currentNum);
		Iterator<StoreSellingItem> iterator = linkedHashMap.keySet().iterator();
		salesLowMovingForCategory = new ArrayList<SalesVolumeLowMoving>();
		while(iterator.hasNext()){
			StoreSellingItem key = iterator.next();
			SalesVolumeLowMoving salesVolumeLowMoving = new SalesVolumeLowMoving();
			salesVolumeLowMoving.setSalesVolume(linkedHashMap.get(key));
			salesVolumeLowMoving.setSellingItem(key);
			salesLowMovingForCategory.add(salesVolumeLowMoving);
		}
	}

	//Show sales trend of products
	public void refreshSalesVolumeTrendForProduct(){
		if(productAnalysisType.equals("forYear")){
			isForecast=false;
			refreshSalesVolumeTrendForProductForYear();
		}

		if(productAnalysisType.equals("forMonth")){
			isForecast=false;
			refreshSalesVolumeTrendForProductForMonth();
		}

	}
	public void refreshSalesVolumeTrendForProductSwitchForecast(){
		if(isForecast == true){
			if(productAnalysisType.equals("forYear")){
				addSalesVolumeTrendForProductForYear();
			}

			if(productAnalysisType.equals("forMonth")){
				final Set<Entry<String, String>> mapValues = years.entrySet();
			    final int maplength = mapValues.size();
				@SuppressWarnings("unchecked")
				final Entry<String,String>[] entries = new Entry[maplength];
			    mapValues.toArray(entries);

			    if(maplength >= 1){
			    	currentYear = entries[maplength-1].getKey();
			    	addSalesVolumeTrendForProductForMonth();
			    }
			}
		}else {
			if(productAnalysisType.equals("forYear")){
				refreshSalesVolumeTrendForProductForYear();
			}

			if(productAnalysisType.equals("forMonth")){
				refreshSalesVolumeTrendForProductForMonth();
			}
		}

	}

	public void refreshSalesVolumeTrendForProductForYear(){
		String currentProductId = selectedItem.getProductId();
		LinkedHashMap<String, LinkedHashMap<String, Double>> dataMap = new LinkedHashMap<String, LinkedHashMap<String,Double>>();
		LinkedHashMap<String, Double> itemHashMap = analysisUtil.getForecastSalesDataForYear(currentStore.getStoreId(), currentProductId);
		dataMap.put(selectedItem.getRegionListItem().getProduct().getName(), itemHashMap);
		salesTrendForCategoryIncludeProduct = ChartUtil.generateLineChartModel("Sales Trend of Product", "Time", "Sales Volume", dataMap);
	}

	public void addSalesVolumeTrendForProductForYear(){
		String currentProductId = selectedItem.getProductId();
		LinkedHashMap<String, LinkedHashMap<String, Double>> dataMap = new LinkedHashMap<String, LinkedHashMap<String,Double>>();
		LinkedHashMap<String, Double> itemHashMap = analysisUtil.getForecastSalesDataForYear(currentStore.getStoreId(), currentProductId);
		dataMap.put(selectedItem.getRegionListItem().getProduct().getName(), itemHashMap);


		final Set<Entry<String, Double>> mapValues = itemHashMap.entrySet();
	    final int maplength = mapValues.size();
	    if(maplength >= 2){
		    @SuppressWarnings("unchecked")
			final Entry<String,Double>[] entries = new Entry[maplength];
		    mapValues.toArray(entries);
		    LinkedHashMap<String, Double> itemHashMapForForecast = new LinkedHashMap<String, Double>();
		    itemHashMapForForecast.put(entries[maplength-1].getKey(), entries[maplength-1].getValue());

			SimpleRegression forecastRegression = LeastSquareUtil.generateForecast(itemHashMap);
			String startYearString = entries[maplength-1].getKey();
			double startYearDouble = new Double(startYearString).doubleValue();
			itemHashMapForForecast.put(Integer.toString(new Double(startYearDouble+1).intValue()), new Double(forecastRegression.predict(startYearDouble+1)));
			itemHashMapForForecast.put(Integer.toString(new Double(startYearDouble+2).intValue()), new Double(forecastRegression.predict(startYearDouble+2)));
			itemHashMapForForecast.put(Integer.toString(new Double(startYearDouble+3).intValue()), new Double(forecastRegression.predict(startYearDouble+3)));

			dataMap.put(selectedItem.getRegionListItem().getProduct().getName()+"(Forecast)", itemHashMapForForecast);
	    }
		salesTrendForCategoryIncludeProduct = ChartUtil.generateLineChartModel("Sales Trend of Product - Forecast From History Data", "Time", "Sales Volume", dataMap);
	}

	public void refreshSalesVolumeTrendForProductForMonth(){
		String currentProductId = selectedItem.getProductId();
		LinkedHashMap<String, LinkedHashMap<String, Double>> dataMap = new LinkedHashMap<String, LinkedHashMap<String,Double>>();
		LinkedHashMap<String, Double> itemHashMap = analysisUtil.getForecastSalesDataForMonth(currentStore.getStoreId(), currentProductId, currentYear);
		dataMap.put(selectedItem.getRegionListItem().getProduct().getName(), itemHashMap);
		salesTrendForCategoryIncludeProduct = ChartUtil.generateLineChartModel("Sales Trend of Product", "Time", "Sales Volume", dataMap);
	}

	public void addSalesVolumeTrendForProductForMonth(){
		String currentProductId = selectedItem.getProductId();
		LinkedHashMap<String, LinkedHashMap<String, Double>> dataMap = new LinkedHashMap<String, LinkedHashMap<String,Double>>();
		LinkedHashMap<String, Double> itemHashMap = analysisUtil.getForecastSalesDataForMonthForForecast(currentStore.getStoreId(), currentProductId);
		dataMap.put(selectedItem.getRegionListItem().getProduct().getName(), itemHashMap);

		final Set<Entry<String, Double>> mapValues = itemHashMap.entrySet();
	    final int maplength = mapValues.size();
	    if(maplength >= 2){
			@SuppressWarnings("unchecked")
			final Entry<String,Double>[] entries = new Entry[maplength];
		    mapValues.toArray(entries);
		    LinkedHashMap<String, Double> itemHashMapForForecast = new LinkedHashMap<String, Double>();

		    Iterator<String> iterator = itemHashMap.keySet().iterator();
		    LinkedHashMap<String, Double> mapForForecast = new LinkedHashMap<String, Double>();
		    String i = "1";
		    while(iterator.hasNext()){
		    	if(i.equals("7"))break;
		    	mapForForecast.put(i, itemHashMap.get(iterator.next()));
		    	i = Integer.toString(new Integer(i) + new Integer(1));
		    }

		    SimpleRegression forecastRegression = LeastSquareUtil.generateForecast(mapForForecast);
		    ArrayList<String> futureDateList = DateUtil.getFutureYearAndMonthList(new Date(), 3);
		    itemHashMapForForecast.put(DateUtil.getYearAndMonth(new Date()), new Double(new Double(forecastRegression.predict(7)).intValue()));
		    itemHashMapForForecast.put(futureDateList.get(0), new Double(new Double(forecastRegression.predict(8)).intValue()));
			itemHashMapForForecast.put(futureDateList.get(1), new Double(new Double(forecastRegression.predict(9)).intValue()));
			itemHashMapForForecast.put(futureDateList.get(2), new Double(new Double(forecastRegression.predict(10)).intValue()));
			dataMap.put(selectedItem.getRegionListItem().getProduct().getName()+"(Forecast)", itemHashMapForForecast);
	    }
		salesTrendForCategoryIncludeProduct = ChartUtil.generateLineChartModel("Sales Trend of Product - Forecast From History Data", "Time", "Sales Volume", dataMap);
	}

	private void initTable(){
		selectedItems = null;
		filteredItems = null;
	}
	public AnalysisUtil getAnalysisUtil() {
		return analysisUtil;
	}

	public void setAnalysisUtil(AnalysisUtil analysisUtil) {
		this.analysisUtil = analysisUtil;
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

	public RegionListCategoryDao getCategoryDao() {
		return categoryDao;
	}

	public void setCategoryDao(RegionListCategoryDao categoryDao) {
		this.categoryDao = categoryDao;
	}

	public SalesRecordDao getSalesRecordDao() {
		return salesRecordDao;
	}

	public void setSalesRecordDao(SalesRecordDao salesRecordDao) {
		this.salesRecordDao = salesRecordDao;
	}

	public Store getCurrentStore() {
		return currentStore;
	}

	public void setCurrentStore(Store currentStore) {
		this.currentStore = currentStore;
	}

	public Region getCurrentRegion() {
		return currentRegion;
	}

	public void setCurrentRegion(Region currentRegion) {
		this.currentRegion = currentRegion;
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

	public List<StoreSellingItem> getStoreSellingItems() {
		return storeSellingItems;
	}

	public void setStoreSellingItems(List<StoreSellingItem> storeSellingItems) {
		this.storeSellingItems = storeSellingItems;
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

	public String getAnalysisType() {
		return analysisType;
	}

	public void setAnalysisType(String analysisType) {
		this.analysisType = analysisType;
	}

	public LineChartModel getSalesTrendForCategoryIncludeCategory() {
		return salesTrendForCategoryIncludeCategory;
	}

	public void setSalesTrendForCategoryIncludeCategory(
			LineChartModel salesTrendForCategoryIncludeCategory) {
		this.salesTrendForCategoryIncludeCategory = salesTrendForCategoryIncludeCategory;
	}

	public String getCurrentYear() {
		return currentYear;
	}

	public void setCurrentYear(String currentYear) {
		this.currentYear = currentYear;
	}

	public LinkedHashMap<String, String> getYears() {
		return years;
	}

	public void setYears(LinkedHashMap<String, String> years) {
		this.years = years;
	}

	public PieChartModel getSalesShareForCategoryIncludeCategory() {
		return salesShareForCategoryIncludeCategory;
	}

	public void setSalesShareForCategoryIncludeCategory(
			PieChartModel salesShareForCategoryIncludeCategory) {
		this.salesShareForCategoryIncludeCategory = salesShareForCategoryIncludeCategory;
	}

	public Integer getCurrentNum() {
		return currentNum;
	}

	public void setCurrentNum(Integer currentNum) {
		this.currentNum = currentNum;
	}

	public ArrayList<SalesVolumeLowMoving> getSalesLowMovingForCategory() {
		return salesLowMovingForCategory;
	}

	public void setSalesLowMovingForCategory(
			ArrayList<SalesVolumeLowMoving> salesLowMovingForCategory) {
		this.salesLowMovingForCategory = salesLowMovingForCategory;
	}

	public LineChartModel getSalesTrendForCategoryIncludeProduct() {
		return salesTrendForCategoryIncludeProduct;
	}

	public void setSalesTrendForCategoryIncludeProduct(
			LineChartModel salesTrendForCategoryIncludeProduct) {
		this.salesTrendForCategoryIncludeProduct = salesTrendForCategoryIncludeProduct;
	}

	public String getProductAnalysisType() {
		return productAnalysisType;
	}

	public void setProductAnalysisType(String productAnalysisType) {
		this.productAnalysisType = productAnalysisType;
	}

	public boolean getIsForecast() {
		return isForecast;
	}

	public void setIsForecast(boolean isForecast) {
		this.isForecast = isForecast;
	}



}
