package inventorybean.utils;

import model.StoreSellingItem;

public class SalesVolumeLowMoving {
	private StoreSellingItem sellingItem;
	private Integer salesVolume;
	public StoreSellingItem getSellingItem() {
		return sellingItem;
	}
	public void setSellingItem(StoreSellingItem sellingItem) {
		this.sellingItem = sellingItem;
	}
	public Integer getSalesVolume() {
		return salesVolume;
	}
	public void setSalesVolume(Integer salesVolume) {
		this.salesVolume = salesVolume;
	}
}
