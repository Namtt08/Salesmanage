package org.project.manage.controller;

import org.project.manage.response.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {

	
	@PostMapping("/a")
	public ResponseEntity<?> registerUser2() {
		
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}
}
