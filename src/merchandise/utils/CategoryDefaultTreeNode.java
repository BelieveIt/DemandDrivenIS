package merchandise.utils;

import java.util.Collections;

import model.Category;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

public class CategoryDefaultTreeNode extends DefaultTreeNode {
	private static final long serialVersionUID = -6890765610500284605L;

	public CategoryDefaultTreeNode(Category category,
			TreeNode node) {
		super(category, node);
	}

	public void sortByCategoryName() {
		    CategoryTreeNodeByNameComparator comparator = new CategoryTreeNodeByNameComparator();
		    Collections.sort(this.getChildren(), comparator);
		    for (TreeNode child : this.getChildren()) {
		      ((CategoryDefaultTreeNode) child).sortByCategoryName();
		    }
	}

	public void sortByCreateTime() {
	    CategoryTreeNodeByCreatTimeComparator comparator = new CategoryTreeNodeByCreatTimeComparator();
	    Collections.sort(this.getChildren(), comparator);
	    for (TreeNode child : this.getChildren()) {
	      ((CategoryDefaultTreeNode) child).sortByCreateTime();
	    }
}
}
