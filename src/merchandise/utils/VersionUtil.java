package merchandise.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import utils.IdentityUtil;

import dao.BasicListCategoryDao;
import dao.BasicListDao;
import dao.BasicListItemDao;
import dao.RegionDao;
import dao.RegionListCategoryDao;
import dao.RegionListItemDao;
import dao.RegionListUpdateInfoDao;
import model.BasicList;
import model.BasicListCategory;
import model.BasicListItem;
import model.Category;
import model.Region;
import model.RegionListCategory;
import model.RegionListItem;
import model.RegionListUpdateInfo;

public class VersionUtil {
public final static Integer RETRIVED = 1;
public final static Integer RETRIVED_NOT = 0;
public final static Integer CATEGORIES_UPDATED = 1;
public final static Integer CATEGORIES_UPDATED_NOT = 0;
public final static Integer CATEGORIES_UPDATED_NOT_RETRIVED_NOT = -1;
public final static Integer PRODUCTS_UPDATED = 1;
public final static Integer PRODUCTS_UPDATED_NOT = 0;
public final static Integer PRODUCTS_UPDATED_NOT_RETRIVED_NOT = -1;
public final static Integer CONFIRM_SENT = 1;
public final static Integer CONFIRM_CAN_SEND = 0;
public final static Integer CONFIRM_CAN_NOT_SEND = -1;
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
//Region VersionList Generation - REGION
public static List<RegionVersionListItem> generateRegionVersionListItems(List<RegionListUpdateInfo> regionListUpdateInfos){
	List<RegionVersionListItem> regionVersionListItems = new ArrayList<RegionVersionListItem>();

	Map<String,BasicList> basiclistMap = getBasicListMap();
	Map<String,RegionListUpdateInfo> regionVersionMap = new LinkedHashMap<String, RegionListUpdateInfo>();
	for(RegionListUpdateInfo regionListUpdateInfo : regionListUpdateInfos){
		regionVersionMap.put(regionListUpdateInfo.getVersionId(), regionListUpdateInfo);
	}
	Iterator<String> iter = basiclistMap.keySet().iterator();
	while (iter.hasNext()) {
		String versionId = iter.next();
		RegionVersionListItem regionVersionListItem = new RegionVersionListItem();
		regionVersionListItem.setIsOutDated(1);
		regionVersionListItem.setCreateTime(basiclistMap.get(versionId).getCreateTime());
		regionVersionListItem.setVersionId(versionId);
		if (regionVersionMap.containsKey(versionId)) {
			//Region have the version locally
			regionVersionListItem.setRegionListUpdateInfo(regionVersionMap.get(versionId));
			if(regionVersionListItem.getRegionListUpdateInfo().getIsFinished().equals(new Integer(1))){
				//UpdateInfo finished
				regionVersionListItem.setIsRetrived(RETRIVED);
				regionVersionListItem.setIsCategoryUpdated(CATEGORIES_UPDATED);
				regionVersionListItem.setIsProductUpdated(PRODUCTS_UPDATED);
			}else{
				//UpdateInfo not finished
				regionVersionListItem.setIsRetrived(RETRIVED);
				Integer categoryUpdatedStatus = setCategoryUpdatedStatus(regionVersionListItem);
				Integer productUpdatedStatus = setProductUpdatedStatus(regionVersionListItem);
				if(categoryUpdatedStatus.equals(new Integer(1)) && productUpdatedStatus.equals(new Integer(1))){
					regionVersionListItem.getRegionListUpdateInfo().setIsFinished(CONFIRM_CAN_SEND);
				}else {
					regionVersionListItem.getRegionListUpdateInfo().setIsFinished(CONFIRM_CAN_NOT_SEND);
				}
			}

		}else {
			//Region don't have the version locally
			regionVersionListItem.setIsRetrived(RETRIVED_NOT);
			regionVersionListItem.setIsCategoryUpdated(CATEGORIES_UPDATED_NOT_RETRIVED_NOT);
			regionVersionListItem.setIsProductUpdated(PRODUCTS_UPDATED_NOT_RETRIVED_NOT);
			RegionListUpdateInfo regionListUpdateInfo = new RegionListUpdateInfo();
			regionListUpdateInfo.setIsFinished(CONFIRM_CAN_NOT_SEND);
			regionVersionListItem.setRegionListUpdateInfo(regionListUpdateInfo);
		}
		regionVersionListItems.add(regionVersionListItem);
	}
	if(regionVersionListItems.size() != 0){
		regionVersionListItems.get(0).setIsOutDated(0);
	}
	return regionVersionListItems;
}

//Retrieve version from the franchiser to local - REGION
public static void retriveFromFranchiser(String regionId){
	BasicListDao basicListDao = new BasicListDao();
	BasicListItemDao basicListItemDao = new BasicListItemDao();
	BasicListCategoryDao basicListCategoryDao  = new BasicListCategoryDao();

	List<BasicList> basicLists = basicListDao.queryBasicLists();
	BasicList newestBasicList = basicLists.get(0);

	List<BasicListItem> basicListItems = basicListItemDao.queryProductsByVersionId(newestBasicList.getVersionId());
	List<BasicListCategory> basicListCategories = basicListCategoryDao.queryCategoriesByVersionId(newestBasicList.getVersionId());


		RegionDao regionDao = new RegionDao();
		RegionListCategoryDao regionListCategoryDao = new RegionListCategoryDao();
		RegionListItemDao regionListItemDao = new RegionListItemDao();
		RegionListUpdateInfoDao regionListUpdateInfoDao = new RegionListUpdateInfoDao();
		Region region = regionDao.queryRegion(regionId);

		//insert new version
		for(BasicListCategory basicListCategory : basicListCategories){
			RegionListCategory regionListCategory = new RegionListCategory();
			regionListCategory.setRegionId(region.getRegionId());
			setBasicListCategoryToRegion(regionListCategory, basicListCategory);
			regionListCategoryDao.insertCategory(regionListCategory);
		}

		for(BasicListItem basicListItem : basicListItems){
			RegionListItem regionListItem = new RegionListItem();
			regionListItem.setRegionId(region.getRegionId());
			setBasicListItemToRegion(regionListItem, basicListItem);
			regionListItemDao.insertProduct(regionListItem);
		}

		RegionListUpdateInfo regionListUpdateInfo = new RegionListUpdateInfo();
		regionListUpdateInfo.setCreateTime(newestBasicList.getCreateTime());
		regionListUpdateInfo.setFinishedTime(null);
		regionListUpdateInfo.setIsFinished(CONFIRM_CAN_SEND);
		regionListUpdateInfo.setRegionId(region.getRegionId());
		regionListUpdateInfo.setVersionId(newestBasicList.getVersionId());
		regionListUpdateInfoDao.insertRegionListUpdateInfo(regionListUpdateInfo);
}
//Update Region Category - REGION
public static void updateToNewestBasiclistCategory(String regionId){
	//init
	RegionDao regionDao = new RegionDao();
	Region region = regionDao.queryRegion(regionId);

	RegionListCategoryDao regionListCategoryDao = new RegionListCategoryDao();

	RegionListCategoryDao categoryDao = new RegionListCategoryDao();
	List<RegionListCategory> categories = categoryDao.queryCategoriesByVersionId("head", regionId);

	BasicListCategoryDao basicListCategoryDao = new BasicListCategoryDao();
	List<BasicListCategory> basicListCategories = basicListCategoryDao.queryCategoriesByVersionId(getRetrivedNewestVersionId(regionId));

	regionListCategoryDao.deleteAllHeadByRegionId(regionId,"head");
	for(BasicListCategory basicListCategory : basicListCategories){
		RegionListCategory categoryInRegionListHead = getIncludedInRegionCategoryHead(basicListCategory, categories);
		if(categoryInRegionListHead!=null){
			regionListCategoryDao.insertCategory(categoryInRegionListHead);
		}else {
			RegionListCategory regionListCategory = new RegionListCategory();
			regionListCategory.setRegionId(region.getRegionId());
			setBasicListCategoryToRegion(regionListCategory, basicListCategory);
			regionListCategory.setVersionId("head");
			regionListCategoryDao.insertCategory(regionListCategory);
		}
	}
//		for(BasicListItem basicListItem : basicListItems){
//			RegionListItem regionListItem = new RegionListItem();
//			regionListItem.setRegionId(region.getRegionId());
//			setBasicListItemToRegion(regionListItem, basicListItem);
//			regionListItem.setVersionId("head");
//			regionListItemDao.insertProduct(regionListItem);
//		}
}
public static RegionListCategory getIncludedInRegionCategoryHead(Category category, List<RegionListCategory> list){
	for(RegionListCategory regionListCategory : list){
		if(category.getCategoryId().equals(regionListCategory.getCategoryId()))return regionListCategory;
	}
	return null;
}

public static Integer setCategoryUpdatedStatus(RegionVersionListItem regionVersionListItem){
	String regionId = regionVersionListItem.getRegionListUpdateInfo().getRegionId();
	String basicVersionId = regionVersionListItem.getVersionId();
	boolean result = CategoryUtil.compareLocalTreeAndBasicList(regionId, basicVersionId);

	if(result){
		regionVersionListItem.setIsCategoryUpdated(CATEGORIES_UPDATED);
		return 1;
	}else {
		regionVersionListItem.setIsCategoryUpdated(CATEGORIES_UPDATED_NOT);
		return 0;
	}
}
public static Integer setProductUpdatedStatus(RegionVersionListItem regionVersionListItem){
	String regionId = regionVersionListItem.getRegionListUpdateInfo().getRegionId();
	RegionListItemDiff regionListItemDiff = ProductUtil.generateRegionListItemDiffForHead(regionId);
	boolean result = ProductUtil.compareLocalItemAndBasicList(regionListItemDiff);
	if(result){
		regionVersionListItem.setIsProductUpdated(PRODUCTS_UPDATED);
		return 1;
	}else {
		regionVersionListItem.setIsProductUpdated(PRODUCTS_UPDATED_NOT);
		return 0;
	}

}
//Convert Basic List Category to Region List Category
public static void setBasicListCategoryToRegion(RegionListCategory regionListCategory, BasicListCategory basicListCategory){
	regionListCategory.setCategoryId(basicListCategory.getCategoryId());
	regionListCategory.setCategoryFatherId(basicListCategory.getCategoryFatherId());
	regionListCategory.setCategoryName(basicListCategory.getCategoryName());
	regionListCategory.setVersionId(basicListCategory.getVersionId());
	regionListCategory.setCreateTime(basicListCategory.getCreateTime());
}
//Convert Basic List Item to Region List Item
public static void setBasicListItemToRegion(RegionListItem regionListItem, BasicListItem basicListItem){
	regionListItem.setCategoryId(basicListItem.getCategoryId());
	regionListItem.setIsConfirmed(0);
	regionListItem.setIsRegionAdd(0);
	regionListItem.setProduct(basicListItem.getProduct());
	regionListItem.setProductId(basicListItem.getProductId());
	regionListItem.setVersionId(basicListItem.getVersionId());
}


//Region retrieved newest versionId
public static String getRetrivedNewestVersionId(String regionId){
	RegionListUpdateInfoDao regionListUpdateInfoDao = new RegionListUpdateInfoDao();
	List<RegionListUpdateInfo> newestRegionListUpdateInfos = regionListUpdateInfoDao.queryRegionListUpdateInfosByRegionId(regionId);
	String localNewestVersion = newestRegionListUpdateInfos.get(0).getVersionId();
	return localNewestVersion;
}

public static Map<String,RegionListUpdateInfo> getRegionListUpdateInfoMapByRegionId(String regionId){
	RegionListUpdateInfoDao regionListUpdateInfoDao = new RegionListUpdateInfoDao();
	List<RegionListUpdateInfo> regionListUpdateInfos = regionListUpdateInfoDao.queryRegionListUpdateInfos();
	LinkedHashMap<String, RegionListUpdateInfo> map = new LinkedHashMap<String, RegionListUpdateInfo>();
	for(RegionListUpdateInfo regionListUpdateInfo : regionListUpdateInfos){
		if(regionListUpdateInfo.getRegionId().equals(regionId))
			map.put(regionListUpdateInfo.getVersionId(), regionListUpdateInfo);
	}
	return map;
}

public static Map<String,BasicList> getBasicListMap(){
	BasicListDao basicListDao = new BasicListDao();
	List<BasicList> basicLists = basicListDao.queryBasicLists();
	LinkedHashMap<String, BasicList> basicListMap = new LinkedHashMap<String, BasicList>();
	for(BasicList basicList : basicLists){
		basicListMap.put(basicList.getVersionId(), basicList);
	}
	return basicListMap;
}
}
