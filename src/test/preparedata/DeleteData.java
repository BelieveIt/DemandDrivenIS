package test.preparedata;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.mail.internet.NewsAddress;

import utils.RandomUtil;

import model.RegionListItem;
import model.SalesRecord;
import dao.BasicListCategoryDao;
import dao.BasicListDao;
import dao.BasicListItemDao;
import dao.ProductTypeDao;
import dao.RegionListCategoryDao;
import dao.RegionListItemDao;
import dao.RegionListUpdateInfoDao;
import dao.SalesRecordDao;
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
