package merchandise.utils;

import java.util.Date;
import java.util.List;

import utils.IdentityUtil;

import dao.BasicListCategoryDao;
import dao.BasicListDao;
import dao.BasicListItemDao;
import model.BasicList;
import model.BasicListCategory;
import model.BasicListItem;

public class VersionUtil {
public static void createNewBasicList(){
	BasicListItemDao basicListItemDao = new BasicListItemDao();
	BasicListCategoryDao basicListCategoryDao  = new BasicListCategoryDao();
	BasicListDao basicListDao = new BasicListDao();
	
	List<BasicListItem> basicListItems = basicListItemDao.queryProductsByVersionId("head");
	List<BasicListCategory> basicListCategories = basicListCategoryDao.queryCategoriesByVersionId("head");
	
	String newVersionId = IdentityUtil.randomVersionId();
	for(BasicListItem basicListItem : basicListItems){
		basicListItem.setVersionId(newVersionId);
		basicListItemDao.insertProduct(basicListItem);
	}
	for(BasicListCategory basicListCategory : basicListCategories){
		basicListCategory.setVersionId(newVersionId);
		basicListCategoryDao.insertCategory(basicListCategory);
	}
	
	BasicList basicList = new BasicList();
	basicList.setCreateTime(new Date());
	basicList.setVersionId(newVersionId);
	basicListDao.insertBasicList(basicList);
}
}
