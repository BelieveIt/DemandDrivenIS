package model;

import java.io.Serializable;
import java.util.Date;

public class Store implements Serializable{
private static final long serialVersionUID = -4891727707354362705L;
private String storeId;
private String regionId;
private Date createTime;
private String name;
private String address;
private Integer operatingArea;
private String environmentId;
private String environmentName;
public String getStoreId() {
	return storeId;
}
public void setStoreId(String storeId) {
	this.storeId = storeId;
}
public String getRegionId() {
	return regionId;
}
public void setRegionId(String regionId) {
	this.regionId = regionId;
}
public Date getCreateTime() {
	return createTime;
}
public void setCreateTime(Date createTime) {
	this.createTime = createTime;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getAddress() {
	return address;
}
public void setAddress(String address) {
	this.address = address;
}
public Integer getOperatingArea() {
	return operatingArea;
}
public void setOperatingArea(Integer operatingArea) {
	this.operatingArea = operatingArea;
}
public String getEnvironmentId() {
	return environmentId;
}
public void setEnvironmentId(String environmentId) {
	this.environmentId = environmentId;
}
public String getEnvironmentName() {
	return environmentName;
}
public void setEnvironmentName(String environmentName) {
	this.environmentName = environmentName;
}

}
