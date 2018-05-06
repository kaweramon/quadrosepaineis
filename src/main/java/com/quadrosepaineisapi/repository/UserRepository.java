package com.quadrosepaineisapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quadrosepaineisapi.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	public Optional<User> findByName(String name);
	
	public Optional<User> findByEmail(String email);
	
}
