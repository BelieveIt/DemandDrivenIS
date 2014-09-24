package merchandise.utils;

import java.util.ArrayList;

import model.RegionListItem;

public class RegionListItemDiff {
private ArrayList<RegionListItem> deletedItems;
private ArrayList<RegionListItem> editedItems;
private ArrayList<RegionListItem> addedItems;
public RegionListItemDiff(){
	deletedItems = new ArrayList<RegionListItem>();
	editedItems = new ArrayList<RegionListItem>();
	addedItems = new ArrayList<RegionListItem>();
}

public ArrayList<RegionListItem> getDeletedItems() {
	return deletedItems;
}
public void setDeletedItems(ArrayList<RegionListItem> deletedItems) {
	this.deletedItems = deletedItems;
}
public ArrayList<RegionListItem> getEditedItems() {
	return editedItems;
}
public void setEditedItems(ArrayList<RegionListItem> editedItems) {
	this.editedItems = editedItems;
}
public ArrayList<RegionListItem> getAddedItems() {
	return addedItems;
}
public void setAddedItems(ArrayList<RegionListItem> addedItems) {
	this.addedItems = addedItems;
}
}
