package model;

import java.io.Serializable;
import java.util.Date;

public class StockOutVirtualSales implements Serializable{
private static final long serialVersionUID = -6216903951665464779L;
private String storeId;
private String productId;
private Integer stockoutNumber;
private Date createTime;
public String getStoreId() {
	return storeId;
}
public void setStoreId(String storeId) {
	this.storeId = storeId;
}
public String getProductId() {
	return productId;
}
public void setProductId(String productId) {
	this.productId = productId;
}
public Integer getStockoutNumber() {
	return stockoutNumber;
}
public void setStockoutNumber(Integer stockoutNumber) {
	this.stockoutNumber = stockoutNumber;
}
public Date getCreateTime() {
	return createTime;
}
public void setCreateTime(Date createTime) {
	this.createTime = createTime;
}
}
