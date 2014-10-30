package merchandisebean.region;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import merchandise.utils.CategoryUtil;
import merchandise.utils.VersionUtil;
import model.BasicListCategory;
import model.Category;
import model.ProductType;
import model.RegionListCategory;
import model.RegionListUpdateInfo;

import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.TreeNode;

import dao.BasicListCategoryDao;
import dao.ProductTypeDao;
import dao.RegionListCategoryDao;
import dao.RegionListUpdateInfoDao;

@ManagedBean(name="regionCategory")
@ViewScoped
public class RegionCategory implements Serializable{
	private static final long serialVersionUID = -7352088192758822869L;

	private RegionListCategoryDao categoryDao;
	private ProductTypeDao productTypeDao;
	private BasicListCategoryDao basicListCategoryDao;

	private TreeNode rootNode;
	private TreeNode selectedNode;
	private String selectedNodeName;
	private RegionListCategory selectedCategory;

	private String currentVersion;

	private String currentRegionId;

	private String headStatus;
	private String newestStatus;

	private Integer compareWithNewest;
	private String leftMenuUpdateAlert;

	private TreeNode newestRootNode;
	private TreeNode lastRootNode;

	public TreeNode getLastRootNode() {
		return lastRootNode;
	}

	public void setLastRootNode(TreeNode lastRootNode) {
		this.lastRootNode = lastRootNode;
	}

	private List<ProductType> productTypes;

	private String newestVersionId;
	private String lastVersionId;

	private Integer addtionNum;
	private Integer deleteNum;

	private List<BasicListCategory> additionCategories;
	private List<BasicListCategory> deleteCategories;
	@PostConstruct
	public void init(){
		//TODO
		currentRegionId = "1";
		categoryDao = new RegionListCategoryDao();
		productTypeDao = new ProductTypeDao();
		basicListCategoryDao = new BasicListCategoryDao();

		if(isHeadNull()){
			headStatus = null;
		}else {
			headStatus = "normal";
			initRootNodeByVersionId("head");
		}

		if(isUpdateInfoNull()){
			newestStatus = null;
		}else {
			newestVersionId = VersionUtil.getRetrivedNewestVersionId(currentRegionId);
			lastVersionId = VersionUtil.getRetrivedLastVersionId(currentRegionId);
			initNewestRootNodeByVersionId(newestVersionId);
			newestStatus = "normal";
			List<BasicListCategory> categories1 = basicListCategoryDao.queryCategoriesByVersionId(newestVersionId);
			List<BasicListCategory> categories2 = null;
			if(lastVersionId != "-1"){
				categories2 = basicListCategoryDao.queryCategoriesByVersionId(lastVersionId);
				lastRootNode = CategoryUtil.generateTree(categories2, CategoryUtil.ORDER_BY_NAME, CategoryUtil.ROOT_FATHER_ID);
				if(lastRootNode!=null)lastRootNode.setExpanded(true);
			}

			additionCategories = new ArrayList<BasicListCategory>();
			deleteCategories = new ArrayList<BasicListCategory>();
			if(categories2 != null){
				for(BasicListCategory categoryInListNext : categories2){
					boolean flag = false;
					for(BasicListCategory categoryInNew : categories1){
						if(categoryInListNext.getCategoryId().equals(categoryInNew.getCategoryId())){
							flag = true;
							break;
						}
					}
					if(!flag){
						deleteCategories.add(categoryInListNext);
					}
				}

				for(BasicListCategory categoryInNew : categories1){
					boolean flag = false;
					for(BasicListCategory categoryInListNext : categories2){
						if(categoryInListNext.getCategoryId().equals(categoryInNew.getCategoryId())){
							flag = true;
							break;
						}
					}
					if(!flag){
						additionCategories.add(categoryInNew);
					}
				}
				addtionNum = additionCategories.size();
				deleteNum = deleteCategories.size();
			}else {
				addtionNum = categories1.size();
				deleteNum = 0;
				additionCategories = categories1;
			}
		}
		doVersionCampare();
		setProductTypes(productTypeDao.queryProductTypes());
	}

	public void viewAdditionCate(){
		List<TreeNode> allNodes = CategoryUtil.getListFromTree(newestRootNode);
		CategoryUtil.collapseAllTree(newestRootNode);
		for(TreeNode treeNode : allNodes){
			treeNode.setSelected(false);
		}
		for(Category addCategory : additionCategories){
			for(TreeNode treeNode : allNodes){
				if( ((Category) treeNode.getData()).getCategoryId().equals(addCategory.getCategoryId())){
					CategoryUtil.expandCertainTree(treeNode);
					treeNode.setSelected(true);
				}
			}
		}
	}

	public void viewDeleteCate(){
		List<TreeNode> allNodes = CategoryUtil.getListFromTree(lastRootNode);
		CategoryUtil.collapseAllTree(lastRootNode);
		for(TreeNode treeNode : allNodes){
			treeNode.setSelected(false);
		}

		for(Category deleteCategory : deleteCategories){
			for(TreeNode treeNode : allNodes){
				if( ((Category) treeNode.getData()).getCategoryId().equals(deleteCategory.getCategoryId())){
					CategoryUtil.expandCertainTree(treeNode);
					treeNode.setSelected(true);
				}
			}
		}
	}

	public void doVersionCampare(){
		if(isHeadNull() && isUpdateInfoNull()) {
			compareWithNewest = -1;
			leftMenuUpdateAlert = null;
			return;
		}
		if(isHeadNull() && !isUpdateInfoNull()){
			compareWithNewest = 0;
			leftMenuUpdateAlert = "Update";
			return;
		}
		boolean result = CategoryUtil.compareLocalTreeAndBasicList(currentRegionId, VersionUtil.getRetrivedNewestVersionId(currentRegionId));
		if(result){
			compareWithNewest = 1;
			leftMenuUpdateAlert = null;
		}else {
			compareWithNewest = 0;
			leftMenuUpdateAlert = "Update";
		}
	}

	public void openDiff(){
		RequestContext.getCurrentInstance().execute("PF('viewDiff').show();");
	}

	private boolean isHeadNull(){
		List<RegionListCategory> categories = categoryDao.queryCategoriesByVersionId("head", currentRegionId);
		if(categories.size() == 0){
			return true;
		}else {
			return false;
		}
	}
	private boolean isUpdateInfoNull(){
		RegionListUpdateInfoDao regionListUpdateInfoDao = new RegionListUpdateInfoDao();
		List<RegionListUpdateInfo> newestRegionListUpdateInfos = regionListUpdateInfoDao.queryRegionListUpdateInfosByRegionId(currentRegionId);
		if(newestRegionListUpdateInfos.size() == 0){
			return true;
		}else {
			return false;
		}
	}

	public void initRootNodeByVersionId(String versionId){
		rootNode = getCurrentTree(CategoryUtil.ORDER_BY_NAME, versionId);
		if(rootNode!=null)rootNode.setExpanded(true);
		selectedNode = null;
		selectedNodeName = null;

		setCurrentVersion(versionId);
	}

	public void initNewestRootNodeByVersionId(String versionId){
		newestRootNode = getCurrentTree(CategoryUtil.ORDER_BY_NAME, versionId);
		if(newestRootNode!=null)newestRootNode.setExpanded(true);
	}

	public void openUpdateToNewest(){
		RequestContext.getCurrentInstance().execute("PF('updateCategories').show();");
	}

	//Update to Retrieved Newest Version
	public void updateToNewest(){
		if(!isUpdateInfoNull()){
			VersionUtil.updateToNewestBasiclistCategory(currentRegionId);
			headStatus = "normal";
			initRootNodeByVersionId("head");
			doVersionCampare();
		}
		RequestContext.getCurrentInstance().execute("PF('updateCategories').hide();");
	}

	//Rename Category
	public void openRenameCategory(ActionEvent actionEvent){
		if(selectedNode == null){
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Notice", "No Catecory Selected!");
	        FacesContext.getCurrentInstance().addMessage(null, message);
	        return;
		}
		RequestContext.getCurrentInstance().execute("PF('renameCategory').show();");
	}

	public void renameCategory(){
		Category selectedCategory = (Category) selectedNode.getData();
		categoryDao.updateCategory((RegionListCategory)selectedCategory);
		RequestContext.getCurrentInstance().execute("PF('renameCategory').hide();");
	}

	public void onNodeSelect(NodeSelectEvent event) {
        selectedNode = event.getTreeNode();
    }
	public void onNodeExpand(NodeExpandEvent event) {
        event.getTreeNode().setExpanded(true);
    }
	public void onNodeCollapse(NodeCollapseEvent event) {
        event.getTreeNode().setExpanded(false);
    }

	//View Category
	public void openViewCategory(){
		selectedCategory = (RegionListCategory) selectedNode.getData();
		if(productTypes!=null){
			for(ProductType productType : productTypes){
				if(productType.getProductTypeId().equals(selectedCategory.getProductTypeId())){
					selectedCategory.setProductTypeName(productType.getProductTypeName());
				}
			}
		}
		RequestContext.getCurrentInstance().execute("PF('viewCategory').show();");
	}

	public void openUpdateRules(){
		RequestContext.getCurrentInstance().execute("PF('updateRules').show();");
	}

	private TreeNode getCurrentTree(String currentOrder, String versionId){
		List<RegionListCategory> categories = categoryDao.queryCategoriesByVersionId(versionId, currentRegionId);
		return CategoryUtil.generateTree(categories, currentOrder, CategoryUtil.ROOT_FATHER_ID);
	}

	public TreeNode getRootNode() {
		return rootNode;
	}

	public void setRootNode(TreeNode rootNode) {
		this.rootNode = rootNode;
	}

	public String getSelectedNodeName() {
		if(selectedNode == null){
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Notice", "No Catecory Selected!");
	        FacesContext.getCurrentInstance().addMessage(null, message);
	        return null;
		}
		Category selectedCategory = (Category) selectedNode.getData();
		selectedNodeName = selectedCategory.getCategoryName();
		return selectedNodeName;
	}

	public void setSelectedNodeName(String selectedNodeName) {
		Category selectedCategory = (Category) selectedNode.getData();
		selectedCategory.setCategoryName(selectedNodeName);
		this.selectedNodeName = selectedNodeName;
	}

	public TreeNode getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}

	public String getCurrentVersion() {
		return currentVersion;
	}

	public void setCurrentVersion(String currentVersion) {
		this.currentVersion = currentVersion;
	}

	public String getHeadStatus() {
		return headStatus;
	}

	public void setHeadStatus(String headStatus) {
		this.headStatus = headStatus;
	}

	public String getNewestStatus() {
		return newestStatus;
	}

	public void setNewestStatus(String newestStatus) {
		this.newestStatus = newestStatus;
	}

	public Integer getCompareWithNewest() {
		return compareWithNewest;
	}

	public void setCompareWithNewest(Integer compareWithNewest) {
		this.compareWithNewest = compareWithNewest;
	}

	public TreeNode getNewestRootNode() {
		return newestRootNode;
	}

	public void setNewestRootNode(TreeNode newestRootNode) {
		this.newestRootNode = newestRootNode;
	}

	public String getLeftMenuUpdateAlert() {
		return leftMenuUpdateAlert;
	}

	public void setLeftMenuUpdateAlert(String leftMenuUpdateAlert) {
		this.leftMenuUpdateAlert = leftMenuUpdateAlert;
	}

	public RegionListCategory getSelectedCategory() {
		return selectedCategory;
	}

	public void setSelectedCategory(RegionListCategory selectedCategory) {
		this.selectedCategory = selectedCategory;
	}

	public List<ProductType> getProductTypes() {
		return productTypes;
	}

	public void setProductTypes(List<ProductType> productTypes) {
		this.productTypes = productTypes;
	}

	public String getCurrentRegionId() {
		return currentRegionId;
	}

	public void setCurrentRegionId(String currentRegionId) {
		this.currentRegionId = currentRegionId;
	}

	public Integer getAddtionNum() {
		return addtionNum;
	}

	public void setAddtionNum(Integer addtionNum) {
		this.addtionNum = addtionNum;
	}

	public Integer getDeleteNum() {
		return deleteNum;
	}

	public void setDeleteNum(Integer deleteNum) {
		this.deleteNum = deleteNum;
	}

	public List<BasicListCategory> getAdditionCategories() {
		return additionCategories;
	}

	public void setAdditionCategories(List<BasicListCategory> additionCategories) {
		this.additionCategories = additionCategories;
	}

	public List<BasicListCategory> getDeleteCategories() {
		return deleteCategories;
	}

	public void setDeleteCategories(List<BasicListCategory> deleteCategories) {
		this.deleteCategories = deleteCategories;
	}

	public String getNewestVersionId() {
		return newestVersionId;
	}

	public void setNewestVersionId(String newestVersionId) {
		this.newestVersionId = newestVersionId;
	}

	public String getLastVersionId() {
		return lastVersionId;
	}

	public void setLastVersionId(String lastVersionId) {
		this.lastVersionId = lastVersionId;
	}


}
