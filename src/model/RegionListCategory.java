package model;

import java.io.Serializable;

public class RegionListCategory implements Serializable{
private static final long serialVersionUID = 7400679166009370232L;
private String categoryId;
private String regionId;
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
public String getRegionId() {
	return regionId;
}
public void setRegionId(String regionId) {
	this.regionId = regionId;
}

}
