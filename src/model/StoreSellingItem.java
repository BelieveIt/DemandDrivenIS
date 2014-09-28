package model;

import java.io.Serializable;
import java.util.Date;

public class StoreSellingItem implements Serializable{
private static final long serialVersionUID = -5116036526271753035L;
private String storeId;
private String productId;
private Integer isAvailable;
private RegionListItem regionListItem;
private Integer currentInventory;
private Date stockoutOccurrenceTime;
private String stockoutDuration;
public String getStoreId() {
	return storeId;
}
public void setStoreId(String storeId) {
	this.storeId = storeId;
}
public Integer getIsAvailable() {
	return isAvailable;
}
public void setIsAvailable(Integer isAvailable) {
	this.isAvailable = isAvailable;
}
public RegionListItem getRegionListItem() {
	return regionListItem;
}
public void setRegionListItem(RegionListItem regionListItem) {
	this.regionListItem = regionListItem;
}
public Integer getCurrentInventory() {
	return currentInventory;
}
public void setCurrentInventory(Integer currentInventory) {
	this.currentInventory = currentInventory;
}
public String getProductId() {
	return productId;
}
public void setProductId(String productId) {
	this.productId = productId;
}
public Date getStockoutOccurrenceTime() {
	return stockoutOccurrenceTime;
}
public void setStockoutOccurrenceTime(Date stockoutOccurrenceTime) {
	this.stockoutOccurrenceTime = stockoutOccurrenceTime;
}
public String getStockoutDuration() {
	return stockoutDuration;
}
public void setStockoutDuration(String stockoutDuration) {
	this.stockoutDuration = stockoutDuration;
}
}
