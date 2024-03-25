package com.smart.smartcontactmanager.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
@Entity
@Table(name="user")
public class UserEntity {
@Id
@GeneratedValue(strategy=GenerationType.AUTO)
private int id;
private String name;
@Column(unique=true)
private String email;
private String password;
private String role;
private boolean status;
private String imageUrl;
@Column(length = 500)
private String about;
@Override
public String toString() {
	return "UserEntity [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", role=" + role
			+ ", status=" + status + ", imageUrl=" + imageUrl + ", about=" + about + ", contacts=" + contacts + "]";
}
@OneToMany(cascade = CascadeType.ALL , fetch=FetchType.LAZY,mappedBy="user")
private List<ContactEntity> contacts=new ArrayList<>();
public List<ContactEntity> getContacts() {
	return contacts;
}
public void setContacts(List<ContactEntity> contacts) {
	this.contacts = contacts;
}
public UserEntity() {
	super();
	// TODO Auto-generated constructor stub
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}
public String getRole() {
	return role;
}
public void setRole(String role) {
	this.role = role;
}
public boolean isStatus() {
	return status;
}
public void setStatus(boolean status) {
	this.status = status;
}
public String getImageUrl() {
	return imageUrl;
}
public void setImageUrl(String imageUrl) {
	this.imageUrl = imageUrl;
}
public String getAbout() {
	return about;
}
public void setAbout(String about) {
	this.about = about;
}


}

