package test.preparedata;


import java.text.ParseException;

import model.DeliverySchedule;
import model.Product;
import model.WasteRecord;

import dao.BasicListCategoryDao;
import dao.BasicListDao;
import dao.BasicListItemDao;
import dao.DeliveryReportDao;
import dao.DeliveryReportItemDao;
import dao.DeliveryScheduleDao;
import dao.ProductTypeDao;
import dao.RegionListCategoryDao;
import dao.RegionListItemDao;
import dao.RegionListUpdateInfoDao;
import dao.ReplenishmentReportDao;
import dao.ReplenishmentReportItemDao;
import dao.StockOutVirtualSalesDao;
import dao.StoreDao;
import dao.SysUserDao;
import dao.WasteRecordDao;

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

		StockOutVirtualSalesDao stockOutVirtualSalesDao = new StockOutVirtualSalesDao();
		stockOutVirtualSalesDao.deleteAll();

		WasteRecordDao wasteRecordDao = new WasteRecordDao();
		wasteRecordDao.deleteAll();


		//Report
		ReplenishmentReportDao replenishmentReportDao = new ReplenishmentReportDao();
		ReplenishmentReportItemDao replenishmentReportItemDao = new ReplenishmentReportItemDao();
		DeliveryReportDao deliveryReportDao = new DeliveryReportDao();
		DeliveryReportItemDao deliveryReportItemDao = new DeliveryReportItemDao();

		replenishmentReportDao.deleteAll();
		replenishmentReportItemDao.deleteAll();
		deliveryReportDao.deleteAll();
		deliveryReportItemDao.deleteAll();

		//Schedule
		DeliveryScheduleDao deliveryScheduleDao = new DeliveryScheduleDao();
		DeliverySchedule deliverySchedule1 = new DeliverySchedule();
		deliverySchedule1.setDeliveryMark(1);
		deliverySchedule1.setDeliveryHour(5);
		deliverySchedule1.setDeliveryType(Product.EVERYWEEK);

		DeliverySchedule deliverySchedule2 = new DeliverySchedule();
		deliverySchedule2.setDeliveryMark(1);
		deliverySchedule2.setDeliveryHour(5);
		deliverySchedule2.setDeliveryType(Product.EVERYMONTH);

		deliveryScheduleDao.deleteAll();
		deliveryScheduleDao.insertDeliverySchedule(deliverySchedule1);
		deliveryScheduleDao.insertDeliverySchedule(deliverySchedule2);
		System.out.println("Deletion Process Finished!");
	}
}
