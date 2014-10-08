package inventorybean.utils;

import java.util.ArrayList;
import java.util.List;

import merchandise.utils.CategoryUtil;
import model.Category;
import model.RegionListCategory;
import model.Store;
import model.StoreSellingItem;

import org.primefaces.model.TreeNode;

import dao.RegionListCategoryDao;
import dao.StoreDao;

public class MonitorUtil {
	public static List<StoreSellingItem> generateStoreSellingItemsBySelectedNode(TreeNode selectedNode, Store store){
		List<StoreSellingItem> list = new ArrayList<StoreSellingItem>();
		List<String> categoryIdList = new ArrayList<String>();
		List<Category> categories = CategoryUtil.getCategoriesFormTree(selectedNode);

		for(Category category : categories){
			categoryIdList.add(category.getCategoryId());
		}

		StoreDao storeDao = new StoreDao();
		List<StoreSellingItem> items = storeDao.queryStoreSellingItemsByStoreId(store.getStoreId());
		for(StoreSellingItem item : items){
			if(item.getRegionListItem() != null && categoryIdList.contains(item.getRegionListItem().getCategoryId())){
				list.add(item);
			}
		}

		if(categoryIdList.contains(CategoryUtil.NOT_CLASSIFIED_FOR_PRODUCT_ID)){
			List<StoreSellingItem> notInHeadCategoryTree = new ArrayList<StoreSellingItem>();
			RegionListCategoryDao regionListCategoryDao = new RegionListCategoryDao();
			List<RegionListCategory> allRegionHeadCategories = regionListCategoryDao.queryCategoriesByVersionId("head", store.getRegionId());
			List<String> allIdList = new ArrayList<String>();

			for(RegionListCategory regionListCategory : allRegionHeadCategories){
				allIdList.add(regionListCategory.getCategoryId());
			}

			for(StoreSellingItem item : items){
				if(item.getRegionListItem() != null && !allIdList.contains(item.getRegionListItem().getCategoryId()) && !item.getRegionListItem().getCategoryId().equals(CategoryUtil.NOT_CLASSIFIED_FOR_PRODUCT_ID)){
					notInHeadCategoryTree.add(item);
				}
			}

			for(StoreSellingItem regionListItem : notInHeadCategoryTree){
				list.add(regionListItem);
			}
		}

		return list;
	}
}
