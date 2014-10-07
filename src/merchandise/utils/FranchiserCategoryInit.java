package merchandise.utils;

import java.util.ArrayList;
import java.util.Date;

import dao.BasicListCategoryDao;

import utils.IdentityUtil;
import model.BasicListCategory;

public class FranchiserCategoryInit {
	public static void initCategory(){
		ArrayList<BasicListCategory> basicListCategories = new ArrayList<BasicListCategory>();
		BasicListCategory root = new BasicListCategory(IdentityUtil.randomUUID(), "0", "Categories", new Date(), "head");
		basicListCategories.add(root);
		
		BasicListCategory cate1 = new BasicListCategory(IdentityUtil.randomUUID(), root.getCategoryId(), "Fast Food", new Date(), "head");
		basicListCategories.add(cate1);
		BasicListCategory cate1_1 = new BasicListCategory(IdentityUtil.randomUUID(), cate1.getCategoryId(), "Baked Goods", new Date(), "head");
		basicListCategories.add(cate1_1);
		BasicListCategory cate1_2 = new BasicListCategory(IdentityUtil.randomUUID(), cate1.getCategoryId(), "Hamburg", new Date(), "head");
		basicListCategories.add(cate1_2);
		BasicListCategory cate1_3 = new BasicListCategory(IdentityUtil.randomUUID(), cate1.getCategoryId(), "Cookies", new Date(), "head");
		basicListCategories.add(cate1_3);
		BasicListCategory cate1_4 = new BasicListCategory(IdentityUtil.randomUUID(), cate1.getCategoryId(), "Only at Ours", new Date(), "head");
		basicListCategories.add(cate1_4);
		
		BasicListCategory cate2 = new BasicListCategory(IdentityUtil.randomUUID(), root.getCategoryId(), "Beverages", new Date(), "head");
		basicListCategories.add(cate2);
		BasicListCategory cate2_1 = new BasicListCategory(IdentityUtil.randomUUID(), cate2.getCategoryId(), "Hot Drinks", new Date(), "head");
		basicListCategories.add(cate2_1);
		BasicListCategory cate2_2 = new BasicListCategory(IdentityUtil.randomUUID(), cate2.getCategoryId(), "Cold Drinks", new Date(), "head");
		basicListCategories.add(cate2_2);
		BasicListCategory cate2_3 = new BasicListCategory(IdentityUtil.randomUUID(), cate2.getCategoryId(), "From the Fridge", new Date(), "head");
		basicListCategories.add(cate2_3);
		BasicListCategory cate2_4 = new BasicListCategory(IdentityUtil.randomUUID(), cate2.getCategoryId(), "Only at Ours", new Date(), "head");
		basicListCategories.add(cate2_4);
		
		BasicListCategory cate3 = new BasicListCategory(IdentityUtil.randomUUID(), root.getCategoryId(), "Candy", new Date(), "head");
		basicListCategories.add(cate3);
		BasicListCategory cate3_1 = new BasicListCategory(IdentityUtil.randomUUID(), cate3.getCategoryId(), "Hershey", new Date(), "head");
		basicListCategories.add(cate3_1);
		BasicListCategory cate3_2 = new BasicListCategory(IdentityUtil.randomUUID(), cate3.getCategoryId(), "Nestle", new Date(), "head");
		basicListCategories.add(cate3_2);
		BasicListCategory cate3_3 = new BasicListCategory(IdentityUtil.randomUUID(), cate3.getCategoryId(), "M&M/Mars", new Date(), "head");
		basicListCategories.add(cate3_3);
		BasicListCategory cate3_4 = new BasicListCategory(IdentityUtil.randomUUID(), cate3.getCategoryId(), "More", new Date(), "head");
		basicListCategories.add(cate3_4);
		
		BasicListCategory cate4 = new BasicListCategory(IdentityUtil.randomUUID(), root.getCategoryId(), "Snacks", new Date(), "head");
		basicListCategories.add(cate4);
		BasicListCategory cate4_1 = new BasicListCategory(IdentityUtil.randomUUID(), cate4.getCategoryId(), "Pastry", new Date(), "head");
		basicListCategories.add(cate4_1);
		BasicListCategory cate4_2 = new BasicListCategory(IdentityUtil.randomUUID(), cate4.getCategoryId(), "Potato Chips", new Date(), "head");
		basicListCategories.add(cate4_2);
		BasicListCategory cate4_3 = new BasicListCategory(IdentityUtil.randomUUID(), cate4.getCategoryId(), "Seeds", new Date(), "head");
		basicListCategories.add(cate4_3);
		BasicListCategory cate4_4 = new BasicListCategory(IdentityUtil.randomUUID(), cate4.getCategoryId(), "Cookies", new Date(), "head");
		basicListCategories.add(cate4_4);
		BasicListCategory cate4_5 = new BasicListCategory(IdentityUtil.randomUUID(), cate4.getCategoryId(), "Meat", new Date(), "head");
		basicListCategories.add(cate4_5);
		
		BasicListCategory cate5 = new BasicListCategory(IdentityUtil.randomUUID(), root.getCategoryId(), "Tobacco", new Date(), "head");
		basicListCategories.add(cate5);
		BasicListCategory cate5_1 = new BasicListCategory(IdentityUtil.randomUUID(), cate5.getCategoryId(), "Snuff", new Date(), "head");
		basicListCategories.add(cate5_1);
		BasicListCategory cate5_2 = new BasicListCategory(IdentityUtil.randomUUID(), cate5.getCategoryId(), "Cigars", new Date(), "head");
		basicListCategories.add(cate5_2);
		BasicListCategory cate5_3 = new BasicListCategory(IdentityUtil.randomUUID(), cate5.getCategoryId(), "Chewing Tobacco", new Date(), "head");
		basicListCategories.add(cate5_3);
		
		BasicListCategory cate6 = new BasicListCategory(IdentityUtil.randomUUID(), root.getCategoryId(), "Cigarettes", new Date(), "head");
		basicListCategories.add(cate6);
		BasicListCategory cate6_1 = new BasicListCategory(IdentityUtil.randomUUID(), cate6.getCategoryId(), "Premium Priced", new Date(), "head");
		basicListCategories.add(cate6_1);
		BasicListCategory cate6_2 = new BasicListCategory(IdentityUtil.randomUUID(), cate6.getCategoryId(), "Generic Priced", new Date(), "head");
		basicListCategories.add(cate6_2);
		BasicListCategory cate6_3 = new BasicListCategory(IdentityUtil.randomUUID(), cate6.getCategoryId(), "Value Priced", new Date(), "head");
		basicListCategories.add(cate6_3);
		
		BasicListCategory cate7 = new BasicListCategory(IdentityUtil.randomUUID(), root.getCategoryId(), "General Merchandise", new Date(), "head");
		basicListCategories.add(cate7);
		BasicListCategory cate7_1 = new BasicListCategory(IdentityUtil.randomUUID(), cate7.getCategoryId(), "Batteries", new Date(), "head");
		basicListCategories.add(cate7_1);
		BasicListCategory cate7_2 = new BasicListCategory(IdentityUtil.randomUUID(), cate7.getCategoryId(), "Film and Disposable Cameras", new Date(), "head");
		basicListCategories.add(cate7_2);
		BasicListCategory cate7_3 = new BasicListCategory(IdentityUtil.randomUUID(), cate7.getCategoryId(), "School Supplies", new Date(), "head");
		basicListCategories.add(cate7_3);
		BasicListCategory cate7_4 = new BasicListCategory(IdentityUtil.randomUUID(), cate7.getCategoryId(), "Playing Cards", new Date(), "head");
		basicListCategories.add(cate7_4);
		BasicListCategory cate7_5 = new BasicListCategory(IdentityUtil.randomUUID(), cate7.getCategoryId(), "Camping Supplies", new Date(), "head");
		basicListCategories.add(cate7_5);
		BasicListCategory cate7_6 = new BasicListCategory(IdentityUtil.randomUUID(), cate7.getCategoryId(), "Firewood by the Bundle", new Date(), "head");
		basicListCategories.add(cate7_6);
		
		BasicListCategoryDao basicListCategoryDao = new BasicListCategoryDao();
		basicListCategoryDao.deleteAll();
		basicListCategoryDao.insertCategories(basicListCategories);
	}
}
