package model;

import java.io.Serializable;

public class BasicListCategory implements Serializable{
	private static final long serialVersionUID = -6750985994930619338L;
	private String categoryId;
	private String versionId;
	private String categoryFatherId;
	private String categoryName;
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getVersionId() {
		return versionId;
	}
	public void setVersionId(String versionId) {
		this.versionId = versionId;
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
