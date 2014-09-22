package merchandise.utils;

import java.io.Serializable;
import java.util.Date;

import model.RegionListUpdateInfo;

public class RegionVersionListItem implements Serializable{
	private static final long serialVersionUID = 7853288422243264161L;
	private Integer isOutDated;
	private String versionId;
	private Date createTime;
	private Integer isRetrived;
	private Integer isCategoryUpdated;
	private Integer isProductUpdated;
	private RegionListUpdateInfo regionListUpdateInfo;
	public Integer getIsRetrived() {
		return isRetrived;
	}
	public void setIsRetrived(Integer isRetrived) {
		this.isRetrived = isRetrived;
	}
	public RegionListUpdateInfo getRegionListUpdateInfo() {
		return regionListUpdateInfo;
	}
	public void setRegionListUpdateInfo(RegionListUpdateInfo regionListUpdateInfo) {
		this.regionListUpdateInfo = regionListUpdateInfo;
	}
	public Integer getIsCategoryUpdated() {
		return isCategoryUpdated;
	}
	public void setIsCategoryUpdated(Integer isCategoryUpdated) {
		this.isCategoryUpdated = isCategoryUpdated;
	}
	public Integer getIsProductUpdated() {
		return isProductUpdated;
	}
	public void setIsProductUpdated(Integer isProductUpdated) {
		this.isProductUpdated = isProductUpdated;
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
	public Integer getIsOutDated() {
		return isOutDated;
	}
	public void setIsOutDated(Integer isOutDated) {
		this.isOutDated = isOutDated;
	}
}
