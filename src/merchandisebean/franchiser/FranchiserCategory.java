package merchandisebean.franchiser;

import java.io.Serializable;
import java.text.ParseException;
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
import merchandise.utils.FranchiserCategoryInit;
import merchandise.utils.ProductUtil;
import model.BasicList;
import model.BasicListCategory;
import model.BasicListItem;
import model.Category;
import model.ProductType;

import oracle.net.aso.i;

import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.TreeNode;

import utils.IdentityUtil;

import dao.BasicListCategoryDao;
import dao.BasicListDao;
import dao.BasicListItemDao;
import dao.ProductTypeDao;

@ManagedBean(name="franchiserCategory")
@ViewScoped
public class FranchiserCategory implements Serializable{
	private static final long serialVersionUID = -9201081405856011379L;

	private BasicListDao basicListDao;
	private BasicListCategoryDao categoryDao;
	private BasicListItemDao basicListItemDao;
	private ProductTypeDao productTypeDao;

	private TreeNode rootNode;
	private TreeNode selectedNode;
	private String selectedNodeName;
	private TreeNode selectedNodeTreeRoot;

	private BasicListCategory selectedCategory;

	private boolean isExpanded;

	private String searchValue;

	private BasicListCategory newBasicListCategory;

	private String categoryName;
	private String currentVersion;
	private String order;
	private List<String> versionIdList;

	private List<ProductType> productTypes;
	private ProductType selectedProductType;
	private String newProductTypeName;
	private String attributeName;
	private String selectedAttributeName;
	private String oldAttributeName;
	@PostConstruct
    public void init() {
		basicListDao = new BasicListDao();
		categoryDao = new BasicListCategoryDao();
		basicListItemDao = new BasicListItemDao();
		productTypeDao = new ProductTypeDao();
		order = CategoryUtil.ORDER_BY_NAME;
		initIfHeadNull();
		initByVersionId("head");
		isExpanded = true;

		productTypes = productTypeDao.queryProductTypes();
    }

	private void initIfHeadNull(){
		List<BasicListCategory> categories = categoryDao.queryCategoriesByVersionId("head");
		if(categories.size() == 0){
			BasicListCategory basicListCategory = new BasicListCategory();
			basicListCategory.setCategoryFatherId(CategoryUtil.ROOT_FATHER_ID);
			basicListCategory.setCategoryId(IdentityUtil.randomUUID());
			basicListCategory.setCategoryName("Categories");
			basicListCategory.setCreateTime(new Date());
			basicListCategory.setVersionId("head");
			categoryDao.insertCategory(basicListCategory);
		}
		if(categories.size() == 0 || categories.size() == 1){
			RequestContext.getCurrentInstance().execute("PF('initCategory').show();");
		}
	}

	public void initByPreparedCategories() throws ParseException{
		FranchiserCategoryInit.initCategory();
		productTypes = productTypeDao.queryProductTypes();
		initByVersionId("head");
		System.out.println("here");
		RequestContext.getCurrentInstance().execute("PF('initCategory').hide();");
	}
	public void initWithoutPreparedCategories(){
		RequestContext.getCurrentInstance().execute("PF('initCategory').hide();");
	}
	private void initByVersionId(String versionId){
		rootNode= getCurrentTree(order, versionId);
		if(rootNode!=null)rootNode.setExpanded(true);


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

	public void searchCategory(){
		List<TreeNode> allNodes = CategoryUtil.getListFromTree(rootNode);
		CategoryUtil.collapseAllTree(rootNode);
		for(TreeNode treeNode : allNodes){
			treeNode.setSelected(false);
		}
		for(TreeNode treeNode : allNodes){
			if( ((Category) treeNode.getData()).getCategoryName().toLowerCase().startsWith(searchValue.toLowerCase())){
				CategoryUtil.expandCertainTree(treeNode);
				treeNode.setSelected(true);
			}
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
	public void openAddCategory(){
		if(selectedNode == null){
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Notice", "No Catecory Selected!");
	        FacesContext.getCurrentInstance().addMessage(null, message);
	        return;
		}
		newBasicListCategory = new BasicListCategory();
		RequestContext.getCurrentInstance().execute("PF('addCategory').show();");
	}

	public void addCategory(){
		BasicListCategory parentCategory = (BasicListCategory) selectedNode.getData();

		newBasicListCategory.setCategoryFatherId(parentCategory.getCategoryId());
		newBasicListCategory.setVersionId(parentCategory.getVersionId());
		newBasicListCategory.setCategoryId(IdentityUtil.randomUUID());
		newBasicListCategory.setCreateTime(new Date());

		categoryDao.insertCategory(newBasicListCategory);
		new CategoryDefaultTreeNode(newBasicListCategory, selectedNode);

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

	//Edit Category
	public void openEditCategory(){
		if(selectedNode == null){
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Notice", "No Catecory Selected!");
	        FacesContext.getCurrentInstance().addMessage(null, message);
	        return;
		}
		selectedCategory = (BasicListCategory) selectedNode.getData();
		RequestContext.getCurrentInstance().execute("PF('editCategory').show();");
	}

	public void editCategory(){
		categoryDao.updateCategory((BasicListCategory)selectedCategory);
		List<Category> categories = CategoryUtil.getCategoriesFormTree(selectedNode);

		for(Category category : categories){
			category.setProductTypeId(selectedCategory.getProductTypeId());
			categoryDao.updateCategory((BasicListCategory)category);
		}

		RequestContext.getCurrentInstance().execute("PF('editCategory').hide();");
	}

	//Manage Product Type
	public void openManageProductType(ActionEvent actionEvent){
		productTypes = productTypeDao.queryProductTypes();
		RequestContext.getCurrentInstance().execute("PF('manageProductType').show();");
	}

	public void viewProductType(){
	}

	//Add Product Type
	public void openAddProductType(){
		newProductTypeName = null;
		RequestContext.getCurrentInstance().execute("PF('addProductType').show();");
	}

	public void addProductType(){
		ProductType productType = new ProductType();
		productType.setProductTypeId(IdentityUtil.randomUUID());
		productType.setProductTypeName(newProductTypeName);
		productTypeDao.insertProductType(productType);
		productTypes.add(productType);
		RequestContext.getCurrentInstance().execute("PF('addProductType').hide();");
	}

	//Add Attribute
	public void openAddAttribute(ActionEvent actionEvent){
		attributeName = null;
		RequestContext.getCurrentInstance().execute("PF('addAttribute').show();");
	}

	public void addAttribute(ActionEvent actionEvent){
		ArrayList<String> attributes = selectedProductType.getAdditionalInformationLable();
		if(attributes != null){
			attributes.add(attributeName);
		}else {
			attributes = new ArrayList<String>();
			attributes.add(attributeName);
		}

		selectedProductType.setAdditionalInformationLable(attributes);
		productTypeDao.updateProductType(selectedProductType);
		RequestContext.getCurrentInstance().execute("PF('addAttribute').hide();");
	}

	public void openDeleteAttribute(){
		RequestContext.getCurrentInstance().execute("PF('deleteAttribute').show();");
	}

	public void deleteAttribute(){
		ArrayList<String> attributes = selectedProductType.getAdditionalInformationLable();
		attributes.remove(selectedAttributeName);
		selectedProductType.setAdditionalInformationLable(attributes);
		productTypeDao.updateProductType(selectedProductType);
		RequestContext.getCurrentInstance().execute("PF('deleteAttribute').hide();");
	}

	public void deleteAttributeCancel(){
		RequestContext.getCurrentInstance().execute("PF('deleteAttribute').hide();");
	}

	public void openEditAttribute(){
		oldAttributeName = selectedAttributeName;
		RequestContext.getCurrentInstance().execute("PF('editAttribute').show();");
	}

	public void editAttribute(){
		ArrayList<String> attributes = selectedProductType.getAdditionalInformationLable();
		for(int index = 0; index < attributes.size(); index++){
			if(attributes.get(index).equals(oldAttributeName)){
				attributes.set(index, selectedAttributeName);
			}
		}
		selectedProductType.setAdditionalInformationLable(attributes);
		productTypeDao.updateProductType(selectedProductType);
		RequestContext.getCurrentInstance().execute("PF('addAttribute').hide();");
	}



	public void expandAllTree(){
		CategoryUtil.expandAllTree(rootNode);
		isExpanded = true;
	}
	public void collapseAllTree(){
		CategoryUtil.collapseAllTree(rootNode);
		isExpanded = false;
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
		selectedNodeTreeRoot.setExpanded(true);
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

	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}

	public boolean getIsExpanded() {
		return isExpanded;
	}

	public void setIsExpanded(boolean isExpanded) {
		this.isExpanded = isExpanded;
	}

	public void setSelectedNodeTreeRoot(TreeNode selectedNodeTreeRoot) {
		this.selectedNodeTreeRoot = selectedNodeTreeRoot;
	}

	public List<ProductType> getProductTypes() {
		return productTypes;
	}

	public void setProductTypes(List<ProductType> productTypes) {
		this.productTypes = productTypes;
	}

	public ProductType getSelectedProductType() {
		return selectedProductType;
	}

	public void setSelectedProductType(ProductType selectedProductType) {
		this.selectedProductType = selectedProductType;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getSelectedAttributeName() {
		return selectedAttributeName;
	}

	public void setSelectedAttributeName(String selectedAttributeName) {
		this.selectedAttributeName = selectedAttributeName;
	}

	public String getOldAttributeName() {
		return oldAttributeName;
	}

	public void setOldAttributeName(String oldAttributeName) {
		this.oldAttributeName = oldAttributeName;
	}

	public String getNewProductTypeName() {
		return newProductTypeName;
	}

	public void setNewProductTypeName(String newProductTypeName) {
		this.newProductTypeName = newProductTypeName;
	}

	public BasicListCategory getSelectedCategory() {
		return selectedCategory;
	}

	public void setSelectedCategory(BasicListCategory selectedCategory) {
		this.selectedCategory = selectedCategory;
	}

	public BasicListCategory getNewBasicListCategory() {
		return newBasicListCategory;
	}

	public void setNewBasicListCategory(BasicListCategory newBasicListCategory) {
		this.newBasicListCategory = newBasicListCategory;
	}





}
