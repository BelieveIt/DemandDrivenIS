package merchandisebean.franchiser;

import java.io.Serializable;
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
	if(index+1 < basicLists.size()){
		selectedBasicListNext = basicLists.get(index+1);
		List<BasicListCategory> categories2 = basicListCategoryDao.queryCategoriesByVersionId(selectedBasicListNext.getVersionId());
		viewRootNext = CategoryUtil.generateTree(categories2, CategoryUtil.ORDER_BY_NAME, CategoryUtil.ROOT_FATHER_ID);
		if(viewRootNext!=null)viewRootNext.setExpanded(true);

		basicListDiff = ProductUtil.generateBasicListDiff(selectedBasicListNext.getVersionId(), selectedBasicList.getVersionId());
	}else {
		selectedBasicListNext = null;
		viewRootNext = null;
		basicListDiff = ProductUtil.generateBasicListDiff("-1", selectedBasicList.getVersionId());
	}
	RequestContext.getCurrentInstance().execute("PF('viewVersion').show();");
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


}
