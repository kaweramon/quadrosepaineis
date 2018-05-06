package com.quadrosepaineisapi.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import lombok.Data;

@Data
@Entity(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	private String email;
	private String password;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_permissions", joinColumns = 
			{@JoinColumn(name = "id_user", referencedColumnName = "id")}, inverseJoinColumns = 
			{@JoinColumn(name = "id_permission", referencedColumnName = "id")})
	private List<Permission> permissions;
	
}