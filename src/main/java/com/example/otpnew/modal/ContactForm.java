package com.example.otpnew.modal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class ContactForm {
@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
private int id;

@NotBlank(message="Email is Required")
@Email(message="Invalid email format")
@Column(name="Email")
private String email;

@NotBlank(message="Name is Required")
@Size(min=3,max=15,message="Name must be between 3 to 15 characters")
@Pattern(regexp = "^[A-Za-zà-ÿÀ-ÿ ]+$", message = "Name must contain only letters and spaces")

@Column(name="Name")
private String name;

@NotBlank(message="Subject is Required")
@Size(min = 2, max = 100, message = "Subject must be between 2 to 50 characters long.")
@Pattern(regexp = "^[A-Za-zà-ÿÀ-ÿ ]+$", message = "Subject must contain only letters and spaces")
@Column(name="Subject")
private String sub;



@NotBlank(message="Message is Required")
@Size(min = 2, max = 100, message = "Message must be between 2 to 100 characters long.")
@Pattern(regexp = "^[A-Za-zà-ÿÀ-ÿ0-9 .,;:!?-]+$", message = "Message must contain only letters, numbers, spaces, and some punctuation")

@Column(name="Message")
private String msg ;

public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}

public String getEmail() {
	return email;
}

public void setEmail(String email) {
	this.email = email;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public String getSub() {
	return sub;
}

public void setSub(String sub) {
	this.sub = sub;
}

public String getMsg() {
	return msg;
}

public void setMsg(String msg) {
	this.msg = msg;
}


}
