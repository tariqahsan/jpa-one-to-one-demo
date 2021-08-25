package com.example.jpa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jpa.model.UserProfile;
import com.example.jpa.repository.UserProfileRepository;

@RestController
@RequestMapping(value = "/api/v1")
public class UserProfileController {
	
	@Autowired
	UserProfileRepository userProfileRepository;
	
	@GetMapping("/userProfiles")
	public ResponseEntity<List<UserProfile>> getAllUserProfile(){
		
		List<UserProfile> userProfiles = userProfileRepository.findAll();
		try {
			if(userProfiles.isEmpty()) {
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<List<UserProfile>>(userProfiles, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/add")
	public ResponseEntity<UserProfile> createUserProfile(@RequestBody UserProfile userProfile) {
		
		try {
			UserProfile userProfileData = userProfileRepository.save(userProfile);
			return new ResponseEntity<UserProfile>(userProfileData, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
		}
	}

}
