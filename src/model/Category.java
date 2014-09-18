package model;

public class Category {
	private String categoryId;
	private String categoryFatherId;
	private String categoryName;
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
}
