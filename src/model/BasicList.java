package model;

import java.io.Serializable;
import java.util.Date;

public class BasicList implements Serializable{
private static final long serialVersionUID = 4452715997879797483L;
private String versionId;
private Date createTime;
public String getVersionId() {
	return versionId;
}
public void setVersionId(String versionId) {
	this.versionId = versionId;
}
public Date getCreateTime() {
	return createTime;
}
public void setCreateTime(Date createTime) {
	this.createTime = createTime;
}

}
