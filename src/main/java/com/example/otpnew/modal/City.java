package com.example.otpnew.modal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="City")
public class City {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="City_Id")
	private int cityid;
	@Column(name="City_Name")
	 
	private String cityname;
	@ManyToOne
	@JoinColumn(name="State_Id")
	private State state;
	public int getCityid() {
		return cityid;
	}
	public void setCityid(int cityid) {
		this.cityid = cityid;
	}
	 
	public String getCityname() {
		return cityname;
	}
	public void setCityname(String cityname) {
		this.cityname = cityname;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	

}
 


