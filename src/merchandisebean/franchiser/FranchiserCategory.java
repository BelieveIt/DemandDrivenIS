package merchandisebean.franchiser;

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
import model.BasicListCategory;
import model.Category;

import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import com.sun.xml.rpc.processor.modeler.j2ee.xml.string;

import utils.IdentityUtil;

import dao.BasicListCategoryDao;

@ManagedBean(name="franchiserCategory")
@ViewScoped
public class FranchiserCategory implements Serializable{
	private static final long serialVersionUID = -9201081405856011379L;
	private TreeNode rootNode;
	private TreeNode selectedNode;
	private BasicListCategoryDao categoryDao;
	
	private String categoryName;

	@PostConstruct
    public void init() {
		categoryDao = new BasicListCategoryDao();
		rootNode= getCurrentTree();
    }

	public void onNodeSelect(NodeSelectEvent event) {
        selectedNode = event.getTreeNode();
    }
	public void onNodeExpand(NodeExpandEvent event) {
        event.getTreeNode().setExpanded(true);
    }
	public void openAddCategory(ActionEvent actionEvent){
		if(selectedNode == null){
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Notice", "No Catecory Selected!");
	        FacesContext.getCurrentInstance().addMessage(null, message);
	        return;
		}
		RequestContext.getCurrentInstance().execute("PF('addCategory').show();");
	}
	public void addCategory(ActionEvent actionEvent){
		BasicListCategory basicListCategory = new BasicListCategory();
		BasicListCategory parentCategory = (BasicListCategory) selectedNode.getData();
		
		basicListCategory.setCategoryFatherId(parentCategory.getCategoryId());
		basicListCategory.setCategoryName(categoryName);
		basicListCategory.setVersionId(parentCategory.getVersionId());
		basicListCategory.setCategoryId(IdentityUtil.randomString(10));
		
		categoryDao.insertCategory(basicListCategory);
		new DefaultTreeNode(basicListCategory, selectedNode);
		
		ArrayList<String> expandIds = CategoryUtil.getExpendList(rootNode);
 		rootNode= getCurrentTree();
		
		Category selectedCategory = (Category) selectedNode.getData();
		selectedNode = CategoryUtil.queryNode(rootNode, selectedCategory.getCategoryId());
		selectedNode.setSelected(true);
		CategoryUtil.expendTree(rootNode, expandIds);
		RequestContext.getCurrentInstance().execute("PF('addCategory').hide();");
		
	}
	
	
	private TreeNode getCurrentTree(){
		List<BasicListCategory> categories = categoryDao.queryCategoriesByVersionId("1");
		return CategoryUtil.generateTree(categories);
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

}
