package inventorybean.store;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import model.DeliveryReport;
import model.DeliveryReportItem;
import model.Store;
import model.StoreSellingItem;

import dao.DeliveryReportDao;
import dao.ReplenishmentReportDao;
import dao.StoreDao;

@ManagedBean(name="storeReceive")
@ViewScoped
public class StoreReceive  implements Serializable{
	private static final long serialVersionUID = 1966364791284449550L;
	private DeliveryReportDao deliveryReportDao;
	private StoreDao storeDao;

	private Integer needToUpdateNum;
	private List<DeliveryReport> reports;
	private Store store;
	private DeliveryReport selectedReport;

	@PostConstruct
	public void init(){
		deliveryReportDao = new DeliveryReportDao();
		storeDao = new StoreDao();
		setStore(storeDao.queryStoreById("1"));

		initProcess();
	}

	public void initProcess(){
		reports = deliveryReportDao.queryDeliveryReportsByStoreId(store.getStoreId());
		needToUpdateNum = 0;
		if(reports!=null){
			for(DeliveryReport report : reports){
				if(report.getUpdated().equals(DeliveryReport.NOT_UPDATED)){
					needToUpdateNum = needToUpdateNum + 1;
				}
			}
		}
		if(needToUpdateNum == 0) needToUpdateNum = null;
	}

	public void openUpdateInventory(){
		RequestContext.getCurrentInstance().execute("PF('updateInventory').show();");
	}

	public void updateInventory(){
		List<DeliveryReportItem> items = selectedReport.getDeliveryReportItems();
		List<StoreSellingItem> storeSellingItems = storeDao.queryStoreSellingItemsByStoreId(store.getStoreId());
		HashMap<String, StoreSellingItem> storeSellingItemsMap = new HashMap<String, StoreSellingItem>();
		for(StoreSellingItem storeSellingItem : storeSellingItems){
			storeSellingItemsMap.put(storeSellingItem.getProductId(), storeSellingItem);
		}
		for(DeliveryReportItem item : items){
			StoreSellingItem sellingItem = storeSellingItemsMap.get(item.getProductId());
			Integer currentInventory = sellingItem.getCurrentInventory();
			Integer newInventory = currentInventory + item.getDeliveryNumber();
			sellingItem.setCurrentInventory(newInventory);
			storeDao.updateStoreSellingItem(sellingItem);
		}
		selectedReport.setUpdated(DeliveryReport.UPDATED);
		deliveryReportDao.updateDeliveryReport(selectedReport);
		initProcess();
		RequestContext.getCurrentInstance().execute("PF('updateInventory').hide();");
	}

	public Integer getNeedToUpdateNum() {
		return needToUpdateNum;
	}
	public void setNeedToUpdateNum(Integer needToUpdateNum) {
		this.needToUpdateNum = needToUpdateNum;
	}
	public List<DeliveryReport> getReports() {
		return reports;
	}
	public void setReports(List<DeliveryReport> reports) {
		this.reports = reports;
	}
	public Store getStore() {
		return store;
	}
	public void setStore(Store store) {
		this.store = store;
	}
	public DeliveryReportDao getDeliveryReportDao() {
		return deliveryReportDao;
	}
	public void setDeliveryReportDao(DeliveryReportDao deliveryReportDao) {
		this.deliveryReportDao = deliveryReportDao;
	}

	public DeliveryReport getSelectedReport() {
		return selectedReport;
	}

	public void setSelectedReport(DeliveryReport selectedReport) {
		this.selectedReport = selectedReport;
	}

}
