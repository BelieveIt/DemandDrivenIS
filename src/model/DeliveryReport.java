package model;

import java.util.Date;
import java.util.List;

public class DeliveryReport {
public static final String NOT_UPDATED = "NotUpdated";
public static final String UPDATED = "InventoryUpdated";
private String reportId;
private String storeId;
private Date createTime;
private Date deliveryTime;
private String updated;
private List<DeliveryReportItem> deliveryReportItems;

public String getReportId() {
	return reportId;
}
public void setReportId(String reportId) {
	this.reportId = reportId;
}
public String getStoreId() {
	return storeId;
}
public void setStoreId(String storeId) {
	this.storeId = storeId;
}
public Date getCreateTime() {
	return createTime;
}
public void setCreateTime(Date createTime) {
	this.createTime = createTime;
}
public Date getDeliveryTime() {
	return deliveryTime;
}
public void setDeliveryTime(Date deliveryTime) {
	this.deliveryTime = deliveryTime;
}
public List<DeliveryReportItem> getDeliveryReportItems() {
	return deliveryReportItems;
}
public void setDeliveryReportItems(List<DeliveryReportItem> deliveryReportItems) {
	this.deliveryReportItems = deliveryReportItems;
}
public String getUpdated() {
	return updated;
}
public void setUpdated(String updated) {
	this.updated = updated;
}

}
