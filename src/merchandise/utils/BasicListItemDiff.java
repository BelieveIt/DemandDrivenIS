package merchandise.utils;

import java.util.ArrayList;

import model.BasicListItem;

public class BasicListItemDiff {
private ArrayList<BasicListItem> deletedItems;
private ArrayList<BasicListItem> editedItems;
private ArrayList<BasicListItem> addedItems;
public BasicListItemDiff(){
	deletedItems = new ArrayList<BasicListItem>();
	editedItems = new ArrayList<BasicListItem>();
	addedItems = new ArrayList<BasicListItem>();
}

public ArrayList<BasicListItem> getDeletedItems() {
	return deletedItems;
}
public void setDeletedItems(ArrayList<BasicListItem> deletedItems) {
	this.deletedItems = deletedItems;
}
public ArrayList<BasicListItem> getEditedItems() {
	return editedItems;
}
public void setEditedItems(ArrayList<BasicListItem> editedItems) {
	this.editedItems = editedItems;
}
public ArrayList<BasicListItem> getAddedItems() {
	return addedItems;
}
public void setAddedItems(ArrayList<BasicListItem> addedItems) {
	this.addedItems = addedItems;
}
}
