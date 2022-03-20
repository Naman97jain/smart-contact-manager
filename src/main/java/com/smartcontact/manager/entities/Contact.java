package com.smartcontact.manager.entities;

import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Contact {
	
	@Id
	@GeneratedValue
	@Type(type="uuid-char")
	private UUID cId;
	private String phone;
	private String name;
	private String nickname;
	private String email;
	private String work;
	private String imageUrl;
	
	@Column(length=5000)
	private String description;
	
	@ManyToOne
	@JsonIgnore
	private User user;

	public UUID getcId() {
		return cId;
	}

	public void setcId(UUID cId) {
		this.cId = cId;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public User getUser() {
		return user;
	}
	
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Contact [cId=" + cId + ", phone=" + phone + ", name=" + name + ", nickname=" + nickname + ", email="
				+ email + ", work=" + work + ", imageUrl=" + imageUrl + ", description=" + description + "]";
	}

	public Contact() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
