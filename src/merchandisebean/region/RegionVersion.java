package merchandisebean.region;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import dao.RegionListUpdateInfoDao;


import merchandise.utils.RegionVersionListItem;
import merchandise.utils.VersionUtil;
import model.RegionListUpdateInfo;
@ManagedBean(name="regionVersion")
@ViewScoped
public class RegionVersion {
	private RegionListUpdateInfoDao regionListUpdateInfoDao;

	private List<RegionVersionListItem> regionVersionListItems;

	private String currentRegionId;
	@PostConstruct
	public void init(){
		//TODO
		currentRegionId = "1";

		regionListUpdateInfoDao = new RegionListUpdateInfoDao();
		List<RegionListUpdateInfo> regionListUpdateInfos = regionListUpdateInfoDao.queryRegionListUpdateInfosByRegionId(currentRegionId);
		regionVersionListItems = VersionUtil.generateRegionVersionListItems(regionListUpdateInfos);
	}


	public List<RegionVersionListItem> getRegionVersionListItems() {
		return regionVersionListItems;
	}

	public void setRegionVersionListItems(List<RegionVersionListItem> regionVersionListItems) {
		this.regionVersionListItems = regionVersionListItems;
	}

}
