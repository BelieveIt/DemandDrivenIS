package inventorybean.region;

import inventorybean.utils.AnalysisUtil;
import inventorybean.utils.MonitorUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.TreeNode;
import org.primefaces.model.chart.LineChartModel;

import dao.RegionDao;
import dao.RegionListCategoryDao;
import dao.SalesRecordDao;
import dao.StoreDao;

import merchandise.utils.CategoryUtil;
import model.Region;
import model.RegionListCategory;
import model.Store;
import model.StoreSellingItem;

@ManagedBean(name="analysisOfRegion")
@ViewScoped
public class AnalysisOfRegion implements Serializable{
	private static final long serialVersionUID = 5265661455745070133L;
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
	private List<StoreSellingItem> selectedItems;
	private List<StoreSellingItem> filteredItems;

	private StoreSellingItem selectedItem;

	private String analysisType;

	private String currentYear;
	private LineChartModel salesTrendForCategoryIncludeCategory;

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
		initChartsForSelectedNode();
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
        	return;
        }
        if(storeSellingItems.size() > 0){
        	analysisType = AnalysisUtil.CATEGORY_INCLUDE_PRODUCT;

        	return;
        }
        if (storeSellingItems.size() == 0) {
			analysisType = AnalysisUtil.CATEGORY_INCLUDE_NONE;
			return;
		}
	}
	public void viewProductAnalysis(){
		analysisType = AnalysisUtil.PRODUCT;
	}

	//Show share of categories
	public void refreshShareOfSalesForCategoryIncludeCategory(){
		List<TreeNode> nodes = selectedNode.getChildren();
		LinkedHashMap<String, LinkedHashMap<String, Double>> dataMap = new LinkedHashMap<String, LinkedHashMap<String,Double>>();
		for(int i = 0; i < nodes.size(); i++){
			List<StoreSellingItem> items  = MonitorUtil.generateStoreSellingItemsBySelectedNode(nodes.get(i), currentStore);
		}
		MonitorUtil.generateStoreSellingItemsBySelectedNode(selectedNode, currentStore);
	}
	//Show sales trend of categories
	public void refreshSalesTrendForCategoryIncludeCategory(){

	}
	//Show Low-Moving of products
	public void refreshLowMovingForCategoryIncludeCategory(){

	}

	//Show Low-Moving of products
	public void refreshLowMovingListForCategoryIncludeProduct(){
	}

	//Show sales trend of products
	public void refreshSalesVolumeTrendForProduct(){

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
}
