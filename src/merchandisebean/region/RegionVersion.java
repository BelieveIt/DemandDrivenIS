package merchandisebean.region;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.primefaces.context.RequestContext;

import dao.RegionListUpdateInfoDao;


import merchandise.utils.RegionVersionListItem;
import merchandise.utils.VersionUtil;
import model.RegionListUpdateInfo;
@ManagedBean(name="regionVersion")
@ViewScoped
public class RegionVersion implements Serializable{
	private static final long serialVersionUID = -6525298726394719841L;

	private RegionListUpdateInfoDao regionListUpdateInfoDao;

	private List<RegionVersionListItem> regionVersionListItems;
	private RegionVersionListItem selectedRegionVersionListItem;
	private RegionVersionListItem selectedRegionVersionListItemNext;
	private String leftMenuNewAlert;

	private String currentRegionId;
	@PostConstruct
	public void init(){
		//TODO
		currentRegionId = "1";
		regionListUpdateInfoDao = new RegionListUpdateInfoDao();
		initProcess();
	}

	public void initProcess(){
		List<RegionListUpdateInfo> regionListUpdateInfos = regionListUpdateInfoDao.queryRegionListUpdateInfosByRegionId(currentRegionId);
		regionVersionListItems = VersionUtil.generateRegionVersionListItems(regionListUpdateInfos);
		if(regionVersionListItems.size() != 0){
			Integer newestVersionStatus = regionVersionListItems.get(0).getIsRetrived();
			if(newestVersionStatus == 1){
				leftMenuNewAlert = null;
			}else {
				leftMenuNewAlert = "new";
			}
		}else {
			leftMenuNewAlert = null;
		}

	}

	public void retriveBasicList(ActionEvent actionEvent){
		VersionUtil.retriveFromFranchiser(currentRegionId);
		initProcess();
	}

	public void openViewVersionDetail(){
		RequestContext.getCurrentInstance().execute("PF('viewVersion').show();");
	}

	public List<RegionVersionListItem> getRegionVersionListItems() {
		return regionVersionListItems;
	}

	public void setRegionVersionListItems(List<RegionVersionListItem> regionVersionListItems) {
		this.regionVersionListItems = regionVersionListItems;
	}

	public RegionVersionListItem getSelectedRegionVersionListItem() {
		return selectedRegionVersionListItem;
	}

	public void setSelectedRegionVersionListItem(
			RegionVersionListItem selectedRegionVersionListItem) {
		this.selectedRegionVersionListItem = selectedRegionVersionListItem;
		int index = -1;
		for(int i = 0; i < regionVersionListItems.size(); i++){
			if(regionVersionListItems.get(i).getVersionId().equals(selectedRegionVersionListItem.getVersionId())){
				index = i;
			}
		}
		if(index+1 >= regionVersionListItems.size()){
			selectedRegionVersionListItemNext = null;
		}else {
			selectedRegionVersionListItemNext = regionVersionListItems.get(index+1);
		}
	}

	public RegionVersionListItem getSelectedRegionVersionListItemNext() {
		return selectedRegionVersionListItemNext;
	}

	public void setSelectedRegionVersionListItemNext(
			RegionVersionListItem selectedRegionVersionListItemNext) {
		this.selectedRegionVersionListItemNext = selectedRegionVersionListItemNext;
	}

	public String getLeftMenuNewAlert() {
		return leftMenuNewAlert;
	}

	public void setLeftMenuNewAlert(String leftMenuNewAlert) {
		this.leftMenuNewAlert = leftMenuNewAlert;
	}

}
