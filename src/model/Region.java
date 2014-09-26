package model;

import java.io.Serializable;

public class Region implements Serializable{

	private static final long serialVersionUID = 2267346057827412715L;
	private String regionId;
	private String name;
	public String getRegionId() {
		return regionId;
	}
	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
