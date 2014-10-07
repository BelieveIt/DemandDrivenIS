package test;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import merchandise.utils.CategoryUtil;

public class TestCase {
public static void main(String[] args){
//	BasicListCategoryDao basicListCategoryDao = new BasicListCategoryDao();
//	List<BasicListCategory> basicListCategories = basicListCategoryDao.queryCategoriesByVersionId("head");
//
//	TreeNode rootOfFranchiser1 = CategoryUtil.generateTree(basicListCategories, CategoryUtil.ORDER_BY_NAME, CategoryUtil.ROOT_FATHER_ID);
//	BasicListCategory b1 = (BasicListCategory) rootOfFranchiser1.getChildren().get(0).getData();
//
//	TreeNode rootOfFranchiser2 = CategoryUtil.generateTree(basicListCategories, CategoryUtil.ORDER_BY_TIME, CategoryUtil.ROOT_FATHER_ID);
//	BasicListCategory b2 = (BasicListCategory) rootOfFranchiser2.getChildren().get(0).getData();
//	System.out.println(b1.getCategoryName());
//	System.out.println(b2.getCategoryName());
//
//
//	CategoryUtil.compareLocalTreeAndBasicList("1", "head");
	
//	SimpleRegression regression = new SimpleRegression();
//	regression.addData(1d, 1d);
//	// At this point, with only one observation,
//	// all regression statistics will return NaN
//
//	regression.addData(3d, 3d);
//	// With only two observations,
//	// slope and intercept can be computed
//	// but inference statistics will return NaN
//
//	regression.addData(5d, 5d);
//	System.out.println(regression.predict(1.5d));
	
	Calendar calendar = Calendar.getInstance(); 
	calendar.setTime(new Date());
	calendar.add(Calendar.MONTH, -13); 
	for(int i = 1; i <= 12; i++){
		calendar.add(Calendar.MONTH, 1);   
		int year = calendar.get(Calendar.YEAR); 
		int month = calendar.get(Calendar.MONTH)+1;
		String monthString = "";
		if(month < 10) {
			monthString = "0" + month;
		}else {
			monthString = Integer.toString(month);
		}
		System.out.println(Integer.toString(year) + monthString);
	}
}
}
