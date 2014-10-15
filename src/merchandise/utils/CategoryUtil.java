package merchandise.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import model.BasicListCategory;
import model.Category;
import model.RegionListCategory;

import org.primefaces.model.TreeNode;

import dao.BasicListCategoryDao;
import dao.RegionListCategoryDao;


public class CategoryUtil {

public static String ORDER_BY_NAME = "name";
public static String ORDER_BY_TIME = "time";
public static String ROOT_FATHER_ID = "0";
public static String ROOT_FOR_PRODUCT_ID="-1";
public static String NOT_CLASSIFIED_FOR_PRODUCT_ID="-2";

//Generate Vertical Tree from Category List
public static TreeNode generateTree(List<? extends Category> categories, String order, String rootCategoryId){
	TreeNode rootNode;
	for(Category cate : categories){
		if(cate.getCategoryFatherId().equals(rootCategoryId)){
			 rootNode = new CategoryDefaultTreeNode(cate, null);
			 generateTreeRecursion(rootNode, categories);
			 if(order.equals(ORDER_BY_NAME))((CategoryDefaultTreeNode) rootNode).sortByCategoryName();
			 if(order.equals(ORDER_BY_TIME))((CategoryDefaultTreeNode) rootNode).sortByCreateTime();
			 setParentNode(rootNode);
			 return rootNode;
		}
	}
	return null;
}

//Generate Left Menu Tree from Category List
public static TreeNode generateTreeForProduct(List<? extends Category> categories){
	Category rootCategory = new Category();
	rootCategory.setCategoryId(ROOT_FOR_PRODUCT_ID);
	rootCategory.setCategoryName("root");
	Category notClassifiedCategory = new Category();
	notClassifiedCategory.setCategoryId(NOT_CLASSIFIED_FOR_PRODUCT_ID);
	notClassifiedCategory.setCategoryName("Not Classified");
	TreeNode rootNode = new CategoryDefaultTreeNode(rootCategory, null);
	new CategoryDefaultTreeNode(notClassifiedCategory, rootNode);
	for(Category cate : categories){
		if(cate.getCategoryFatherId().equals(ROOT_FATHER_ID)){
			 TreeNode classifiedNode = new CategoryDefaultTreeNode(cate, rootNode);
			 generateTreeRecursion(classifiedNode, categories);
			 ((CategoryDefaultTreeNode) classifiedNode).sortByCategoryName();
			 return rootNode;
		}
	}
	return rootNode;
}

//Set Parent for Every Node
public static void setParentNode(TreeNode rootNode){
	List<TreeNode> treeNodes = rootNode.getChildren();
	for(int i = 0; i < treeNodes.size(); i++){
		treeNodes.get(i).setParent(rootNode);
		setParentNode(treeNodes.get(i));
	}
}

//Get expended node's categoryId list
public static ArrayList<String> getExpendList(TreeNode rootNode){
	List<TreeNode> treeNodes = getListFromTree(rootNode);
	ArrayList<String> expandIds = new ArrayList<String>();
	for(TreeNode node : treeNodes){
		Category currentCategory = (Category) node.getData();
		if(node.isExpanded()){
			expandIds.add(currentCategory.getCategoryId());
		}
	}
	return expandIds;
}

//Expend tree according expended node's list
public static void expandTree(TreeNode rootNode, ArrayList<String> expandIds){
	List<TreeNode> treeNodes = getListFromTree(rootNode);
	for(TreeNode node : treeNodes){
		Category currentCategory = (Category) node.getData();
		if(expandIds.contains(currentCategory.getCategoryId())){
			node.setExpanded(true);
		}
	}
}

public static void expandAllTree(TreeNode rootNode){
	List<TreeNode> treeNodes = getListFromTree(rootNode);
	for(TreeNode node : treeNodes){
			node.setExpanded(true);
	}
}

public static void expandCertainTree(TreeNode node){
	List<TreeNode> fathers = new ArrayList<TreeNode>();
	while(node.getParent() != null){
		fathers.add(node.getParent());
		node = node.getParent();
	}
	for(TreeNode n : fathers){
		n.setExpanded(true);
}
}
public static void collapseAllTree(TreeNode rootNode){
	List<TreeNode> treeNodes = getListFromTree(rootNode);
	for(TreeNode node : treeNodes){
			node.setExpanded(false);
	}
}

//Query node by categoryId
public static TreeNode queryNode(TreeNode rootNode, String categoryId){
	Category category = (Category) rootNode.getData();
	if(category.getCategoryId().equals(categoryId)) return rootNode;
	List<TreeNode> treeNodes = rootNode.getChildren();
	TreeNode node = null;
	for(int i = 0; node == null && i < treeNodes.size(); i++){
		node = queryNode(treeNodes.get(i), categoryId);
	}
	return node;
}

public static TreeNode restoreTreeStatus(TreeNode oldRootTree, TreeNode newRootTree,
		TreeNode oldSelectedNode){
	ArrayList<String> expandIds = CategoryUtil.getExpendList(oldRootTree);
	Category oldSelectedCategory = (Category) oldSelectedNode.getData();
	expandIds.add(oldSelectedCategory.getCategoryId());

	TreeNode newSelectedNode = CategoryUtil.queryNode(newRootTree, oldSelectedCategory.getCategoryId());
	if(newSelectedNode != null) newSelectedNode.setSelected(true);

	expandTree(newRootTree, expandIds);
	return newRootTree;
}

//Check isLeafNode
public static boolean isLeafNode(TreeNode treeNode){
	Category category = (Category) treeNode.getData();
	if(category.getCategoryId().equals(NOT_CLASSIFIED_FOR_PRODUCT_ID)) return true;
	if(treeNode.getChildCount() > 0){
		return false;
	}else {
		return true;
	}
}

//Compare Region HEAD and version of Basic List
public static boolean compareLocalTreeAndBasicList(String regionId, String basicListVersionId){
	RegionListCategoryDao regionListCategoryDao = new RegionListCategoryDao();
	List<RegionListCategory> regionCategories = regionListCategoryDao.queryCategoriesByVersionId("head", regionId);

	BasicListCategoryDao basicListCategoryDao = new BasicListCategoryDao();
	List<BasicListCategory> basicListCategories = basicListCategoryDao.queryCategoriesByVersionId(basicListVersionId);

	if(regionCategories.size() != basicListCategories.size()){
		return false;
	}else {
		for(int i = 0; i < regionCategories.size(); i++){
			BasicListCategory basicListCategory = getIncludedInBasicCategoryList(regionCategories.get(i), basicListCategories);
			if(basicListCategory == null){
				return false;
			}

			if(!(regionCategories.get(i).getProductTypeId() == null && basicListCategory.getProductTypeId() == null)){
				if((regionCategories.get(i).getProductTypeId() == null && basicListCategory.getProductTypeId() != null)
						||(regionCategories.get(i).getProductTypeId() != null && basicListCategory.getProductTypeId() == null)
						|| !regionCategories.get(i).getProductTypeId().equals(basicListCategory.getProductTypeId())){
					return false;
				}

			}
			if(!(regionCategories.get(i).getDescription() == null && basicListCategory.getDescription() == null)){
				if(!regionCategories.get(i).getDescription().equals(basicListCategory.getDescription())
						||(regionCategories.get(i).getDescription() != null && basicListCategory.getDescription() == null)
						|| !regionCategories.get(i).getDescription().equals(basicListCategory.getDescription())){
					return false;
				}
			}
		}
	}
	return true;
}

public static BasicListCategory getIncludedInBasicCategoryList(Category category, List<BasicListCategory> list){
	for(BasicListCategory basicListCategory : list){
		if(category.getCategoryId().equals(basicListCategory.getCategoryId())){
			return basicListCategory;
		}
	}
	return null;
}

//Get All Categories in Tree without Order
public static List<Category> getCategoriesFormTree(TreeNode rootNode){
	List<TreeNode> list = new ArrayList<TreeNode>();
	visitNode(rootNode, list);
	List<Category> categories = new ArrayList<Category>();
	for(TreeNode treeNode : list){
		categories.add((Category) treeNode.getData());
	}
	return categories;
}

//Get All Categories in Tree by Name Order
public static void getCategoriesByTreeOrder(List<Category> categories, TreeNode root){
	LinkedList<TreeNode> nodes = new LinkedList<TreeNode>();
	nodes.offer(root);
	while (nodes.size() != 0) {
		TreeNode currentNode = nodes.poll();
		Category currentCategory = (Category) currentNode.getData();
		categories.add(currentCategory);
		List<TreeNode> childsOfCurrent = currentNode.getChildren();
		for(TreeNode node : childsOfCurrent){
			nodes.offer(node);
		}
	}
}

//Get All Tree Node in Tree
public static List<TreeNode> getListFromTree(TreeNode rootNode){
	List<TreeNode> list = new ArrayList<TreeNode>();
	visitNode(rootNode, list);
	return list;
}
private static void visitNode(TreeNode rootNode, List<TreeNode> list){
	if(rootNode==null)return;
	list.add(rootNode);
	List<TreeNode> treeNodes = rootNode.getChildren();
	for(int i = 0; i < treeNodes.size(); i++){
		visitNode(treeNodes.get(i), list);
	}
}
private static void generateTreeRecursion(TreeNode node, List<? extends Category> categories){
	for (Category cate : categories) {
		if(cate.getCategoryFatherId().equals(((Category) node.getData()).getCategoryId())){
			TreeNode newNode = new CategoryDefaultTreeNode(cate, node);
			newNode.setParent(node);
			generateTreeRecursion(newNode, categories);
		}
	}
}
public static void addNode(){

}

public static void deleteNode(){

}

}
