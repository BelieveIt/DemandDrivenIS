package model;

import java.util.ArrayList;

public class ProductType {
	private String productTypeId;
	private String productTypeName;
	private ArrayList<String>  additionalInformationLable;
	public String getProductTypeId() {
		return productTypeId;
	}
	public void setProductTypeId(String productTypeId) {
		this.productTypeId = productTypeId;
	}

	public ArrayList<String> getAdditionalInformationLable() {
		return additionalInformationLable;
	}
	public void setAdditionalInformationLable(
			ArrayList<String> additionalInformationLable) {
		this.additionalInformationLable = additionalInformationLable;
	}
	public String getProductTypeName() {
		return productTypeName;
	}
	public void setProductTypeName(String productTypeName) {
		this.productTypeName = productTypeName;
	}
}
