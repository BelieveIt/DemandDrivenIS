package utils;

import javax.servlet.ServletRequest;

public class AccessControlUtil {
	public static final String STORE_INVENTORY_ADMIN = "storeInventoryAdmin";
	public static boolean isAccessible(ServletRequest request, String roleName){
		return true;
	}
}
