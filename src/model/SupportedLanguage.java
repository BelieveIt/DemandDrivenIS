package model;

import java.io.Serializable;

public class SupportedLanguage implements Serializable{
private static final long serialVersionUID = 1L;
private String languageLabel;
private String languageCode;
public SupportedLanguage(String languageCode, String languageLabel) {
	this.languageLabel = languageLabel;
	this.languageCode = languageCode;
}
public String getLanguageLabel() {
	return languageLabel;
}
public void setLanguageLabel(String languageLabel) {
	this.languageLabel = languageLabel;
}
public String getLanguageCode() {
	return languageCode;
}
public void setLanguageCode(String languageCode) {
	this.languageCode = languageCode;
}
}
