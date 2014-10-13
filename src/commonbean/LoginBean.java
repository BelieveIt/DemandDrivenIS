package commonbean;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.context.RequestContext;

import dao.SysUserDao;


import model.SysUser;
@ManagedBean(name="loginBean")
@SessionScoped
public class LoginBean implements Serializable{
	private static final long serialVersionUID = 9083703210663336815L;
	private String username;
	private String password;
	private SysUser sysUser;

    public void doLogin(ActionEvent event){
    	username = username.trim();
    	password = password.trim();
    	RequestContext context = RequestContext.getCurrentInstance();
    	if(username==null || username.equals("") || password==null || password.equals("") ){
    		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Sorry!", "Username and password can't be empty."));
    		context.addCallbackParam("logged", false);
    	}else {
    		SysUserDao sysUserDao = new SysUserDao();
    		sysUser = sysUserDao.querySysUser(username, password);
    		if(sysUser!=null){
    			context.addCallbackParam("logged", true);
    			context.addCallbackParam("redirect",LoginNavi.redirectToHome(sysUser.getUsertype()));
        	}else {
        		context.addCallbackParam("logged", false);
        		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal!", "Username or password is wrong."));
			}
		}
    }
    public void doLogout(ActionEvent event){
    	sysUser = null;
    	username = null;
    	password = null;
    }

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
	public SysUser getSysUser() {
		return sysUser;
	}
	public void setSysUser(SysUser sysUser) {
		this.sysUser = sysUser;
	}
}
