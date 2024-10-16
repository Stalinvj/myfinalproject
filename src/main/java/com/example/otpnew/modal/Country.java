package com.example.otpnew.modal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="Country")
public class Country {
@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
@Column(name="Country_Id")
private int countryid;
@Column(name="Country_Name")
 
private String countryname;
 
public int getCountryid() {
	return countryid;
}
public void setCountryid(int countryid) {
	this.countryid = countryid;
}
public String getCountryname() {
	return countryname;
}
public void setCountryname(String countryname) {
	this.countryname = countryname;
}
 
 
}



