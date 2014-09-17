package model;

public class RegionListAddRequestItem {
private String requestId;
private String productId;
private Integer isApproved;
public String getRequestId() {
	return requestId;
}
public void setRequestId(String requestId) {
	this.requestId = requestId;
}
public String getProductId() {
	return productId;
}
public void setProductId(String productId) {
	this.productId = productId;
}
public Integer getIsApproved() {
	return isApproved;
}
public void setIsApproved(Integer isApproved) {
	this.isApproved = isApproved;
}
}
