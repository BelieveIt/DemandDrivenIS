package model;

import java.util.Date;

public class RegionListUpdateInfo {
private String verionId;
private Date finishedTime;
private String regionId;
private Integer isFinished;
private Date createTime;
public String getVerionId() {
	return verionId;
}
public void setVerionId(String verionId) {
	this.verionId = verionId;
}
public Date getFinishedTime() {
	return finishedTime;
}
public void setFinishedTime(Date finishedTime) {
	this.finishedTime = finishedTime;
}
public String getRegionId() {
	return regionId;
}
public void setRegionId(String regionId) {
	this.regionId = regionId;
}
public Integer getIsFinished() {
	return isFinished;
}
public void setIsFinished(Integer isFinished) {
	this.isFinished = isFinished;
}
public Date getCreateTime() {
	return createTime;
}
public void setCreateTime(Date createTime) {
	this.createTime = createTime;
}
}
