package org.project.manage.controller;

import org.project.manage.response.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

	
	@PostMapping("/a")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> test() {
		return ResponseEntity.ok(new MessageResponse(200,"User registered successfully!"));
	}
}
