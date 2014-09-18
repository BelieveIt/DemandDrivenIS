package merchandise.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.Category;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;


public class CategoryUtil {
public static TreeNode generateTree(List<? extends Category> categories){
	TreeNode rootNode;
	for(Category cate : categories){
		if(cate.getCategoryFatherId().equals("0")){
			 rootNode = new DefaultTreeNode(cate, null);
			 generateTreeRecursion(rootNode, categories);
			 return rootNode;
		}
	}
	return null;
}
private static void generateTreeRecursion(TreeNode node, List<? extends Category> categories){
	for (Category cate : categories) {
		if(cate.getCategoryFatherId().equals(((Category) node.getData()).getCategoryId())){
			TreeNode newNode = new DefaultTreeNode(cate, node);
			generateTreeRecursion(newNode, categories);
		}
	}
}

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

public static void expendTree(TreeNode rootNode, ArrayList<String> expandIds){
	List<TreeNode> treeNodes = getListFromTree(rootNode);
	for(TreeNode node : treeNodes){
		Category currentCategory = (Category) node.getData();
		if(expandIds.contains(currentCategory.getCategoryId())){
			node.setExpanded(true);
			System.out.println("ex");
		}
	}
}

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

public static List<TreeNode> getListFromTree(TreeNode rootNode){
	List<TreeNode> list = new ArrayList<TreeNode>();
	visitNode(rootNode, list);
	return list;
}
public static void visitNode(TreeNode rootNode, List<TreeNode> list){
	list.add(rootNode);
	List<TreeNode> treeNodes = rootNode.getChildren();
	for(int i = 0; i < treeNodes.size(); i++){
		visitNode(treeNodes.get(i), list);
	}
}

public static void addNode(){

}

public static void deleteNode(){

}

}
