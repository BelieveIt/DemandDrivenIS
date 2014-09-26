package model;

import java.io.Serializable;
import java.util.Date;

public class WasteRecord implements Serializable{
private static final long serialVersionUID = 7540494728094163764L;
private String storeId;
private String productId;
private Integer wasteNumber;
private String reasonId;
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
public Integer getWasteNumber() {
	return wasteNumber;
}
public void setWasteNumber(Integer wasteNumber) {
	this.wasteNumber = wasteNumber;
}
public String getReasonId() {
	return reasonId;
}
public void setReasonId(String reasonId) {
	this.reasonId = reasonId;
}
public Date getCreateTime() {
	return createTime;
}
public void setCreateTime(Date createTime) {
	this.createTime = createTime;
}

}
