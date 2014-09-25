package commonbean;

public class LoginNavi {
	public String redirectToHome(String group){
		if(group.equals("franchiserMerchandise")){
			return "franchiser/merchandise/category.xhtml?faces-redirect=true";
		}
		if(group.equals("regionMerchandise")){
			return "region/merchandise/category.xhtml?faces-redirect=true";
		}
		return "error.xhtml?faces-redirect=true";
	}

	public String toLogin(){
		return "login.xhtml?faces-redirect=true";
	}
}
