package com.qcu.fitnesstracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ErrorController {
	@GetMapping("/error")
	@ResponseBody
	public String handleError() {
		return "Custom error page: Something went wrong!";
	}
}