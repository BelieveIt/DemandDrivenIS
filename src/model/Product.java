package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;


import utils.StringUtil;

public class Product implements Serializable{
	private static final long serialVersionUID = -4546717255351419498L;
	private String name;
	private String brand;
	private Integer itemWeight;
	private String manufacturer;
	private String image;
	private BigDecimal price;
	private String unit;
	private String deliveryFrequency;
	private String minInventory;
	private Date productCreateTime;
	private ArrayList<String>  additionalInformation;
	@SuppressWarnings("unused")
	private String  additionalInformationString;
	private ArrayList<AdditionalInfoItem> additionalInfoItems;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public Integer getItemWeight() {
		return itemWeight;
	}
	public void setItemWeight(Integer itemWeight) {
		this.itemWeight = itemWeight;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getDeliveryFrequency() {
		return deliveryFrequency;
	}
	public void setDeliveryFrequency(String deliveryFrequency) {
		this.deliveryFrequency = deliveryFrequency;
	}
	public String getMinInventory() {
		return minInventory;
	}
	public void setMinInventory(String minInventory) {
		this.minInventory = minInventory;
	}
	public Date getProductCreateTime() {
		return productCreateTime;
	}
	public void setProductCreateTime(Date productCreateTime) {
		this.productCreateTime = productCreateTime;
	}
	public ArrayList<String> getAdditionalInformation() {
		return additionalInformation;
	}
	public void setAdditionalInformation(ArrayList<String> additionalInformation) {
		this.additionalInformation = additionalInformation;
	}
	public String getAdditionalInformationString() {
		return StringUtil.getStringByList(additionalInformation);
	}
	public void setAdditionalInformationString(
			String additionalInformationString) {
		this.additionalInformationString = additionalInformationString;
	}
	public ArrayList<AdditionalInfoItem> getAdditionalInfoItems() {
		return additionalInfoItems;
	}
	public void setAdditionalInfoItems(ArrayList<AdditionalInfoItem> additionalInfoItems) {
		this.additionalInfoItems = additionalInfoItems;
	}


}
