package merchandisebean.franchiser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.TreeNode;


import dao.BasicListCategoryDao;
import dao.BasicListDao;

import merchandise.utils.BasicListItemDiff;
import merchandise.utils.CategoryUtil;
import merchandise.utils.ProductUtil;
import merchandise.utils.VersionUtil;
import model.BasicList;
import model.BasicListCategory;
import model.BasicListItem;
@ManagedBean(name="franchiserVersion")
@ViewScoped
public class FranchiserVersion implements Serializable{
private static final long serialVersionUID = -3694259330226236227L;
private List<BasicList> basicLists;
private BasicList selectedBasicList;
private BasicList selectedBasicListNext;

private TreeNode viewRoot;
private TreeNode viewRootNext;

private BasicListDao basicListDao;
private BasicListCategoryDao basicListCategoryDao;
private BasicListItemDiff basicListDiff;

private List<BasicList> viewItems;
private BasicListItem selectedViewItem;

private Integer addtionNum;
private Integer deleteNum;

private List<BasicListCategory> additionCategories;
private List<BasicListCategory> deleteCategories;
@PostConstruct
public void init(){
	basicListDao = new BasicListDao();
	basicListCategoryDao = new BasicListCategoryDao();
	basicLists = basicListDao.queryBasicLists();
}

//Create
public void openCreateVersion(){
	RequestContext.getCurrentInstance().execute("PF('createVersion').show();");
}

public void createVersion(){
	VersionUtil.createNewBasicList();
	basicLists = basicListDao.queryBasicLists();

	RequestContext.getCurrentInstance().execute("PF('createVersion').hide();");
}

public void openViewVersionDetail(){
	List<BasicListCategory> categories1 = basicListCategoryDao.queryCategoriesByVersionId(selectedBasicList.getVersionId());
	viewRoot = CategoryUtil.generateTree(categories1, CategoryUtil.ORDER_BY_NAME, CategoryUtil.ROOT_FATHER_ID);
	if(viewRoot!=null)viewRoot.setExpanded(true);

	int index = -1;
	for(int i = 0; i < basicLists.size(); i++){
		if(basicLists.get(i).getVersionId().equals(selectedBasicList.getVersionId())){
			index = i;
		}
	}

	List<BasicListCategory> categories2 = null;
	if(index+1 < basicLists.size()){
		selectedBasicListNext = basicLists.get(index+1);
		categories2 = basicListCategoryDao.queryCategoriesByVersionId(selectedBasicListNext.getVersionId());
		viewRootNext = CategoryUtil.generateTree(categories2, CategoryUtil.ORDER_BY_NAME, CategoryUtil.ROOT_FATHER_ID);
		if(viewRootNext!=null)viewRootNext.setExpanded(true);

		basicListDiff = ProductUtil.generateBasicListDiff(selectedBasicListNext.getVersionId(), selectedBasicList.getVersionId());
	}else {
		selectedBasicListNext = null;
		viewRootNext = null;
		basicListDiff = ProductUtil.generateBasicListDiff("-1", selectedBasicList.getVersionId());
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
		setAddtionNum(additionCategories.size());
		setDeleteNum(deleteCategories.size());
	}else {
		setAddtionNum(categories1.size());
		setDeleteNum(0);
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
public void openAboutRules(){
	RequestContext.getCurrentInstance().execute("PF('aboutRules').show();");
}

public void viewItemsDetail(){
	RequestContext.getCurrentInstance().execute("PF('viewItemsDetail').show();");
}


public void viewItemDetail(){
	RequestContext.getCurrentInstance().execute("PF('viewItemDetail').show();");
}

public List<BasicList> getBasicLists() {
	return basicLists;
}

public void setBasicLists(List<BasicList> basicLists) {
	this.basicLists = basicLists;
}

public BasicList getSelectedBasicList() {
	return selectedBasicList;
}

public void setSelectedBasicList(BasicList selectedBasicList) {
	this.selectedBasicList = selectedBasicList;
}

public BasicList getSelectedBasicListNext() {
	return selectedBasicListNext;
}

public void setSelectedBasicListNext(BasicList selectedBasicListNext) {
	this.selectedBasicListNext = selectedBasicListNext;
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

public BasicListItemDiff getBasicListDiff() {
	return basicListDiff;
}

public void setBasicListDiff(BasicListItemDiff basicListDiff) {
	this.basicListDiff = basicListDiff;
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
