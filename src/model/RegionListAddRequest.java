package model;

import java.io.Serializable;
import java.util.Date;

public class RegionListAddRequest implements Serializable{
private static final long serialVersionUID = -6185765563906069490L;
private String requestId;
private String requestComment;
private Date createTime;
private String regionId;
public String getRequestId() {
	return requestId;
}
public void setRequestId(String requestId) {
	this.requestId = requestId;
}
public String getRequestComment() {
	return requestComment;
}
public void setRequestComment(String requestComment) {
	this.requestComment = requestComment;
}
public Date getCreateTime() {
	return createTime;
}
public void setCreateTime(Date createTime) {
	this.createTime = createTime;
}
public String getRegionId() {
	return regionId;
}
public void setRegionId(String regionId) {
	this.regionId = regionId;
}
}
