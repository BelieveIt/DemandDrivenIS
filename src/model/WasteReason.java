package model;

import java.io.Serializable;

public class WasteReason implements Serializable{
private static final long serialVersionUID = -6389380388263513444L;
private String reasonId;
private String reasonDescription;
public String getReasonId() {
	return reasonId;
}
public void setReasonId(String reasonId) {
	this.reasonId = reasonId;
}
public String getReasonDescription() {
	return reasonDescription;
}
public void setReasonDescription(String reasonDescription) {
	this.reasonDescription = reasonDescription;
}

}
