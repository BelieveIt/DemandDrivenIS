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

import merchandise.utils.CategoryUtil;
import merchandise.utils.ProductUtil;
import model.BasicList;
import model.BasicListCategory;
import model.BasicListItem;
import model.Category;
import model.Product;

import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.TreeNode;

import utils.FileUpload;
import utils.IdentityUtil;

import dao.BasicListCategoryDao;
import dao.BasicListDao;
import dao.BasicListItemDao;

@ManagedBean(name="franchiserProduct")
@ViewScoped
public class FranchiserProduct implements Serializable{
	private static final long serialVersionUID = 1834613278230576950L;
	
	private BasicListCategoryDao categoryDao;
	private BasicListItemDao basicListItemDao;
	private BasicListDao basicListDao;
	
	private TreeNode rootNode;
	private TreeNode selectedNode;

	private List<BasicListItem> basicListItems;
	private BasicListItem selectedBasicListItem;
	private List<BasicListItem> selectedItems;
	private List<BasicListItem> filteredItems;
	
	private String currentVersion;
	private Product newProduct;
	private List<String> versionIdList;
	@PostConstruct
    public void init() {
		categoryDao = new BasicListCategoryDao();
		basicListItemDao = new BasicListItemDao();
		basicListDao = new BasicListDao();
		currentVersion = "head";
		
		initBasicListItemsByVersionId(currentVersion);
    }
	
	public void initBasicListItemsByVersionId(String versionId){
		List<BasicListCategory> categories = categoryDao.queryCategoriesByVersionId(versionId);
		rootNode = CategoryUtil.generateTreeForProduct(categories);	
		CategoryUtil.expandAllTree(rootNode);
		rootNode.getChildren().get(0).setSelected(true);
		selectedNode = rootNode.getChildren().get(0);
		
        List<BasicListItem> list = basicListItemDao.queryProductsByVersionId(currentVersion);
        basicListItems = ProductUtil.generateBasicListItemsBySelectedNode(list, selectedNode);
        selectedBasicListItem = null;
        selectedItems = null;
        filteredItems = null;
        
        newProduct = new Product();
		List<BasicList> basicLists = basicListDao.queryBasicLists();
		versionIdList = new ArrayList<String>();
		versionIdList.add("head");
		for(BasicList basicList : basicLists){
			versionIdList.add(basicList.getVersionId());
		}
	}
	public void onNodeSelect(NodeSelectEvent event) {
        selectedNode = event.getTreeNode();
        List<BasicListItem> list = basicListItemDao.queryProductsByVersionId(currentVersion);
        basicListItems = ProductUtil.generateBasicListItemsBySelectedNode(list, selectedNode);
        initTable();
    }
	//Add Product
	public void openAddProduct(ActionEvent actionEvent){
		if(selectedNode == null){
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Notice", "No Category Selected!");
	        FacesContext.getCurrentInstance().addMessage(null, message);
	        return;
		}
		if(!CategoryUtil.isLeafNode(selectedNode)){
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Notice", "The Category includes categories.");
	        FacesContext.getCurrentInstance().addMessage(null, message);
	        return;
		}
		newProduct = new Product();
		RequestContext.getCurrentInstance().execute("PF('addProduct').show();");
	}
	public void addProduct(ActionEvent actionEvent){
		BasicListItem basicListItem = new BasicListItem();
		Category category = (Category) selectedNode.getData();
		basicListItem.setCategoryId(category.getCategoryId());
		basicListItem.setProductId(IdentityUtil.randomUUID());
		basicListItem.setVersionId(currentVersion);
		basicListItem.setProduct(newProduct);
		newProduct.setProductCreateTime(new Date());
		basicListItemDao.insertProduct(basicListItem);
		basicListItems.add(basicListItem);
		RequestContext.getCurrentInstance().execute("PF('addProduct').hide();");
	}

	//Delete Products
	public void openDeleteProducts(ActionEvent actionEvent){
		RequestContext.getCurrentInstance().execute("PF('deleteProducts').show();");
	}
	
	public void deleteProducts(ActionEvent actionEvent){
		for(BasicListItem item : selectedItems){
			basicListItemDao.deleteProduct(item);
		}
		deleteProductsFromTable(selectedItems);
		RequestContext.getCurrentInstance().execute("PF('deleteProducts').hide();");
	}
	
	//Delete Product
	public void openDeleteProduct(ActionEvent actionEvent){
		RequestContext.getCurrentInstance().execute("PF('deleteProduct').show();");
	}
	
	public void deleteProduct(ActionEvent actionEvent){
		basicListItemDao.deleteProduct(selectedBasicListItem);
		deleteProductFromTable(selectedBasicListItem);
		RequestContext.getCurrentInstance().execute("PF('deleteProduct').hide();");
	}
	
	//View Product
	public void openViewProduct(ActionEvent actionEvent){
		RequestContext.getCurrentInstance().execute("PF('viewProduct').show();");
	}
	
	//Edit Product
	public void openEditProduct(ActionEvent actionEvent){
		RequestContext.getCurrentInstance().execute("PF('editProduct').show();");
	}
	
	//Change Version
	public void changeVersion(){
		initBasicListItemsByVersionId(currentVersion);
	}
	
	public void uploadProductImage(FileUploadEvent event){
		newProduct.setImage(FileUpload.handleFileUpload(event));
	}
	
	private void initTable(){
		selectedItems = null;
		filteredItems = null;
	}
	private void deleteProductFromTable(BasicListItem basicListItem){
		if(selectedItems != null) selectedItems.remove(basicListItem);
		if(filteredItems != null) filteredItems.remove(basicListItem);
		if(basicListItems != null) basicListItems.remove(basicListItem);
		selectedBasicListItem = null;
	}
	private void deleteProductsFromTable(List<BasicListItem> itemList){
		if(filteredItems != null){
			for(BasicListItem item : itemList){
				filteredItems.remove(item);
			}
		}
		if(basicListItems != null) {
			for(BasicListItem item : itemList){
				basicListItems.remove(item);
			}
		}		
		selectedItems = null;
		selectedBasicListItem = null;
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
	public Product getNewProduct() {
		return newProduct;
	}
	public void setNewProduct(Product newProduct) {
		this.newProduct = newProduct;
	}
	public BasicListItemDao getBasicListItemDao() {
		return basicListItemDao;
	}
	public void setBasicListItemDao(BasicListItemDao basicListItemDao) {
		this.basicListItemDao = basicListItemDao;
	}
	public List<BasicListItem> getBasicListItems() {
		return basicListItems;
	}
	public void setBasicListItems(List<BasicListItem> basicListItems) {
		this.basicListItems = basicListItems;
	}
	public BasicListItem getSelectedBasicListItem() {
		return selectedBasicListItem;
	}
	public void setSelectedBasicListItem(BasicListItem selectedBasicListItem) {
		this.selectedBasicListItem = selectedBasicListItem;
	}
	public String getCurrentVersion() {
		return currentVersion;
	}
	public void setCurrentVersion(String currentVersion) {
		this.currentVersion = currentVersion;
	}
	public List<BasicListItem> getSelectedItems() {
		return selectedItems;
	}
	public void setSelectedItems(List<BasicListItem> selectedItems) {
		this.selectedItems = selectedItems;
	}
	public List<BasicListItem> getFilteredItems() {
		return filteredItems;
	}
	public void setFilteredItems(List<BasicListItem> filteredItems) {
		this.filteredItems = filteredItems;
	}

	public List<String> getVersionIdList() {
		return versionIdList;
	}

	public void setVersionIdList(List<String> versionIdList) {
		this.versionIdList = versionIdList;
	}
}
