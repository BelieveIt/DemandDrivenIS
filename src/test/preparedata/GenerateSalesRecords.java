package test.preparedata;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import dao.SalesRecordDao;

import model.SalesRecord;
import utils.RandomUtil;


public class GenerateSalesRecords {
	public static void main(String[] strs) throws ParseException{
		SalesRecordDao salesRecordDao = new SalesRecordDao();
		salesRecordDao.deleteAll();
		String productId1 = "OdkRj84jt1";
		String productId2 = "OdkRj84jt2";
		String productId3 = "OdkRj84jt3";
		String productId4 = "OdkRj84jt4";
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate;
		Date endDate;
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		//2011
		startDate = dateFormat.parse("2011-01-01");
		endDate = dateFormat.parse("2011-12-30");
		start.setTime(startDate);
		end.setTime(endDate);
		for (Date date = start.getTime(); !start.after(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
			if(start.get(Calendar.DAY_OF_WEEK) == 7 || start.get(Calendar.DAY_OF_WEEK) == 1){
				//weekend
				salesRecordDao.insertProduct(new SalesRecord("1", productId1, RandomUtil.getRandom(25, 30), start.getTime()));
				salesRecordDao.insertProduct(new SalesRecord("1", productId2, RandomUtil.getRandom(10, 15), start.getTime()));
				salesRecordDao.insertProduct(new SalesRecord("1", productId3, RandomUtil.getRandom(45, 50), start.getTime()));
				salesRecordDao.insertProduct(new SalesRecord("1", productId4, RandomUtil.getRandom(35, 40), start.getTime()));
			}else {
				//work day
				salesRecordDao.insertProduct(new SalesRecord("1", productId1, RandomUtil.getRandom(35, 40), start.getTime()));
				salesRecordDao.insertProduct(new SalesRecord("1", productId2, RandomUtil.getRandom(20, 25), start.getTime()));
				salesRecordDao.insertProduct(new SalesRecord("1", productId3, RandomUtil.getRandom(55, 60), start.getTime()));
				salesRecordDao.insertProduct(new SalesRecord("1", productId4, RandomUtil.getRandom(45, 50), start.getTime()));
			}
	        try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//2012
		startDate = dateFormat.parse("2012-01-01");
		endDate = dateFormat.parse("2012-12-30");
		start.setTime(startDate);
		end.setTime(endDate);
		for (Date date = start.getTime(); !start.after(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
			if(start.get(Calendar.DAY_OF_WEEK) == 7 || start.get(Calendar.DAY_OF_WEEK) == 1){
				//weekend
				salesRecordDao.insertProduct(new SalesRecord("1", productId1, RandomUtil.getRandom(30, 35), start.getTime()));
				salesRecordDao.insertProduct(new SalesRecord("1", productId2, RandomUtil.getRandom(15, 20), start.getTime()));
				salesRecordDao.insertProduct(new SalesRecord("1", productId3, RandomUtil.getRandom(50, 55), start.getTime()));
				salesRecordDao.insertProduct(new SalesRecord("1", productId4, RandomUtil.getRandom(40, 45), start.getTime()));
			}else {
				//work day
				salesRecordDao.insertProduct(new SalesRecord("1", productId1, RandomUtil.getRandom(40, 45), start.getTime()));
				salesRecordDao.insertProduct(new SalesRecord("1", productId2, RandomUtil.getRandom(25, 30), start.getTime()));
				salesRecordDao.insertProduct(new SalesRecord("1", productId3, RandomUtil.getRandom(60, 65), start.getTime()));
				salesRecordDao.insertProduct(new SalesRecord("1", productId4, RandomUtil.getRandom(50, 55), start.getTime()));
			}
	        try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//2013
		startDate = dateFormat.parse("2013-01-01");
		endDate = dateFormat.parse("2013-12-30");
		start.setTime(startDate);
		end.setTime(endDate);
		for (Date date = start.getTime(); !start.after(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
			if(start.get(Calendar.DAY_OF_WEEK) == 7 || start.get(Calendar.DAY_OF_WEEK) == 1){
				//weekend
				salesRecordDao.insertProduct(new SalesRecord("1", productId1, RandomUtil.getRandom(50, 55), start.getTime()));
				salesRecordDao.insertProduct(new SalesRecord("1", productId2, RandomUtil.getRandom(35, 40), start.getTime()));
				salesRecordDao.insertProduct(new SalesRecord("1", productId3, RandomUtil.getRandom(70, 75), start.getTime()));
				salesRecordDao.insertProduct(new SalesRecord("1", productId4, RandomUtil.getRandom(60, 65), start.getTime()));
			}else {
				//work day
				salesRecordDao.insertProduct(new SalesRecord("1", productId1, RandomUtil.getRandom(60, 65), start.getTime()));
				salesRecordDao.insertProduct(new SalesRecord("1", productId2, RandomUtil.getRandom(45, 50), start.getTime()));
				salesRecordDao.insertProduct(new SalesRecord("1", productId3, RandomUtil.getRandom(80, 85), start.getTime()));
				salesRecordDao.insertProduct(new SalesRecord("1", productId4, RandomUtil.getRandom(70, 75), start.getTime()));
			}
	        try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//2014
		startDate = dateFormat.parse("2014-01-01");
		endDate = dateFormat.parse("2014-10-09");
		start.setTime(startDate);
		end.setTime(endDate);
		for (Date date = start.getTime(); !start.after(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
			if(start.get(Calendar.DAY_OF_WEEK) == 7 || start.get(Calendar.DAY_OF_WEEK) == 1){
				//weekend
				salesRecordDao.insertProduct(new SalesRecord("1", productId1, RandomUtil.getRandom(55, 60), start.getTime()));
				salesRecordDao.insertProduct(new SalesRecord("1", productId2, RandomUtil.getRandom(40, 45), start.getTime()));
				salesRecordDao.insertProduct(new SalesRecord("1", productId3, RandomUtil.getRandom(75, 80), start.getTime()));
				salesRecordDao.insertProduct(new SalesRecord("1", productId4, RandomUtil.getRandom(65, 70), start.getTime()));
			}else {
				//work day
				salesRecordDao.insertProduct(new SalesRecord("1", productId1, RandomUtil.getRandom(65, 70), start.getTime()));
				salesRecordDao.insertProduct(new SalesRecord("1", productId2, RandomUtil.getRandom(50, 55), start.getTime()));
				salesRecordDao.insertProduct(new SalesRecord("1", productId3, RandomUtil.getRandom(85, 90), start.getTime()));
				salesRecordDao.insertProduct(new SalesRecord("1", productId4, RandomUtil.getRandom(75, 80), start.getTime()));
			}
	        try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Process Finished!");
	}
}
