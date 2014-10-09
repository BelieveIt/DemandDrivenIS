package merchandise.utils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import dao.BasicListCategoryDao;
import dao.BasicListItemDao;
import dao.ProductTypeDao;
import dao.StoreDao;

import utils.IdentityUtil;
import utils.StringUtil;
import model.BasicListCategory;
import model.BasicListItem;
import model.Product;
import model.ProductType;

public class FranchiserCategoryInit {
	public static void initCategory() throws ParseException{
		//Init Product Types
		ProductType food = new ProductType();
		food.setProductTypeId(IdentityUtil.randomUUID());
		food.setProductTypeName("Food");
		food.setAdditionalInformationLable(StringUtil.getStringsBySplit("##Packaging Information##Storage Instructions##Edible Methods"));

		ProductType book = new ProductType();
		book.setProductTypeId(IdentityUtil.randomUUID());
		book.setProductTypeName("Book");
		book.setAdditionalInformationLable(StringUtil.getStringsBySplit("##Book Size##Language##Publisher"));

		ProductType dailyUse = new ProductType();
		dailyUse.setProductTypeId(IdentityUtil.randomUUID());
		dailyUse.setProductTypeName("DailyUse");
		dailyUse.setAdditionalInformationLable(StringUtil.getStringsBySplit("##Color##Packaging Information"));

		ProductType tobbacco = new ProductType();
		tobbacco.setProductTypeId(IdentityUtil.randomUUID());
		tobbacco.setProductTypeName("Tobbacco&Cigarette");
		tobbacco.setAdditionalInformationLable(StringUtil.getStringsBySplit("##Storage Instructions"));

		ProductTypeDao productTypeDao = new ProductTypeDao();
		productTypeDao.insertProductType(food);
		productTypeDao.insertProductType(book);
		productTypeDao.insertProductType(dailyUse);
		productTypeDao.insertProductType(tobbacco);

		//Init Categories
		ArrayList<BasicListCategory> basicListCategories = new ArrayList<BasicListCategory>();
		BasicListCategory root = new BasicListCategory(IdentityUtil.randomUUID(), "0", "Categories", new Date(), "head");
		basicListCategories.add(root);

		BasicListCategory cate1 = new BasicListCategory(IdentityUtil.randomUUID(), root.getCategoryId(), "Fast Food", new Date(), "head");
		cate1.setProductTypeId(food.getProductTypeId());
		basicListCategories.add(cate1);
		BasicListCategory cate1_1 = new BasicListCategory(IdentityUtil.randomUUID(), cate1.getCategoryId(), "Baked Goods", new Date(), "head");
		cate1_1.setProductTypeId(food.getProductTypeId());
		basicListCategories.add(cate1_1);
		BasicListCategory cate1_2 = new BasicListCategory(IdentityUtil.randomUUID(), cate1.getCategoryId(), "Hamburg", new Date(), "head");
		cate1_2.setProductTypeId(food.getProductTypeId());
		basicListCategories.add(cate1_2);
		BasicListCategory cate1_3 = new BasicListCategory(IdentityUtil.randomUUID(), cate1.getCategoryId(), "Cookies", new Date(), "head");
		cate1_3.setProductTypeId(food.getProductTypeId());
		basicListCategories.add(cate1_3);
		BasicListCategory cate1_4 = new BasicListCategory(IdentityUtil.randomUUID(), cate1.getCategoryId(), "Only at Ours", new Date(), "head");
		cate1_4.setProductTypeId(food.getProductTypeId());
		basicListCategories.add(cate1_4);

		BasicListCategory cate2 = new BasicListCategory(IdentityUtil.randomUUID(), root.getCategoryId(), "Beverages", new Date(), "head");
		cate2.setProductTypeId(food.getProductTypeId());
		basicListCategories.add(cate2);
		BasicListCategory cate2_1 = new BasicListCategory(IdentityUtil.randomUUID(), cate2.getCategoryId(), "Hot Drinks", new Date(), "head");
		cate2_1.setProductTypeId(food.getProductTypeId());
		basicListCategories.add(cate2_1);
		BasicListCategory cate2_2 = new BasicListCategory(IdentityUtil.randomUUID(), cate2.getCategoryId(), "Cold Drinks", new Date(), "head");
		cate2_2.setProductTypeId(food.getProductTypeId());
		basicListCategories.add(cate2_2);
		BasicListCategory cate2_3 = new BasicListCategory(IdentityUtil.randomUUID(), cate2.getCategoryId(), "From the Fridge", new Date(), "head");
		cate2_3.setProductTypeId(food.getProductTypeId());
		basicListCategories.add(cate2_3);
		BasicListCategory cate2_4 = new BasicListCategory(IdentityUtil.randomUUID(), cate2.getCategoryId(), "Only at Ours", new Date(), "head");
		cate2_4.setProductTypeId(food.getProductTypeId());
		basicListCategories.add(cate2_4);

		BasicListCategory cate3 = new BasicListCategory(IdentityUtil.randomUUID(), root.getCategoryId(), "Candy", new Date(), "head");
		cate3.setProductTypeId(food.getProductTypeId());
		basicListCategories.add(cate3);
		BasicListCategory cate3_1 = new BasicListCategory(IdentityUtil.randomUUID(), cate3.getCategoryId(), "Hershey", new Date(), "head");
		cate3_1.setProductTypeId(food.getProductTypeId());
		basicListCategories.add(cate3_1);
		BasicListCategory cate3_2 = new BasicListCategory(IdentityUtil.randomUUID(), cate3.getCategoryId(), "Nestle", new Date(), "head");
		cate3_2.setProductTypeId(food.getProductTypeId());
		basicListCategories.add(cate3_2);
		BasicListCategory cate3_3 = new BasicListCategory(IdentityUtil.randomUUID(), cate3.getCategoryId(), "M&M/Mars", new Date(), "head");
		cate3_3.setProductTypeId(food.getProductTypeId());
		basicListCategories.add(cate3_3);
		BasicListCategory cate3_4 = new BasicListCategory(IdentityUtil.randomUUID(), cate3.getCategoryId(), "More", new Date(), "head");
		cate3_4.setProductTypeId(food.getProductTypeId());
		basicListCategories.add(cate3_4);

		BasicListCategory cate4 = new BasicListCategory(IdentityUtil.randomUUID(), root.getCategoryId(), "Snacks", new Date(), "head");
		cate4.setProductTypeId(food.getProductTypeId());
		basicListCategories.add(cate4);
		BasicListCategory cate4_1 = new BasicListCategory(IdentityUtil.randomUUID(), cate4.getCategoryId(), "Pastry", new Date(), "head");
		cate4_1.setProductTypeId(food.getProductTypeId());
		basicListCategories.add(cate4_1);
		BasicListCategory cate4_2 = new BasicListCategory(IdentityUtil.randomUUID(), cate4.getCategoryId(), "Potato Chips", new Date(), "head");
		cate4_2.setProductTypeId(food.getProductTypeId());
		basicListCategories.add(cate4_2);
		BasicListCategory cate4_3 = new BasicListCategory(IdentityUtil.randomUUID(), cate4.getCategoryId(), "Seeds", new Date(), "head");
		cate4_3.setProductTypeId(food.getProductTypeId());
		basicListCategories.add(cate4_3);
		BasicListCategory cate4_4 = new BasicListCategory(IdentityUtil.randomUUID(), cate4.getCategoryId(), "Cookies", new Date(), "head");
		cate4_4.setProductTypeId(food.getProductTypeId());
		basicListCategories.add(cate4_4);
		BasicListCategory cate4_5 = new BasicListCategory(IdentityUtil.randomUUID(), cate4.getCategoryId(), "Meat", new Date(), "head");
		cate4_5.setProductTypeId(food.getProductTypeId());
		basicListCategories.add(cate4_5);

		BasicListCategory cate5 = new BasicListCategory(IdentityUtil.randomUUID(), root.getCategoryId(), "Tobacco", new Date(), "head");
		cate5.setProductTypeId(tobbacco.getProductTypeId());
		basicListCategories.add(cate5);
		BasicListCategory cate5_1 = new BasicListCategory(IdentityUtil.randomUUID(), cate5.getCategoryId(), "Snuff", new Date(), "head");
		cate5_1.setProductTypeId(tobbacco.getProductTypeId());
		basicListCategories.add(cate5_1);
		BasicListCategory cate5_2 = new BasicListCategory(IdentityUtil.randomUUID(), cate5.getCategoryId(), "Cigars", new Date(), "head");
		cate5_2.setProductTypeId(tobbacco.getProductTypeId());
		basicListCategories.add(cate5_2);
		BasicListCategory cate5_3 = new BasicListCategory(IdentityUtil.randomUUID(), cate5.getCategoryId(), "Chewing Tobacco", new Date(), "head");
		cate5_3.setProductTypeId(tobbacco.getProductTypeId());
		basicListCategories.add(cate5_3);

		BasicListCategory cate6 = new BasicListCategory(IdentityUtil.randomUUID(), root.getCategoryId(), "Cigarettes", new Date(), "head");
		cate6.setProductTypeId(tobbacco.getProductTypeId());
		basicListCategories.add(cate6);
		BasicListCategory cate6_1 = new BasicListCategory(IdentityUtil.randomUUID(), cate6.getCategoryId(), "Premium Priced", new Date(), "head");
		cate6_1.setProductTypeId(tobbacco.getProductTypeId());
		basicListCategories.add(cate6_1);
		BasicListCategory cate6_2 = new BasicListCategory(IdentityUtil.randomUUID(), cate6.getCategoryId(), "Generic Priced", new Date(), "head");
		cate6_2.setProductTypeId(tobbacco.getProductTypeId());
		basicListCategories.add(cate6_2);
		BasicListCategory cate6_3 = new BasicListCategory(IdentityUtil.randomUUID(), cate6.getCategoryId(), "Value Priced", new Date(), "head");
		cate6_3.setProductTypeId(tobbacco.getProductTypeId());
		basicListCategories.add(cate6_3);

		BasicListCategory cate7 = new BasicListCategory(IdentityUtil.randomUUID(), root.getCategoryId(), "General Merchandise", new Date(), "head");
		cate7.setProductTypeId(dailyUse.getProductTypeId());
		basicListCategories.add(cate7);
		BasicListCategory cate7_1 = new BasicListCategory(IdentityUtil.randomUUID(), cate7.getCategoryId(), "Batteries", new Date(), "head");
		cate7_1.setProductTypeId(dailyUse.getProductTypeId());
		basicListCategories.add(cate7_1);
		BasicListCategory cate7_2 = new BasicListCategory(IdentityUtil.randomUUID(), cate7.getCategoryId(), "Film and Disposable Cameras", new Date(), "head");
		cate7_2.setProductTypeId(dailyUse.getProductTypeId());
		basicListCategories.add(cate7_2);
		BasicListCategory cate7_3 = new BasicListCategory(IdentityUtil.randomUUID(), cate7.getCategoryId(), "School Supplies", new Date(), "head");
		cate7_3.setProductTypeId(dailyUse.getProductTypeId());
		basicListCategories.add(cate7_3);
		BasicListCategory cate7_4 = new BasicListCategory(IdentityUtil.randomUUID(), cate7.getCategoryId(), "Playing Cards", new Date(), "head");
		cate7_4.setProductTypeId(dailyUse.getProductTypeId());
		basicListCategories.add(cate7_4);
		BasicListCategory cate7_5 = new BasicListCategory(IdentityUtil.randomUUID(), cate7.getCategoryId(), "Camping Supplies", new Date(), "head");
		cate7_5.setProductTypeId(dailyUse.getProductTypeId());
		basicListCategories.add(cate7_5);
		BasicListCategory cate7_6 = new BasicListCategory(IdentityUtil.randomUUID(), cate7.getCategoryId(), "Firewood by the Bundle", new Date(), "head");
		cate7_6.setProductTypeId(dailyUse.getProductTypeId());
		basicListCategories.add(cate7_6);

		BasicListCategoryDao basicListCategoryDao = new BasicListCategoryDao();
		basicListCategoryDao.deleteAll();
		basicListCategoryDao.insertCategories(basicListCategories);

		//Init two example products
		////Cold Drinks
		BasicListItem icedCoffee = new BasicListItem();
		icedCoffee.setCategoryId(cate2_2.getCategoryId());
		icedCoffee.setVersionId("head");
		icedCoffee.setProductId("OdkRj84jt1");
		Product icedCoffeeProduct = new Product();

		ArrayList<String> attributesForCoffee = new ArrayList<String>();
		attributesForCoffee.add("Cup");
		attributesForCoffee.add("Cold Storage");
		attributesForCoffee.add("Drink immediately");
		icedCoffeeProduct.setAdditionalInformation(attributesForCoffee);

		icedCoffeeProduct.setBrand("Own Brand");
		icedCoffeeProduct.setDeliveryFrequency(ProductUtil.EVERYDAY);
		icedCoffeeProduct.setImage("icedCoffee.jpg");
		icedCoffeeProduct.setItemWeight(250);
		icedCoffeeProduct.setManufacturer("Own Factory");
		icedCoffeeProduct.setMinInventory("5");
		icedCoffeeProduct.setName("Iced Coffee");
		icedCoffeeProduct.setPrice(new BigDecimal("3.5"));

		Date createTime = new SimpleDateFormat("yyyy-MM-dd").parse("2010-01-01");
		icedCoffeeProduct.setProductCreateTime(createTime);

		icedCoffeeProduct.setUnit("g");
		icedCoffee.setProduct(icedCoffeeProduct);

		////From the Fridge
		BasicListItem cocaCola = new BasicListItem();
		cocaCola.setCategoryId(cate2_3.getCategoryId());
		cocaCola.setVersionId("head");
		cocaCola.setProductId("OdkRj84jt2");
		Product cocaColaProduct = new Product();

		ArrayList<String> attributesForCoca = new ArrayList<String>();
		attributesForCoca.add("Can");
		attributesForCoca.add("Cold Storage");
		attributesForCoca.add("Drink immediately");
		cocaColaProduct.setAdditionalInformation(attributesForCoca);

		cocaColaProduct.setBrand("Coca-Cola");
		cocaColaProduct.setDeliveryFrequency(ProductUtil.EVERYWEEK);
		cocaColaProduct.setImage("cocacola.jpg");
		cocaColaProduct.setItemWeight(250);
		cocaColaProduct.setManufacturer("Coca-Cola Company");
		cocaColaProduct.setMinInventory("10");
		cocaColaProduct.setName("Coca-Cola Can");
		cocaColaProduct.setPrice(new BigDecimal("1.5"));

		cocaColaProduct.setProductCreateTime(createTime);

		cocaColaProduct.setUnit("g");
		cocaCola.setProduct(cocaColaProduct);

		////Hot Drink
		BasicListItem hotCoffee = new BasicListItem();
		hotCoffee.setCategoryId(cate2_1.getCategoryId());
		hotCoffee.setVersionId("head");
		hotCoffee.setProductId("OdkRj84jt3");
		Product hotCoffeeProduct = new Product();

		ArrayList<String> attributesForHotCoffee = new ArrayList<String>();
		attributesForHotCoffee.add("Cup");
		attributesForHotCoffee.add("Normal temperature");
		attributesForHotCoffee.add("Drink immediately");
		hotCoffeeProduct.setAdditionalInformation(attributesForHotCoffee);

		hotCoffeeProduct.setBrand("Hot Coffee");
		hotCoffeeProduct.setDeliveryFrequency(ProductUtil.EVERYDAY);
		hotCoffeeProduct.setImage("hotCoffee.jpg");
		hotCoffeeProduct.setItemWeight(300);
		hotCoffeeProduct.setManufacturer("Nestle");
		hotCoffeeProduct.setMinInventory("10");
		hotCoffeeProduct.setName("Hot Coffee");
		hotCoffeeProduct.setPrice(new BigDecimal("3.5"));

		hotCoffeeProduct.setProductCreateTime(createTime);

		hotCoffeeProduct.setUnit("g");
		hotCoffee.setProduct(hotCoffeeProduct);

		////Only at Ours
		BasicListItem freshOrgangeJuice = new BasicListItem();
		freshOrgangeJuice.setCategoryId(cate2_4.getCategoryId());
		freshOrgangeJuice.setVersionId("head");
		freshOrgangeJuice.setProductId("OdkRj84jt4");
		Product freshOrgangeJuiceProduct = new Product();

		ArrayList<String> attributesForFreshOrgangeJuice = new ArrayList<String>();
		attributesForFreshOrgangeJuice.add("Cup");
		attributesForFreshOrgangeJuice.add("Normal temperature");
		attributesForFreshOrgangeJuice.add("Drink immediately");
		freshOrgangeJuiceProduct.setAdditionalInformation(attributesForFreshOrgangeJuice);

		freshOrgangeJuiceProduct.setBrand("Fresh Organge Juice");
		freshOrgangeJuiceProduct.setDeliveryFrequency(ProductUtil.EVERYDAY);
		freshOrgangeJuiceProduct.setImage("freshOrangeJuice.jpg");
		freshOrgangeJuiceProduct.setItemWeight(300);
		freshOrgangeJuiceProduct.setManufacturer("Our Factory");
		freshOrgangeJuiceProduct.setMinInventory("10");
		freshOrgangeJuiceProduct.setName("Fresh Organge Juice");
		freshOrgangeJuiceProduct.setPrice(new BigDecimal("5.5"));

		freshOrgangeJuiceProduct.setProductCreateTime(createTime);

		freshOrgangeJuiceProduct.setUnit("g");
		freshOrgangeJuice.setProduct(freshOrgangeJuiceProduct);

		BasicListItemDao basicListItemDao = new BasicListItemDao();
		basicListItemDao.insertProduct(cocaCola);
		basicListItemDao.insertProduct(icedCoffee);
		basicListItemDao.insertProduct(hotCoffee);
		basicListItemDao.insertProduct(freshOrgangeJuice);

		StoreDao storeDao = new StoreDao();
		Calendar stockOutCalendar = Calendar.getInstance();
		stockOutCalendar.setTime(new Date());
		stockOutCalendar.add(Calendar.DATE, -3);
		storeDao.insertStoreSellingItemForStore("1", cocaCola.getProductId(), 20, null);
		storeDao.insertStoreSellingItemForStore("1", icedCoffee.getProductId(), 0, stockOutCalendar.getTime());
		storeDao.insertStoreSellingItemForStore("1", hotCoffee.getProductId(), 15, null);
		storeDao.insertStoreSellingItemForStore("1", freshOrgangeJuice.getProductId(), 0, stockOutCalendar.getTime());

	}
}
