package merchandise.utils;

import java.util.ArrayList;
import java.util.List;

import org.primefaces.model.TreeNode;


import model.BasicListItem;
import model.Category;

public class ProductUtil {
	public static final String EVERYDAY = "everyday";
	public static final String EVERYWEEK = "everyweek";
	public static final String EVERYMONTH = "everymonth";	
	public static List<BasicListItem> generateBasicListItemsBySelectedNode(List<BasicListItem> items, TreeNode selectedNode){
		List<BasicListItem> list = new ArrayList<BasicListItem>();
		List<String> categoryIdList = new ArrayList<String>();
		List<Category> categories = CategoryUtil.getCategoriesFormTree(selectedNode);
		for(Category category : categories){
			categoryIdList.add(category.getCategoryId());
		}
		for(BasicListItem item : items){
			if(categoryIdList.contains(item.getCategoryId())){
				list.add(item);
			}
		}
		return list;
	}
	
	public static List<BasicListItem> generateBasicListItemsByCategory(List<BasicListItem> items, Category category){
		List<BasicListItem> list = new ArrayList<BasicListItem>();
		for(BasicListItem item : items){
			if(item.getCategoryId().equals(category.getCategoryId())){
				list.add(item);
			}
		}
		return list;
	}
}
