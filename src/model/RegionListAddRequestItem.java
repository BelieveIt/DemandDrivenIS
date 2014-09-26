package model;

import java.io.Serializable;

public class RegionListAddRequestItem implements Serializable{
private static final long serialVersionUID = -7102812018195430990L;
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
