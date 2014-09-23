package model;

import java.util.Date;

public class RegionListUpdateInfo {
private String versionId;
private Date finishedTime;
private String regionId;
private Integer isFinished;
private Date createTime;

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
public String getVersionId() {
	return versionId;
}
public void setVersionId(String versionId) {
	this.versionId = versionId;
}
}
