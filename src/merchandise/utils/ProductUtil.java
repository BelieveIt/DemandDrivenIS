package merchandise.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.primefaces.model.TreeNode;

import com.sun.org.apache.bcel.internal.generic.NEW;


import dao.BasicListItemDao;
import dao.RegionListItemDao;


import model.BasicListItem;
import model.Category;
import model.Product;
import model.RegionListItem;

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

	public static List<RegionListItem> generateRegionListItemsBySelectedNode(List<RegionListItem> items, TreeNode selectedNode){
		List<RegionListItem> list = new ArrayList<RegionListItem>();
		List<String> categoryIdList = new ArrayList<String>();
		List<Category> categories = CategoryUtil.getCategoriesFormTree(selectedNode);
		for(Category category : categories){
			categoryIdList.add(category.getCategoryId());
		}
		for(RegionListItem item : items){
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

	public static BasicListItemDiff generateBasicListDiff(String oldVersionId, String newVersionId){
		BasicListItemDiff basicListDiff = new BasicListItemDiff();

		BasicListItemDao basicListItemDao = new BasicListItemDao();
		List<BasicListItem> oldBasicListItems = basicListItemDao.queryProductsByVersionId(oldVersionId);
		HashMap<String, BasicListItem> oldMap = convertToMapForBasicListItem(oldBasicListItems);
		List<BasicListItem> newBasicListItems = basicListItemDao.queryProductsByVersionId(newVersionId);
		HashMap<String, BasicListItem> newMap = convertToMapForBasicListItem(newBasicListItems);
		for(BasicListItem basicListItem : oldBasicListItems){
			if(!newMap.containsKey(basicListItem.getProductId())) basicListDiff.getDeletedItems().add(basicListItem);
		}
		for(BasicListItem basicListItem : newBasicListItems){
			String currentId = basicListItem.getProductId();
			if(!oldMap.containsKey(currentId))basicListDiff.getAddedItems().add(basicListItem);
			if(oldMap.containsKey(currentId) && !checkProductDiff(basicListItem.getProduct(), oldMap.get(currentId).getProduct()))basicListDiff.getEditedItems().add(basicListItem);
		}
		return basicListDiff;
	}

	public static RegionListItemDiff generateRegionListItemDiffForHead(String regionId){
		RegionListItemDiff regionListItemDiff = new RegionListItemDiff();
		RegionListItemDao regionListItemDao = new RegionListItemDao();

		List<RegionListItem> allHeadRegionListItems = regionListItemDao.queryProductsByVersionIdAndRegionId(regionId, "head");
		List<RegionListItem> headRegionListItems = new ArrayList<RegionListItem>();
		for(RegionListItem regionListItem : allHeadRegionListItems){
			if(regionListItem.getIsRegionAdd()==null || !regionListItem.getIsRegionAdd().equals(new Integer(1))){
				headRegionListItems.add(regionListItem);
			}
		}
		HashMap<String, RegionListItem> oldMap = convertToMapForRegionListItem(headRegionListItems);

		List<RegionListItem> newestRetrivedItems = regionListItemDao.queryProductsByVersionIdAndRegionId(regionId, VersionUtil.getRetrivedNewestVersionId(regionId));
		HashMap<String, RegionListItem> newMap = convertToMapForRegionListItem(newestRetrivedItems);

		for(RegionListItem regionListItem : headRegionListItems){
			if(!newMap.containsKey(regionListItem.getProductId())) regionListItemDiff.getDeletedItems().add(regionListItem);
		}
		for(RegionListItem regionListItem : newestRetrivedItems){
			String currentId = regionListItem.getProductId();
			if(!oldMap.containsKey(currentId))
				regionListItemDiff.getAddedItems().add(regionListItem);
			if(oldMap.containsKey(currentId) && !checkProductDiff(regionListItem.getProduct(), oldMap.get(currentId).getProduct()))
				regionListItemDiff.getEditedItems().add(regionListItem);
		}
		return regionListItemDiff;
	}

	public static boolean compareLocalItemAndBasicList(RegionListItemDiff regionListItemDiff){
		if(regionListItemDiff.getDeletedItems().size()!=0)return false;
		if(regionListItemDiff.getAddedItems().size()!=0)return false;
		if(regionListItemDiff.getEditedItems().size()!=0)return false;
		return true;
	}

	public static boolean checkProductDiff(Product productA, Product productB){
		if(!productA.getBrand().equals(productB.getBrand()))return false;
		if(!productA.getDeliveryFrequency().equals(productB.getDeliveryFrequency()))return false;
		if(!productA.getImage().equals(productB.getImage()))return false;
		if(!productA.getItemWeight().equals(productB.getItemWeight()))return false;
		if(!productA.getManufacturer().equals(productB.getManufacturer()))return false;
		if(!productA.getMinInventory().equals(productB.getMinInventory()))return false;
		if(!productA.getName().equals(productB.getName()))return false;
		if(!productA.getPrice().equals(productB.getPrice()))return false;
		if(!productA.getUnit().equals(productB.getUnit()))return false;
		return true;
	}
	public static HashMap<String, BasicListItem> convertToMapForBasicListItem(List<BasicListItem> items){
		HashMap<String, BasicListItem> map = new HashMap<String, BasicListItem>();
		for(BasicListItem basicListItem : items){
			map.put(basicListItem.getProductId(), basicListItem);
		}
		return map;
	}
	public static HashMap<String, RegionListItem> convertToMapForRegionListItem(List<RegionListItem> items){
		HashMap<String, RegionListItem> map = new HashMap<String, RegionListItem>();
		for(RegionListItem regionListItem : items){
			map.put(regionListItem.getProductId(), regionListItem);
		}
		return map;
	}
}
