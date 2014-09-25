package merchandisebean.region;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.primefaces.context.RequestContext;
import org.primefaces.model.TreeNode;

import dao.BasicListCategoryDao;
import dao.RegionListUpdateInfoDao;


import merchandise.utils.BasicListItemDiff;
import merchandise.utils.CategoryUtil;
import merchandise.utils.ProductUtil;
import merchandise.utils.RegionVersionListItem;
import merchandise.utils.VersionUtil;
import model.BasicListCategory;
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
		CategoryUtil.expandAllTree(viewRoot);

		List<BasicListCategory> categories2;
		if(selectedRegionVersionListItemNext!=null){
			categories2 = basicListCategoryDao.queryCategoriesByVersionId(selectedRegionVersionListItemNext.getVersionId());
			viewRootNext = CategoryUtil.generateTree(categories2, CategoryUtil.ORDER_BY_NAME, CategoryUtil.ROOT_FATHER_ID);
			CategoryUtil.expandAllTree(viewRootNext);

			basicListDiff = ProductUtil.generateBasicListDiff(selectedRegionVersionListItemNext.getVersionId(), selectedRegionVersionListItem.getVersionId());
		}else{
			viewRootNext = null;
			basicListDiff = ProductUtil.generateBasicListDiff("-1", selectedRegionVersionListItem.getVersionId());
		}

		RequestContext.getCurrentInstance().execute("PF('viewVersion').show();");
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

}
