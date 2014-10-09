package model;

public class ReplenishmentReportItem {
private String reportId;
private String productId;
private Integer replenishmentNumber;
private Integer currentInventory;
private Integer wasteForecast;
private Integer salesForecast;
private Integer minInventoryOnNextDelivery;

private RegionListItem regionListItem;
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
public Integer getReplenishmentNumber() {
	return replenishmentNumber;
}
public void setReplenishmentNumber(Integer replenishmentNumber) {
	this.replenishmentNumber = replenishmentNumber;
}
public Integer getCurrentInventory() {
	return currentInventory;
}
public void setCurrentInventory(Integer currentInventory) {
	this.currentInventory = currentInventory;
}
public Integer getWasteForecast() {
	return wasteForecast;
}
public void setWasteForecast(Integer wasteForecast) {
	this.wasteForecast = wasteForecast;
}
public Integer getSalesForecast() {
	return salesForecast;
}
public void setSalesForecast(Integer salesForecast) {
	this.salesForecast = salesForecast;
}
public Integer getMinInventoryOnNextDelivery() {
	return minInventoryOnNextDelivery;
}
public void setMinInventoryOnNextDelivery(Integer minInventoryOnNextDelivery) {
	this.minInventoryOnNextDelivery = minInventoryOnNextDelivery;
}
public RegionListItem getRegionListItem() {
	return regionListItem;
}
public void setRegionListItem(RegionListItem regionListItem) {
	this.regionListItem = regionListItem;
}

}
