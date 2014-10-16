package merchandisebean.region;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import merchandise.utils.CategoryUtil;
import merchandise.utils.ProductUtil;
import merchandise.utils.RegionListItemDiff;
import model.AdditionalInfoItem;
import model.Category;
import model.Product;
import model.ProductType;
import model.RegionListCategory;
import model.RegionListItem;
import model.RegionListUpdateInfo;

import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.TreeNode;

import utils.FileUpload;

import dao.ProductTypeDao;
import dao.RegionListCategoryDao;
import dao.RegionListItemDao;
import dao.RegionListUpdateInfoDao;

@ManagedBean(name="regionProduct")
@ViewScoped
public class RegionProduct implements Serializable{
	private static final long serialVersionUID = -5994527968580302084L;
	private RegionListCategoryDao categoryDao;
	private RegionListItemDao regionListItemDao;
	private RegionListUpdateInfoDao regionListUpdateInfoDao;
	private ProductTypeDao productTypeDao;
	private RegionListCategoryDao regionListCategoryDao;

	private RegionListUpdateInfo regionListUpdateInfo;

	private TreeNode rootNode;
	private TreeNode selectedNode;

	private String currentRegionId;

	private List<RegionListItem> regionListItems;
	private RegionListItem selectedRegionListItem;
	private List<RegionListItem> selectedItems;
	private List<RegionListItem> filteredItems;

	private Product newProduct;

	private RegionListItemDiff regionListItemDiff;
	private List<RegionListItem> selectedItemsForAddition;
	private List<RegionListItem> selectedItemsForEdit;
	private List<RegionListItem> selectedItemsForDeletion;

	private RegionListItem selectedForEdit;
	private RegionListItem selectedForEditHead;

	private String leftMenuUpdateAlert;

	private RegionListItem selectedViewItem;
	@PostConstruct
    public void init() {
		//TODO
		currentRegionId = "1";
		categoryDao = new RegionListCategoryDao();
		regionListItemDao = new RegionListItemDao();
		regionListUpdateInfoDao = new RegionListUpdateInfoDao();
		productTypeDao = new ProductTypeDao();
		regionListCategoryDao = new RegionListCategoryDao();

		List<RegionListUpdateInfo> regionListUpdateInfos= regionListUpdateInfoDao.queryRegionListUpdateInfosByRegionId(currentRegionId);
		if(regionListUpdateInfos!=null && regionListUpdateInfos.size()!=0)regionListUpdateInfo = regionListUpdateInfos.get(0);
		initRegionListItemsByVersionId("head");
		initRegionListItemDiff();
    }

	public void initRegionListItemsByVersionId(String versionId){
		List<RegionListCategory> categories = categoryDao.queryCategoriesByVersionId(versionId, currentRegionId);
		rootNode = CategoryUtil.generateTreeForProduct(categories);
		if(rootNode !=null){
			if(rootNode.getChildCount() > 1){
				rootNode.getChildren().get(1).setExpanded(true);
				rootNode.getChildren().get(1).setSelected(true);
				selectedNode = rootNode.getChildren().get(1);
			}else {
				rootNode.getChildren().get(0).setSelected(true);
				selectedNode = rootNode.getChildren().get(0);
			}


	        List<RegionListItem> list = regionListItemDao.queryProductsByVersionIdAndRegionId(currentRegionId, versionId);
	        regionListItems = ProductUtil.generateRegionListItemsBySelectedNode(list, selectedNode, currentRegionId);
		}

        selectedRegionListItem = null;
        selectedItems = null;
        filteredItems = null;

        newProduct = new Product();
	}

	public void onNodeSelect(NodeSelectEvent event) {
        selectedNode = event.getTreeNode();
        List<RegionListItem> list = regionListItemDao.queryProductsByVersionIdAndRegionId(currentRegionId, "head");
        regionListItems = ProductUtil.generateRegionListItemsBySelectedNode(list, selectedNode, currentRegionId);
        initTable();
    }

	public void onTabChange(TabChangeEvent event) {
        selectedRegionListItem = null;
        selectedItems = null;
    }

	private void initTable(){
		selectedItems = null;
		filteredItems = null;
	}

	public void openUpdateItems(){
		RequestContext.getCurrentInstance().execute("PF('updateItems').show();");
	}

	public void updateDeletion(){
		if(selectedItemsForDeletion == null || selectedItemsForDeletion.size() == 0){
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Notice", "No Item Selected!");
	        FacesContext.getCurrentInstance().addMessage(null, message);
	        return;
		}
		for(RegionListItem regionListItem : selectedItemsForDeletion){
			regionListItemDao.deleteProduct(regionListItem);
		}

		initRegionListItemsByVersionId("head");
		initRegionListItemDiff();
	}

	public void updateAddition(){
		if(selectedItemsForAddition == null || selectedItemsForAddition.size() == 0){
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Notice", "No Item Selected!");
	        FacesContext.getCurrentInstance().addMessage(null, message);
	        return;
		}
		for(RegionListItem regionListItem : selectedItemsForAddition){
			regionListItem.setVersionId("head");
			regionListItemDao.insertProduct(regionListItem);
		}

		initRegionListItemsByVersionId("head");
		initRegionListItemDiff();
	}

	public void updateEdit(){
		if(selectedItemsForEdit == null || selectedItemsForEdit.size() == 0){
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Notice", "No Item Selected!");
	        FacesContext.getCurrentInstance().addMessage(null, message);
	        return;
		}
		for(RegionListItem regionListItem : selectedItemsForEdit){
			regionListItem.setVersionId("head");
			regionListItem.setIsConfirmed(1);
			regionListItemDao.updateProduct(regionListItem);
		}

		initRegionListItemsByVersionId("head");
		initRegionListItemDiff();
	}

	public void openEditConfirm(){
		String editProductId = selectedForEdit.getProductId();
		selectedForEditHead = regionListItemDao.queryProductByVersionIdAndRegionId(currentRegionId, "head", editProductId);

		ArrayList<AdditionalInfoItem> productAdditionalInfos = new ArrayList<AdditionalInfoItem>();
		List<String> productAdditionalInfoLabels = new ArrayList<String>();
		ProductType categoryProductType = new ProductType();

		String categoryId = selectedForEditHead.getCategoryId();
		RegionListCategory regionListCategory = regionListCategoryDao.queryCategory("head", currentRegionId, categoryId);
		categoryProductType = productTypeDao.queryProductType(regionListCategory.getProductTypeId());

		//For selectedForEditHead
		if(categoryProductType != null){
			productAdditionalInfoLabels = categoryProductType.getAdditionalInformationLable();
			for(int i = 0; i < productAdditionalInfoLabels.size(); i++){
				AdditionalInfoItem additionalInfoItem = new AdditionalInfoItem();
				additionalInfoItem.setLabel(productAdditionalInfoLabels.get(i));
				additionalInfoItem.setValue(selectedForEditHead.getProduct().getAdditionalInformation().get(i));
				productAdditionalInfos.add(additionalInfoItem);
			}
		}
		Product product = selectedForEditHead.getProduct();
		product.setAdditionalInfoItems(productAdditionalInfos);
		selectedForEditHead.setProduct(product);

		ArrayList<AdditionalInfoItem> productAdditionalInfos2 = new ArrayList<AdditionalInfoItem>();
		List<String> productAdditionalInfoLabels2 = new ArrayList<String>();
		//For selectedForEdit
		if(categoryProductType != null){
			productAdditionalInfoLabels2 = categoryProductType.getAdditionalInformationLable();
			for(int i = 0; i < productAdditionalInfoLabels2.size(); i++){
				AdditionalInfoItem additionalInfoItem = new AdditionalInfoItem();
				additionalInfoItem.setLabel(productAdditionalInfoLabels2.get(i));
				additionalInfoItem.setValue(selectedForEdit.getProduct().getAdditionalInformation().get(i));
				productAdditionalInfos2.add(additionalInfoItem);
			}
		}
		Product product2 = selectedForEdit.getProduct();
		product2.setAdditionalInfoItems(productAdditionalInfos2);
		selectedForEdit.setProduct(product2);


		RequestContext.getCurrentInstance().execute("PF('editConfirm').show();");
	}

	public void editConfirm(){
		selectedForEditHead.setVersionId("head");
		selectedForEditHead.setIsConfirmed(1);

		Product product = selectedForEditHead.getProduct();
		ArrayList<String> additionInfos = new ArrayList<String>();
		if(product.getAdditionalInfoItems() != null){
			for(AdditionalInfoItem item : product.getAdditionalInfoItems()){
				additionInfos.add(item.getValue());
			}
		}

		product.setAdditionalInformation(additionInfos);
		selectedForEditHead.setProduct(product);

		regionListItemDao.updateProduct(selectedForEditHead);
		initRegionListItemsByVersionId("head");
		initRegionListItemDiff();
		RequestContext.getCurrentInstance().execute("PF('editConfirm').hide();");
	}

	public void uploadProductImageForEditConfirm(FileUploadEvent event){
		selectedForEditHead.getProduct().setImage(FileUpload.handleFileUpload(event));
	}

	public void initRegionListItemDiff(){
		regionListItemDiff = ProductUtil.generateRegionListItemDiffForHead(currentRegionId);
		if(regionListItemDiff.getAddedItems().size() + regionListItemDiff.getDeletedItems().size() + regionListItemDiff.getEditedItems().size() > 0){
			leftMenuUpdateAlert = "Update";
		}else {
			leftMenuUpdateAlert = null;
		}
	}

	public void viewItemDetail(){
		RequestContext.getCurrentInstance().execute("PF('viewItemDetail').show();");
	}
	//View Product
	public void openViewProduct(){
		ArrayList<AdditionalInfoItem> productAdditionalInfos = new ArrayList<AdditionalInfoItem>();
		List<String> productAdditionalInfoLabels = new ArrayList<String>();
		List<Category> categories = CategoryUtil.getCategoriesFormTree(selectedNode);
		ProductType categoryProductType = new ProductType();
		for(Category category : categories){
			if(category.getCategoryId().equals(selectedViewItem.getCategoryId())){
				categoryProductType = productTypeDao.queryProductType(category.getProductTypeId());
				break;
			}
		}
		if(categoryProductType != null){
			productAdditionalInfoLabels = categoryProductType.getAdditionalInformationLable();
			for(int i = 0; i < productAdditionalInfoLabels.size(); i++){
				AdditionalInfoItem additionalInfoItem = new AdditionalInfoItem();
				additionalInfoItem.setLabel(productAdditionalInfoLabels.get(i));
				additionalInfoItem.setValue(selectedViewItem.getProduct().getAdditionalInformation().get(i));
				productAdditionalInfos.add(additionalInfoItem);
			}
		}
		Product product = selectedViewItem.getProduct();
		product.setAdditionalInfoItems(productAdditionalInfos);
		selectedViewItem.setProduct(product);
		RequestContext.getCurrentInstance().execute("PF('viewItemDetail').show();");
	}

	//View Product
	public void openEditProduct(){
		ArrayList<AdditionalInfoItem> productAdditionalInfos = new ArrayList<AdditionalInfoItem>();
		List<String> productAdditionalInfoLabels = new ArrayList<String>();
		List<Category> categories = CategoryUtil.getCategoriesFormTree(selectedNode);
		ProductType categoryProductType = new ProductType();
		for(Category category : categories){
			if(category.getCategoryId().equals(selectedViewItem.getCategoryId())){
				categoryProductType = productTypeDao.queryProductType(category.getProductTypeId());
				break;
			}
		}
		if(categoryProductType != null){
			productAdditionalInfoLabels = categoryProductType.getAdditionalInformationLable();
			for(int i = 0; i < productAdditionalInfoLabels.size(); i++){
				AdditionalInfoItem additionalInfoItem = new AdditionalInfoItem();
				additionalInfoItem.setLabel(productAdditionalInfoLabels.get(i));
				additionalInfoItem.setValue(selectedViewItem.getProduct().getAdditionalInformation().get(i));
				productAdditionalInfos.add(additionalInfoItem);
			}
		}
		Product product = selectedViewItem.getProduct();
		product.setAdditionalInfoItems(productAdditionalInfos);
		selectedViewItem.setProduct(product);
		RequestContext.getCurrentInstance().execute("PF('editItemDetail').show();");
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
	public List<RegionListItem> getRegionListItems() {
		return regionListItems;
	}
	public void setRegionListItems(List<RegionListItem> regionListItems) {
		this.regionListItems = regionListItems;
	}
	public RegionListItem getSelectedRegionListItem() {
		return selectedRegionListItem;
	}
	public void setSelectedRegionListItem(RegionListItem selectedRegionListItem) {
		this.selectedRegionListItem = selectedRegionListItem;
	}
	public List<RegionListItem> getSelectedItems() {
		return selectedItems;
	}
	public void setSelectedItems(List<RegionListItem> selectedItems) {
		this.selectedItems = selectedItems;
	}
	public List<RegionListItem> getFilteredItems() {
		return filteredItems;
	}
	public void setFilteredItems(List<RegionListItem> filteredItems) {
		this.filteredItems = filteredItems;
	}
	public Product getNewProduct() {
		return newProduct;
	}
	public void setNewProduct(Product newProduct) {
		this.newProduct = newProduct;
	}

	public String getCurrentRegionId() {
		return currentRegionId;
	}

	public void setCurrentRegionId(String currentRegionId) {
		this.currentRegionId = currentRegionId;
	}

	public RegionListUpdateInfo getRegionListUpdateInfo() {
		return regionListUpdateInfo;
	}

	public void setRegionListUpdateInfo(RegionListUpdateInfo regionListUpdateInfo) {
		this.regionListUpdateInfo = regionListUpdateInfo;
	}

	public RegionListItemDiff getRegionListItemDiff() {
		return regionListItemDiff;
	}

	public void setRegionListItemDiff(RegionListItemDiff regionListItemDiff) {
		this.regionListItemDiff = regionListItemDiff;
	}

	public List<RegionListItem> getSelectedItemsForAddition() {
		return selectedItemsForAddition;
	}

	public void setSelectedItemsForAddition(List<RegionListItem> selectedItemsForAddtion) {
		this.selectedItemsForAddition = selectedItemsForAddtion;
	}

	public List<RegionListItem> getSelectedItemsForEdit() {
		return selectedItemsForEdit;
	}

	public void setSelectedItemsForEdit(List<RegionListItem> selectedItemsForEdit) {
		this.selectedItemsForEdit = selectedItemsForEdit;
	}

	public List<RegionListItem> getSelectedItemsForDeletion() {
		return selectedItemsForDeletion;
	}

	public void setSelectedItemsForDeletion(List<RegionListItem> selectedItemsForDeletion) {
		this.selectedItemsForDeletion = selectedItemsForDeletion;
	}

	public String getLeftMenuUpdateAlert() {
		return leftMenuUpdateAlert;
	}

	public void setLeftMenuUpdateAlert(String leftMenuUpdateAlert) {
		this.leftMenuUpdateAlert = leftMenuUpdateAlert;
	}

	public RegionListItem getSelectedForEdit() {
		return selectedForEdit;
	}

	public void setSelectedForEdit(RegionListItem selectedForEdit) {
		this.selectedForEdit = selectedForEdit;
	}

	public RegionListItem getSelectedForEditHead() {
		return selectedForEditHead;
	}

	public void setSelectedForEditHead(RegionListItem selectedForEditHead) {
		this.selectedForEditHead = selectedForEditHead;
	}

	public RegionListItem getSelectedViewItem() {
		return selectedViewItem;
	}

	public void setSelectedViewItem(RegionListItem selectedViewItem) {
		this.selectedViewItem = selectedViewItem;
	}

}
