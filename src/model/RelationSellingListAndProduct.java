package model;

import java.io.Serializable;

public class RelationSellingListAndProduct implements Serializable{
private static final long serialVersionUID = -7472164715685016430L;
private String listId;
private String productId;
public String getListId() {
	return listId;
}
public void setListId(String listId) {
	this.listId = listId;
}
public String getProductId() {
	return productId;
}
public void setProductId(String productId) {
	this.productId = productId;
}
}
