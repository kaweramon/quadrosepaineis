package com.quadrosepaineisapi.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quadrosepaineisapi.model.User;
import com.quadrosepaineisapi.repository.UserRepository;
import com.quadrosepaineisapi.util.UrlConstants;

@RequestMapping(path = UrlConstants.URL_USERS)
@RestController
public class UserResource {

	@Autowired
	private UserRepository repo;
	
	@GetMapping(UrlConstants.PARAM_USERNAME)
	public ResponseEntity<User> getByUserName(@PathVariable("username") String username) {
		return ResponseEntity.ok(repo.findByName(username).get());
	}
	
}
