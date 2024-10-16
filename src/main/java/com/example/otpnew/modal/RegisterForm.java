 package com.example.otpnew.modal;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
 
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="oneregnew")
public class RegisterForm {
@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
@Column(name="regid")
private int id;
 
@NotBlank(message="FirstName is Required")
@Size(min=3,max=15,message="FirstName must be between 3 to 15 characters")
@Pattern(regexp = "^[A-Za-zà-ÿÀ-ÿ]+( [A-Za-zà-ÿÀ-ÿ]+)*$", 
message = "FirstName must contain only letters and spaces")
@Column(name="FirstName")
private String fname;

 
@NotBlank(message="LastName is Required")
@Size(min=1,max=15, message="LastName must be between 1 to 15 characters")
@Pattern(regexp = "^[A-Za-zà-ÿÀ-ÿ]+( [A-Za-zà-ÿÀ-ÿ]+)*$", 
message = "LastName must contain only letters and spaces")
@Column(name="Lastname")
private String lname;

 
@NotBlank(message="Gender is Required")
@Column(name="Gender")
private String gender;

 
@NotNull(message="Date of Birth is Required")
@DateTimeFormat(pattern = "yyyy-MM-dd")  
@Column(name="DateofBirth" ,nullable=false)

private LocalDate dob;

@NotBlank(message="Email is Required")
@Email(message="Invalid email format")
 
@Column(name="EmailId")
private String emailid;
@Column(name="verificationcode")
private String  verificationcode;
@Column(name="Enabled")
private boolean enabled;
 
 @NotBlank(message="Mobile Number is Required")
 
@Column(name="PhoneNumber")
private String phone;

 
@NotBlank(message="Education Qualification is Required")
@Size(min = 2, max = 15, message = "Qualification is Invalid")
@Pattern(regexp = "^[A-Za-z\\s]+$", message = "Education Qualification can only contain letters and spaces")
@Column(name="Education")
private String education;

@NotBlank(message="Address is Required")
@Size(min = 2, max = 100, message = "Address is Invalid")
@Pattern(regexp = "^[A-Za-z0-9\\s,/-]+$", message = "Address can only contain letters, numbers, spaces, commas, slashes, and hyphens") 
@Column(name="Address")
private String address;
@NotNull(message="Select any one")
@Column(name="Hobbies")
private String hobbies;


 
@Column(name = "Photo")  

 
private String photo;


 
@Transient 
private MultipartFile photom;
 
 
 
public String getPhoto() {
	return photo;
}
public void setPhoto(String photo) {
	this.photo = photo;
}
@ManyToOne
@JoinColumn(name="Country_Id")
@NotNull(message="Country is Required")
private Country country;

@ManyToOne
@JoinColumn(name="State_Id")
@NotNull(message="State is Required")
private State state;

@ManyToOne
@JoinColumn(name="City_Id")
@NotNull(message="City is Required")
private City city;
 


public MultipartFile getPhotom() {
    return photom;
}

public void setPhoto(MultipartFile photom) {
    this.photom = photom;
}

public Country getCountry() {
	return country;
}
public void setCountry(Country country) {
	this.country = country;
}
public State getState() {
	return state;
}
public void setState(State state) {
	this.state = state;
}
public City getCity() {
	return city;
}
public void setCity(City city) {
	this.city = city;
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getFname() {
	return fname;
}
public void setFname(String fname) {
	this.fname = fname;
}
public String getLname() {
	return lname;
}
public void setLname(String lname) {
	this.lname = lname;
}
public String getGender() {
	return gender;
}
public void setGender(String gender) {
	this.gender = gender;
}
 
public LocalDate getDob() {
	return dob;
}
public void setDob(LocalDate dob) {
	this.dob = dob;
}
public String getPhone() {
	return phone;
}
public void setPhone(String phone) {
	this.phone = phone;
}
public String getEducation() {
	return education;
}
public void setEducation(String education) {
	this.education = education;
}
public String getAddress() {
	return address;
}
public void setAddress(String address) {
	this.address = address;
}
public String getHobbies() {
	return hobbies;
}
public void setHobbies(String hobbies) {
	this.hobbies = hobbies;
}
 
 
 
public String getEmailid() {
	return emailid;
}
public void setEmailid(String emailid) {
	this.emailid = emailid;
}
public String getVerificationcode() {
	return verificationcode;
}
public void setVerificationcode(String verificationcode) {
	this.verificationcode = verificationcode;
}
public boolean isEnabled() {
	return enabled;
}
public void setEnabled(boolean enabled) {
	this.enabled = enabled;
}
 
}
