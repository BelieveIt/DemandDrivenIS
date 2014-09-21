package merchandisebean.franchiser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import merchandise.utils.CategoryDefaultTreeNode;
import merchandise.utils.CategoryUtil;
import merchandise.utils.ProductUtil;
import model.BasicList;
import model.BasicListCategory;
import model.BasicListItem;
import model.Category;


import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.TreeNode;

import utils.IdentityUtil;

import dao.BasicListCategoryDao;
import dao.BasicListDao;
import dao.BasicListItemDao;

@ManagedBean(name="franchiserCategory")
@ViewScoped
public class FranchiserCategory implements Serializable{
	private static final long serialVersionUID = -9201081405856011379L;
	
	private BasicListDao basicListDao;
	private BasicListCategoryDao categoryDao;
	private BasicListItemDao basicListItemDao;
	
	private TreeNode rootNode;
	private TreeNode selectedNode;
	private String selectedNodeName;
	private TreeNode selectedNodeTreeRoot;

	private String categoryName;
	private String currentVersion;
	private String order;
	private List<String> versionIdList;
	
	@PostConstruct
    public void init() {
		basicListDao = new BasicListDao();
		categoryDao = new BasicListCategoryDao();
		basicListItemDao = new BasicListItemDao();
		order = CategoryUtil.ORDER_BY_NAME;
		initByVersionId("head");
    }

	private void initByVersionId(String versionId){		
		rootNode= getCurrentTree(order, versionId);
		CategoryUtil.expandAllTree(rootNode);	
		selectedNode = null;
		selectedNodeName = null;
		
		categoryName = null;
		currentVersion = versionId;	
		
		List<BasicList> basicLists = basicListDao.queryBasicLists();
		versionIdList = new ArrayList<String>();
		versionIdList.add("head");
		for(BasicList basicList : basicLists){
			versionIdList.add(basicList.getVersionId());
		}
		
	}
	public void onNodeSelect(NodeSelectEvent event) {
        selectedNode = event.getTreeNode();
    }
	public void onNodeExpand(NodeExpandEvent event) {
        event.getTreeNode().setExpanded(true);
    }
	public void onNodeCollapse(NodeExpandEvent event) {
        event.getTreeNode().setExpanded(false);
    }

	//Add Category
	public void openAddCategory(ActionEvent actionEvent){
		if(selectedNode == null){
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Notice", "No Catecory Selected!");
	        FacesContext.getCurrentInstance().addMessage(null, message);
	        return;
		}
		categoryName = null;
		RequestContext.getCurrentInstance().execute("PF('addCategory').show();");
	}

	public void addCategory(ActionEvent actionEvent){
		BasicListCategory basicListCategory = new BasicListCategory();
		BasicListCategory parentCategory = (BasicListCategory) selectedNode.getData();

		basicListCategory.setCategoryFatherId(parentCategory.getCategoryId());
		basicListCategory.setCategoryName(categoryName);
		basicListCategory.setVersionId(parentCategory.getVersionId());
		basicListCategory.setCategoryId(IdentityUtil.randomUUID());
		basicListCategory.setCreateTime(new Date());

		categoryDao.insertCategory(basicListCategory);
		new CategoryDefaultTreeNode(basicListCategory, selectedNode);

//		ArrayList<String> expandIds = CategoryUtil.getExpendList(rootNode);
//		Category selectedCategory = (Category) selectedNode.getData();
//		expandIds.add(selectedCategory.getCategoryId());
// 		rootNode= getCurrentTree();
//
//		selectedNode = CategoryUtil.queryNode(rootNode, selectedCategory.getCategoryId());
//		selectedNode.setSelected(true);
//		CategoryUtil.expandTree(rootNode, expandIds);

		rootNode = CategoryUtil.restoreTreeStatus(rootNode, getCurrentTree(order, currentVersion), selectedNode);
		Category selectedCategory = (Category) selectedNode.getData();
		selectedNode = CategoryUtil.queryNode(rootNode, selectedCategory.getCategoryId());
		RequestContext.getCurrentInstance().execute("PF('addCategory').hide();");

	}

	//Delete Category
	public void openDeleteCategory(ActionEvent actionEvent){
		if(selectedNode == null){
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Notice", "No Catecory Selected!");
	        FacesContext.getCurrentInstance().addMessage(null, message);
	        return;
		}
		Category selectedCategory = (Category) selectedNode.getData();
		Category rootCategory = (Category) rootNode.getData();
		if(selectedCategory.getCategoryId().equals(rootCategory.getCategoryId())){
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Notice", "Can not Delete Root Category!");
	        FacesContext.getCurrentInstance().addMessage(null, message);
	        return;
		}
		RequestContext.getCurrentInstance().execute("PF('deleteCategory').show();");
	}

	public void deleteCategory(ActionEvent actionEvent){
		List<Category> categories = CategoryUtil.getCategoriesFormTree(selectedNode);
		List<BasicListItem> basicListItems = basicListItemDao.queryProductsByVersionId(currentVersion);
		
		for(Category category : categories){
			List<BasicListItem> basicListItemsByCategory = ProductUtil.generateBasicListItemsByCategory(basicListItems, category);
			for(BasicListItem basicListItem : basicListItemsByCategory){
				basicListItem.setCategoryId(CategoryUtil.NOT_CLASSIFIED_FOR_PRODUCT_ID);
				basicListItemDao.updateProduct(basicListItem);
			}
			categoryDao.deleteCategory(category);
		}
		rootNode = CategoryUtil.restoreTreeStatus(rootNode, getCurrentTree(order,currentVersion), selectedNode);
		RequestContext.getCurrentInstance().execute("PF('deleteCategory').hide();");
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
		categoryDao.updateCategory((BasicListCategory)selectedCategory);
		RequestContext.getCurrentInstance().execute("PF('renameCategory').hide();");
	}

	public void expandAllTree(){
		CategoryUtil.expandAllTree(rootNode);
	}
	public void collapseAllTree(){
		CategoryUtil.collapseAllTree(rootNode);
	}

	public void changeOrder(){
		if(order.equals(CategoryUtil.ORDER_BY_NAME)) sortTreeByName();
		if(order.equals(CategoryUtil.ORDER_BY_TIME)) sortTreeByCreateTime();
	}

	public void sortTreeByName(){
		((CategoryDefaultTreeNode) rootNode).sortByCategoryName();
	}

	public void sortTreeByCreateTime(){
		((CategoryDefaultTreeNode) rootNode).sortByCreateTime();
	}

	//Change Version
	public void changeVersion(){
		initByVersionId(currentVersion);
	}
	
	private TreeNode getCurrentTree(String currentOrder, String versionId){
		List<BasicListCategory> categories = categoryDao.queryCategoriesByVersionId(versionId);
		return CategoryUtil.generateTree(categories, currentOrder, CategoryUtil.ROOT_FATHER_ID);
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

	public BasicListCategoryDao getCategoryDao() {
		return categoryDao;
	}

	public void setCategoryDao(BasicListCategoryDao categoryDao) {
		this.categoryDao = categoryDao;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
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

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public TreeNode getSelectedNodeTreeRoot() {
		if(selectedNode == null){
	        return null;
		}
		Category selectedCategory = (Category) selectedNode.getData();
		Category rootCategory = (Category) rootNode.getData();
		if(selectedCategory.getCategoryId().equals(rootCategory.getCategoryId())){
	        return null;
		}
		Category selectedCategoryParent = (Category) selectedNode.getParent().getData();
		List<Category> categories = CategoryUtil.getCategoriesFormTree(selectedNode);
		selectedNodeTreeRoot = CategoryUtil.generateTree(categories, order, selectedCategoryParent.getCategoryId());
		CategoryUtil.expandAllTree(selectedNodeTreeRoot);
		return selectedNodeTreeRoot;
	}

	public String getCurrentVersion() {
		return currentVersion;
	}

	public void setCurrentVersion(String currentVersion) {
		this.currentVersion = currentVersion;
	}

	public BasicListItemDao getBasicListItemDao() {
		return basicListItemDao;
	}

	public void setBasicListItemDao(BasicListItemDao basicListItemDao) {
		this.basicListItemDao = basicListItemDao;
	}

	public List<String> getVersionIdList() {
		return versionIdList;
	}

	public void setVersionIdList(List<String> versionIdList) {
		this.versionIdList = versionIdList;
	}





}
