package merchandise.utils;

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

public static void expendTree(TreeNode expendNode){
	TreeNode currentNode = expendNode;
	expendNode.setExpanded(true);
	while(true){
		TreeNode parentNode = currentNode.getParent();
		if(parentNode == null){
			return;
		}
		parentNode.setExpanded(true);
		currentNode = parentNode;
	}
}

public static void addNode(){

}

public static void deleteNode(){

}

}
