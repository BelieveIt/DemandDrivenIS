package test.preparedata;


import java.text.ParseException;

import dao.BasicListCategoryDao;
import dao.BasicListDao;
import dao.BasicListItemDao;
import dao.ProductTypeDao;
import dao.RegionListCategoryDao;
import dao.RegionListItemDao;
import dao.RegionListUpdateInfoDao;
import dao.StoreDao;

public class DeleteData {
	public static void main(String[] Str) throws ParseException{
		//Basic List
		BasicListDao basicListDao = new BasicListDao();
		basicListDao.deleteAll();

		BasicListCategoryDao basicListCategoryDao = new BasicListCategoryDao();
		basicListCategoryDao.deleteAll();

		BasicListItemDao basicListItemDao = new BasicListItemDao();
		basicListItemDao.deleteAll();

		ProductTypeDao productTypeDao = new ProductTypeDao();
		productTypeDao.deleteAll();

		//Region List
		RegionListCategoryDao regionListCategoryDao = new RegionListCategoryDao();
		regionListCategoryDao.deleteAll();

		RegionListItemDao regionListItemDao = new RegionListItemDao();
		regionListItemDao.deleteAll();

		RegionListUpdateInfoDao regionListUpdateInfoDao = new RegionListUpdateInfoDao();
		regionListUpdateInfoDao.deleteAll();

		//Store List
		StoreDao storeDao = new StoreDao();
		storeDao.deleteAllStoreSellingItemForStore();

		System.out.println("Deletion Process Finished!");


	}
}
