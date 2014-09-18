package model;

import java.io.Serializable;

public class RegionListCategory extends Category implements Serializable{
private static final long serialVersionUID = 7400679166009370232L;
private String regionId;
private String versionId;

public String getVersionId() {
	return versionId;
}
public void setVersionId(String versionId) {
	this.versionId = versionId;
}
public String getRegionId() {
	return regionId;
}
public void setRegionId(String regionId) {
	this.regionId = regionId;
}

}
