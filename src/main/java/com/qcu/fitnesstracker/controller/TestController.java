package com.qcu.fitnesstracker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/boink/test")
public class TestController {

	@GetMapping("/halimaw")
	public ResponseEntity<String> testEndpoint() {
		return ResponseEntity.ok("fuck! this is halimaw");
	}

}