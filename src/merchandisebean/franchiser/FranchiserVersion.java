package merchandisebean.franchiser;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;


import dao.BasicListDao;
import dao.RegionListUpdateInfoDao;

import merchandise.utils.VersionUtil;
import model.BasicList;
import model.RegionListUpdateInfo;

@ManagedBean(name="franchiserVersion")
@ViewScoped
public class FranchiserVersion implements Serializable{
private static final long serialVersionUID = -3694259330226236227L;
private List<BasicList> basicLists;
private BasicList selectedBasicList;
private BasicListDao basicListDao;
private RegionListUpdateInfoDao regionListUpdateInfoDao;

@PostConstruct
public void init(){
	basicListDao = new BasicListDao();
	regionListUpdateInfoDao = new RegionListUpdateInfoDao();
	basicLists = basicListDao.queryBasicLists();
}

//Create
public void openCreateVersion(){
	RequestContext.getCurrentInstance().execute("PF('createVersion').show();");
}

public void createVersion(){
	VersionUtil.createNewBasicList();
	basicLists = basicListDao.queryBasicLists();
	
	RequestContext.getCurrentInstance().execute("PF('createVersion').hide();");
}

//View
public void viewVersion(){
	
}

public List<BasicList> getBasicLists() {
	return basicLists;
}

public void setBasicLists(List<BasicList> basicLists) {
	this.basicLists = basicLists;
}

public BasicList getSelectedBasicList() {
	return selectedBasicList;
}

public void setSelectedBasicList(BasicList selectedBasicList) {
	this.selectedBasicList = selectedBasicList;
}

}
