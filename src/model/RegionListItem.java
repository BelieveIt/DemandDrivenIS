package model;

import java.io.Serializable;

public class RegionListItem implements Serializable{
private static final long serialVersionUID = 77385688458549152L;
private String productId;
private String categoryId;
private String reginoId;
private String versionId;
private Integer isRegionAdd;
private Integer isConfirmed;
private Product product;
public String getProductId() {
	return productId;
}
public void setProductId(String productId) {
	this.productId = productId;
}
public String getCategoryId() {
	return categoryId;
}
public void setCategoryId(String categoryId) {
	this.categoryId = categoryId;
}
public String getReginoId() {
	return reginoId;
}
public void setReginoId(String reginoId) {
	this.reginoId = reginoId;
}
public String getVersionId() {
	return versionId;
}
public void setVersionId(String versionId) {
	this.versionId = versionId;
}
public Integer getIsRegionAdd() {
	return isRegionAdd;
}
public void setIsRegionAdd(Integer isRegionAdd) {
	this.isRegionAdd = isRegionAdd;
}
public Integer getIsConfirmed() {
	return isConfirmed;
}
public void setIsConfirmed(Integer isConfirmed) {
	this.isConfirmed = isConfirmed;
}
public Product getProduct() {
	return product;
}
public void setProduct(Product product) {
	this.product = product;
}
}
