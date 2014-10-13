package model;

import java.io.Serializable;

public class SysUser implements Serializable{
	private static final long serialVersionUID = -4470223692103400168L;
	public final static String FRANCHISER_MERCHANDISE = "franchiserMerchandise";
	public final static String REGION_MERCHANDISE = "regionMerchandise";
	public final static String REGION_INVENTORY = "regionInventory";
	public final static String STORE_INVENTORY = "storeInventory";
	private String username;
	private String password;
	private String usertype;
	private String userId;
	private Object userObject;

	private String operatingUnit;
	private String roleName;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUsertype() {
		return usertype;
	}
	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}
	public Object getUserObject() {
		return userObject;
	}
	public void setUserObject(Object userObject) {
		this.userObject = userObject;
	}
	public String getOperatingUnit() {
		return operatingUnit;
	}
	public void setOperatingUnit(String operatingUnit) {
		this.operatingUnit = operatingUnit;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}
