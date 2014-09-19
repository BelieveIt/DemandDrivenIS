package model;

import java.util.Date;

public class Category implements Comparable<Category>{
	private String categoryId;
	private String categoryFatherId;
	private String categoryName;
	private Date createTime;
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryFatherId() {
		return categoryFatherId;
	}
	public void setCategoryFatherId(String categoryFatherId) {
		this.categoryFatherId = categoryFatherId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	@Override
	public int compareTo(Category category) {
		return this.getCategoryName().compareTo(category.getCategoryName());
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
