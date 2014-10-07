package inventorybean.utils;

import java.util.LinkedHashMap;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;

import dao.WasteReasonDao;

@ManagedBean(name="wasteReasonSelect")
@SessionScoped
public class WasteReasonSelect {
	private LinkedHashMap<String, String> reasonsMap;
	@PostConstruct
	public void init(){
		WasteReasonDao wasteReasonDao = new WasteReasonDao();
		reasonsMap = wasteReasonDao.queryWasteRecordsMap();
	}
	public LinkedHashMap<String, String> getReasonsMap() {
		return reasonsMap;
	}
	public void setReasonsMap(LinkedHashMap<String, String> reasonsMap) {
		this.reasonsMap = reasonsMap;
	}
}
