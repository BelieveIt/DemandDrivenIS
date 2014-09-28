package inventorybean.region;

import inventorybean.utils.AnalysisUtil;
import inventorybean.utils.MonitorUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import merchandise.utils.CategoryUtil;
import model.Region;
import model.RegionListCategory;
import model.SalesRecord;
import model.Store;
import model.StoreSellingItem;

import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.TreeNode;

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

		RequestContext.getCurrentInstance().execute("PF('addVirtualSales').show();");
	}
	public void setVirtualSales(){
		RequestContext.getCurrentInstance().execute("PF('addVirtualSales').hide();");
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
}
