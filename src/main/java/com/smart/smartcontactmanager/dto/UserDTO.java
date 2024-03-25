package com.smart.smartcontactmanager.dto;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
public class UserDTO {
	private int id;
	private String name;
	private String email;
	private String password;
	private String role;
	private boolean status;
	private String imageUrl;
	private String about;
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
//	public boolean isStatus() {
//		return status;
//	}
//	public void setStatus(boolean status) {
//		this.status = status;
//	}
//	public void getStatus() {
//		this.status = status;
//	}
	public String getImageUrl() {
		return imageUrl;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
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
	@Override
	public String toString() {
		return "UserDTO [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", role=" + role
				+ ", status=" + status + ", imageUrl=" + imageUrl + ", about=" + about + "]";
	}
	public UserDTO(int id, String name, String email, String password, String role, boolean status, String imageUrl,
			String about) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
		this.status = status;
		this.imageUrl = imageUrl;
		this.about = about;
	}
	public UserDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
