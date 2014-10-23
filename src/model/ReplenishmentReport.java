package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReplenishmentReport {
public static final String WAITING_FOR_APPROVAL = "WaitingForApproval";
public static final String APPROVED = "Approved";
public static final String APPROVED_PARTLY = "ApprovedPartly";
public static final String REJECTED = "Rejected";
private String reportId;
private String storeId;
private Date createTime;
private Date deliveryTime;
private String deliveryType;
private String commentFromRegion;
private String status;
private List<ReplenishmentReportItem> replenishmentReportItems;
private Store store;

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
public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}
public List<ReplenishmentReportItem> getReplenishmentReportItems() {
	return replenishmentReportItems;
}
public void setReplenishmentReportItems(List<ReplenishmentReportItem> replenishmentReportItems) {
	this.replenishmentReportItems = replenishmentReportItems;
}
public String getDeliveryType() {
	return deliveryType;
}
public void setDeliveryType(String deliveryType) {
	this.deliveryType = deliveryType;
}
public Store getStore() {
	return store;
}
public void setStore(Store store) {
	this.store = store;
}
public String getCommentFromRegion() {
	return commentFromRegion;
}
public void setCommentFromRegion(String commentFromRegion) {
	this.commentFromRegion = commentFromRegion;
}
}
