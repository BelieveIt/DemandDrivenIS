package merchandise.utils;

import java.util.Comparator;

import model.Category;

import org.primefaces.model.TreeNode;

public class CategoryTreeNodeByCreatTimeComparator implements Comparator<TreeNode> {
		  public int compare(TreeNode n1, TreeNode n2) {
			Category category1 = (Category) n1.getData();
			Category category2 = (Category) n2.getData();
		    return category1.getCreateTime().compareTo(category2.getCreateTime());
		  }

}
