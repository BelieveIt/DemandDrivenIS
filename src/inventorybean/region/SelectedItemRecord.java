package inventorybean.region;

import java.util.ArrayList;

import model.SalesRecord;
import model.StockOutVirtualSales;
import model.WasteRecord;

public class SelectedItemRecord {
private ArrayList<SalesRecord> salesRecords;
private ArrayList<StockOutVirtualSales> stockOutVirtualSalesRecords;
private ArrayList<WasteRecord> wasteRecords;
public ArrayList<SalesRecord> getSalesRecords() {
	return salesRecords;
}
public void setSalesRecords(ArrayList<SalesRecord> salesRecords) {
	this.salesRecords = salesRecords;
}
public ArrayList<StockOutVirtualSales> getStockOutVirtualSalesRecords() {
	return stockOutVirtualSalesRecords;
}
public void setStockOutVirtualSalesRecords(
		ArrayList<StockOutVirtualSales> stockOutVirtualSalesRecords) {
	this.stockOutVirtualSalesRecords = stockOutVirtualSalesRecords;
}
public ArrayList<WasteRecord> getWasteRecords() {
	return wasteRecords;
}
public void setWasteRecords(ArrayList<WasteRecord> wasteRecords) {
	this.wasteRecords = wasteRecords;
}
}
