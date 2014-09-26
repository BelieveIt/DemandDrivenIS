package model;

import java.io.Serializable;

public class CommercialEnvironment implements Serializable{
private static final long serialVersionUID = -5344399772666197593L;
private String environmentId;
private String name;
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getEnvironmentId() {
	return environmentId;
}
public void setEnvironmentId(String environmentId) {
	this.environmentId = environmentId;
}
}
