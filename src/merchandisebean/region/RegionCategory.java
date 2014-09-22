package merchandisebean.region;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import merchandise.utils.CategoryUtil;
import model.Category;
import model.RegionListCategory;

import org.primefaces.model.TreeNode;

import dao.RegionListCategoryDao;

@ManagedBean(name="regionCategory")
@ViewScoped
public class RegionCategory {
	private RegionListCategoryDao categoryDao;

	private TreeNode rootNode;
	private TreeNode selectedNode;
	private String selectedNodeName;

	private String currentVersion;

	private String currentRegionId;

	@PostConstruct
	public void init(){
		//TODO
		currentRegionId = "1";
		categoryDao = new RegionListCategoryDao();
	}

	public void initByVersionId(String versionId){
		rootNode = getCurrentTree(CategoryUtil.ORDER_BY_NAME, versionId);
		CategoryUtil.expandAllTree(rootNode);
		selectedNode = null;
		selectedNodeName = null;

		setCurrentVersion(versionId);
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

}
