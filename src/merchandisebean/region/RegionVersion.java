package merchandisebean.region;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.apache.commons.lang3.ObjectUtils.Null;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.TreeNode;

import dao.BasicListCategoryDao;
import dao.RegionListUpdateInfoDao;


import merchandise.utils.BasicListItemDiff;
import merchandise.utils.CategoryUtil;
import merchandise.utils.ProductUtil;
import merchandise.utils.RegionVersionListItem;
import merchandise.utils.VersionUtil;
import model.BasicList;
import model.BasicListCategory;
import model.BasicListItem;
import model.Category;
import model.RegionListUpdateInfo;
@ManagedBean(name="regionVersion")
@ViewScoped
public class RegionVersion implements Serializable{
	private static final long serialVersionUID = -6525298726394719841L;

	private RegionListUpdateInfoDao regionListUpdateInfoDao;
	private BasicListCategoryDao basicListCategoryDao;

	private List<RegionVersionListItem> regionVersionListItems;
	private RegionVersionListItem selectedRegionVersionListItem;
	private RegionVersionListItem selectedRegionVersionListItemNext;

	private TreeNode viewRoot;
	private TreeNode viewRootNext;

	@ManagedProperty(value="#{regionCategory}")
	private RegionCategory regionCategory;
	@ManagedProperty(value="#{regionProduct}")
	private RegionProduct regionProduct;
	private String leftMenuNewAlert;

	private BasicListItemDiff basicListDiff;

	private String currentRegionId;

	private List<BasicList> viewItems;
	private BasicListItem selectedViewItem;
	
	private Integer addtionNum;
	private Integer deleteNum;
	
	private List<BasicListCategory> additionCategories;
	private List<BasicListCategory> deleteCategories;
	@PostConstruct
	public void init(){
		//TODO
		currentRegionId = "1";
		regionListUpdateInfoDao = new RegionListUpdateInfoDao();
		basicListCategoryDao = new BasicListCategoryDao();
		initProcess();
	}

	public void initProcess(){
		List<RegionListUpdateInfo> regionListUpdateInfos = regionListUpdateInfoDao.queryRegionListUpdateInfosByRegionId(currentRegionId);
		regionVersionListItems = VersionUtil.generateRegionVersionListItems(regionListUpdateInfos);
		if(regionVersionListItems.size() != 0){
			Integer newestVersionStatus = regionVersionListItems.get(0).getIsRetrived();
			if(newestVersionStatus == 1){
				leftMenuNewAlert = null;
			}else {
				leftMenuNewAlert = "new";
			}
		}else {
			leftMenuNewAlert = null;
		}

	}
	//Retrieve
	public void retriveBasicList(ActionEvent actionEvent){
		VersionUtil.retriveFromFranchiser(currentRegionId);
		initProcess();
		regionCategory.doVersionCampare();
		regionProduct.initRegionListItemDiff();
	}

	//Confirm
	public void openConfirmFinishingUpdate(){
		RequestContext.getCurrentInstance().execute("PF('confirmFinishingUpdate').show();");
	}
	public void confirmFinishingUpdate(){
		String confirmVersionId = selectedRegionVersionListItem.getVersionId();
		RegionListUpdateInfo regionListUpdateInfo = regionListUpdateInfoDao.queryRegionListUpdateInfosByRegionIdAndVersionId(currentRegionId, confirmVersionId);
		regionListUpdateInfo.setIsFinished(new Integer(1));
		regionListUpdateInfo.setCreateTime(new Date());
		regionListUpdateInfoDao.updateRegionListUpdateInfo(regionListUpdateInfo);
		initProcess();
		RequestContext.getCurrentInstance().execute("PF('confirmFinishingUpdate').hide();");
	}

	//View
	public void openViewVersionDetail(){
		int index = -1;
		for(int i = 0; i < regionVersionListItems.size(); i++){
			if(regionVersionListItems.get(i).getVersionId().equals(selectedRegionVersionListItem.getVersionId())){
				index = i;
			}
		}
		if(index+1 >= regionVersionListItems.size()){
			selectedRegionVersionListItemNext = null;
		}else {
			selectedRegionVersionListItemNext = regionVersionListItems.get(index+1);
		}
		List<BasicListCategory> categories1 = basicListCategoryDao.queryCategoriesByVersionId(selectedRegionVersionListItem.getVersionId());
		viewRoot = CategoryUtil.generateTree(categories1, CategoryUtil.ORDER_BY_NAME, CategoryUtil.ROOT_FATHER_ID);
		if(viewRoot!=null)viewRoot.setExpanded(true);

		List<BasicListCategory> categories2 = null;
		if(selectedRegionVersionListItemNext!=null){
			categories2 = basicListCategoryDao.queryCategoriesByVersionId(selectedRegionVersionListItemNext.getVersionId());
			viewRootNext = CategoryUtil.generateTree(categories2, CategoryUtil.ORDER_BY_NAME, CategoryUtil.ROOT_FATHER_ID);
			if(viewRootNext!=null)viewRootNext.setExpanded(true);
			basicListDiff = ProductUtil.generateBasicListDiff(selectedRegionVersionListItemNext.getVersionId(), selectedRegionVersionListItem.getVersionId());
		}else{
			viewRootNext = null;
			basicListDiff = ProductUtil.generateBasicListDiff("-1", selectedRegionVersionListItem.getVersionId());
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

		RequestContext.getCurrentInstance().execute("PF('viewVersion').show();");
	}

	public void viewAdditionCate(){
		List<TreeNode> allNodes = CategoryUtil.getListFromTree(viewRoot);
		CategoryUtil.collapseAllTree(viewRoot);
		for(TreeNode treeNode : allNodes){
			treeNode.setSelected(false);
		}
		for(BasicListCategory addCategory : additionCategories){
			for(TreeNode treeNode : allNodes){
				if( ((BasicListCategory) treeNode.getData()).getCategoryId().equals(addCategory.getCategoryId())){
					CategoryUtil.expandCertainTree(treeNode);
					treeNode.setSelected(true);
				}
			}
		}
	}

	public void viewDeleteCate(){
		List<TreeNode> allNodes = CategoryUtil.getListFromTree(viewRootNext);
		CategoryUtil.collapseAllTree(viewRootNext);
		for(TreeNode treeNode : allNodes){
			treeNode.setSelected(false);
		}

		for(BasicListCategory deleteCategory : deleteCategories){
			for(TreeNode treeNode : allNodes){
				if( ((BasicListCategory) treeNode.getData()).getCategoryId().equals(deleteCategory.getCategoryId())){
					CategoryUtil.expandCertainTree(treeNode);
					treeNode.setSelected(true);
				}
			}
		}
	}
	public void viewItemsDetail(){
		RequestContext.getCurrentInstance().execute("PF('viewItemsDetail').show();");
	}


	public void viewItemDetail(){
		RequestContext.getCurrentInstance().execute("PF('viewItemDetail').show();");
	}

	public void viewUsageWizard(){
		RequestContext.getCurrentInstance().execute("PF('viewUsageWizard').show();");
	}

	public String onFlowProcess(FlowEvent event) {

	    return event.getNewStep();

	}

	public List<RegionVersionListItem> getRegionVersionListItems() {
		return regionVersionListItems;
	}

	public void setRegionVersionListItems(List<RegionVersionListItem> regionVersionListItems) {
		this.regionVersionListItems = regionVersionListItems;
	}

	public RegionVersionListItem getSelectedRegionVersionListItem() {
		return selectedRegionVersionListItem;
	}

	public void setSelectedRegionVersionListItem(
			RegionVersionListItem selectedRegionVersionListItem) {
		this.selectedRegionVersionListItem = selectedRegionVersionListItem;
	}

	public RegionVersionListItem getSelectedRegionVersionListItemNext() {
		return selectedRegionVersionListItemNext;
	}

	public void setSelectedRegionVersionListItemNext(
			RegionVersionListItem selectedRegionVersionListItemNext) {
		this.selectedRegionVersionListItemNext = selectedRegionVersionListItemNext;
	}

	public String getLeftMenuNewAlert() {
		return leftMenuNewAlert;
	}

	public void setLeftMenuNewAlert(String leftMenuNewAlert) {
		this.leftMenuNewAlert = leftMenuNewAlert;
	}

	public RegionCategory getRegionCategory() {
		return regionCategory;
	}

	public void setRegionCategory(RegionCategory regionCategory) {
		this.regionCategory = regionCategory;
	}

	public TreeNode getViewRoot() {
		return viewRoot;
	}

	public void setViewRoot(TreeNode viewRoot) {
		this.viewRoot = viewRoot;
	}

	public TreeNode getViewRootNext() {
		return viewRootNext;
	}

	public void setViewRootNext(TreeNode viewRootNext) {
		this.viewRootNext = viewRootNext;
	}

	public BasicListCategoryDao getBasicListCategoryDao() {
		return basicListCategoryDao;
	}

	public void setBasicListCategoryDao(BasicListCategoryDao basicListCategoryDao) {
		this.basicListCategoryDao = basicListCategoryDao;
	}

	public BasicListItemDiff getBasicListDiff() {
		return basicListDiff;
	}

	public void setBasicListDiff(BasicListItemDiff basicListDiff) {
		this.basicListDiff = basicListDiff;
	}

	public RegionProduct getRegionProduct() {
		return regionProduct;
	}

	public void setRegionProduct(RegionProduct regionProduct) {
		this.regionProduct = regionProduct;
	}

	public String getCurrentRegionId() {
		return currentRegionId;
	}

	public void setCurrentRegionId(String currentRegionId) {
		this.currentRegionId = currentRegionId;
	}

	public List<BasicList> getViewItems() {
		return viewItems;
	}

	public void setViewItems(List<BasicList> viewItems) {
		this.viewItems = viewItems;
	}

	public BasicListItem getSelectedViewItem() {
		return selectedViewItem;
	}

	public void setSelectedViewItem(BasicListItem selectedViewItem) {
		this.selectedViewItem = selectedViewItem;
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

}
