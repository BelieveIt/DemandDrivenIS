package model;

import java.io.Serializable;

public class BasicListCategory extends Category implements Serializable{
	private static final long serialVersionUID = -6750985994930619338L;
	private String versionId;
	public String getVersionId() {
		return versionId;
	}
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
}
