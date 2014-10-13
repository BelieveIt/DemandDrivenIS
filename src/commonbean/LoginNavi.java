package commonbean;

public class LoginNavi {
	public static String redirectToHome(String userType){
		if(userType.equals("franchiserMerchandise")){
			return "franchiser/merchandise/category.xhtml?faces-redirect=true";
		}
		if(userType.equals("regionMerchandise")){
			return "region/merchandise/category.xhtml?faces-redirect=true";
		}
		if(userType.equals("storeInventory")){
			return "store/inventory/monitor.xhtml?faces-redirect=true";
		}
		if(userType.equals("regionInventory")){
			return "region/inventory/viewRequests.xhtml?faces-redirect=true";
		}
		return "error.xhtml?faces-redirect=true";
	}

	public static String toLogin(){
		return "login.xhtml?faces-redirect=true";
	}
}
