package merchandisebean.franchiser;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import merchandise.utils.CategoryUtil;
import model.BasicListCategory;

import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.TreeNode;

import dao.BasicListCategoryDao;

@ManagedBean(name="franchiserCategory")
@ViewScoped
public class FranchiserCategory implements Serializable{
	private static final long serialVersionUID = -9201081405856011379L;
	private TreeNode rootNode;
	private TreeNode selectedNode;
	private BasicListCategoryDao categoryDao;

	@PostConstruct
    public void init() {
		categoryDao = new BasicListCategoryDao();
		rootNode= getCurrentTree();
    }

	public void onNodeSelect(NodeSelectEvent event) {
        selectedNode = event.getTreeNode();
    }
	public void openAddCategory(ActionEvent actionEvent){
		System.out.println("here");
		if(selectedNode == null){
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Notice", "No Catecory Selected!");
	        FacesContext.getCurrentInstance().addMessage(null, message);
	        return;
		}
		RequestContext.getCurrentInstance().execute("PF('addCategory').show();");
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

}
