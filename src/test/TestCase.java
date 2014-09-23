package test;

import merchandise.utils.CategoryUtil;

public class TestCase {
public static void main(String[] args){
//	BasicListCategoryDao basicListCategoryDao = new BasicListCategoryDao();
//	List<BasicListCategory> basicListCategories = basicListCategoryDao.queryCategoriesByVersionId("head");
//
//	TreeNode rootOfFranchiser1 = CategoryUtil.generateTree(basicListCategories, CategoryUtil.ORDER_BY_NAME, CategoryUtil.ROOT_FATHER_ID);
//	BasicListCategory b1 = (BasicListCategory) rootOfFranchiser1.getChildren().get(0).getData();
//
//	TreeNode rootOfFranchiser2 = CategoryUtil.generateTree(basicListCategories, CategoryUtil.ORDER_BY_TIME, CategoryUtil.ROOT_FATHER_ID);
//	BasicListCategory b2 = (BasicListCategory) rootOfFranchiser2.getChildren().get(0).getData();
//	System.out.println(b1.getCategoryName());
//	System.out.println(b2.getCategoryName());


	CategoryUtil.compareLocalTreeAndBasicList("1", "head");
}
}
