package model;

import java.io.Serializable;
import java.util.Date;

public class SellingList implements Serializable{
private static final long serialVersionUID = 2544784618024012766L;
private String listId;
private Date createTime;
private String regionId;
public String getListId() {
	return listId;
}
public void setListId(String listId) {
	this.listId = listId;
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
