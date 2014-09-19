package test.preparedata;

import java.util.Date;

import dao.BasicListCategoryDao;

import utils.IdentityUtil;
import model.BasicListCategory;

public class GenerateBasicList {
	public static void main(String[] str){
		generateBasicListCategories();
	}
	public static void generateBasicListCategories(){
		BasicListCategory category = new BasicListCategory();
		category.setCategoryId(IdentityUtil.randomUUID());
		category.setCategoryFatherId("0");
		category.setCategoryName("categories");
		category.setVersionId("head");
		category.setCreateTime(new Date());

		BasicListCategoryDao dao = new BasicListCategoryDao();
		dao.deleteAll();
		dao.insertCategory(category);
	}
}
