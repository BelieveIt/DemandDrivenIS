package model;

import java.io.Serializable;
import java.util.Date;

public class BasicListCategory extends Category implements Serializable{
	private static final long serialVersionUID = -6750985994930619338L;
	private String versionId;
	public BasicListCategory(){}
	
	public BasicListCategory(String categoryId, String categoryFatherId,
			String categoryName, Date createTime, String versionId) {
		super(categoryId, categoryFatherId, categoryName, createTime);
		this.versionId = versionId;
	}

	public String getVersionId() {
		return versionId;
	}
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
}
