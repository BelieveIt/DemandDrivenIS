package model;

import java.io.Serializable;
import java.util.Date;

public class SalesRecord implements Serializable{
private static final long serialVersionUID = -4702771532648194529L;
private String storeId;
private String productId;
private Integer salesNumber;
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
public Integer getSalesNumber() {
	return salesNumber;
}
public void setSalesNumber(Integer salesNumber) {
	this.salesNumber = salesNumber;
}
public Date getCreateTime() {
	return createTime;
}
public void setCreateTime(Date createTime) {
	this.createTime = createTime;
}

}
