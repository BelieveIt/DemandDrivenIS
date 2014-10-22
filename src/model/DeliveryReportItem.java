package model;

public class DeliveryReportItem {
private String reportId;
private String productId;
private Integer deliveryNumber;

private RegionListItem regionListItem;

private StoreSellingItem storeSellingItem;

public String getReportId() {
	return reportId;
}
public void setReportId(String reportId) {
	this.reportId = reportId;
}
public String getProductId() {
	return productId;
}
public void setProductId(String productId) {
	this.productId = productId;
}
public Integer getDeliveryNumber() {
	return deliveryNumber;
}
public void setDeliveryNumber(Integer deliveryNumber) {
	this.deliveryNumber = deliveryNumber;
}
public RegionListItem getRegionListItem() {
	return regionListItem;
}
public void setRegionListItem(RegionListItem regionListItem) {
	this.regionListItem = regionListItem;
}
public StoreSellingItem getStoreSellingItem() {
	return storeSellingItem;
}
public void setStoreSellingItem(StoreSellingItem storeSellingItem) {
	this.storeSellingItem = storeSellingItem;
}
}
