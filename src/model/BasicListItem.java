package model;

import java.io.Serializable;

public class BasicListItem implements Serializable{
private static final long serialVersionUID = -2749664647911565243L;
private String productId;
private String categoryId;
private String versionId;
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
public String getVersionId() {
	return versionId;
}
public void setVersionId(String versionId) {
	this.versionId = versionId;
}
public Product getProduct() {
	return product;
}
public void setProduct(Product product) {
	this.product = product;
}
}
