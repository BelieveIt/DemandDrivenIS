package inventorybean.region;

import inventorybean.utils.AnalysisUtil;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.TreeNode;

import dao.RegionDao;
import dao.RegionListCategoryDao;
import dao.SalesRecordDao;
import dao.StockOutVirtualSalesDao;
import dao.StoreDao;
import dao.WasteRecordDao;

import model.Region;
import model.Store;
import model.StoreSellingItem;

@ManagedBean(name="analysisOfRegion")
@ViewScoped
public class AnalysisOfRegion implements Serializable{
	private static final long serialVersionUID = 5265661455745070133L;
	@ManagedProperty(value="#{analysisUtil}")
	private AnalysisUtil analysisUtil;
	
	private StoreDao storeDao;
	private RegionDao regionDao;
	private RegionListCategoryDao categoryDao;
	private SalesRecordDao salesRecordDao;

	
	private Store currentStore;
	private Region currentRegion;
	
	private TreeNode rootNode;
	private TreeNode selectedNode;
	
	private List<StoreSellingItem> storeSellingItems;
	private List<StoreSellingItem> selectedItems;
	private List<StoreSellingItem> filteredItems;

	private StoreSellingItem selectedItem;
	
	@PostConstruct
	public void init(){
		storeDao = new StoreDao();
		regionDao = new RegionDao();
		categoryDao = new RegionListCategoryDao();
		salesRecordDao = new SalesRecordDao();

		//TODO
		String storeId = "1";
		currentStore = storeDao.queryStoreById(storeId);
		currentRegion = regionDao.queryRegionById(currentStore.getRegionId());
	}
}
