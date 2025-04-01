package com.qcu.fitnesstracker.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class AuthSuccessController {

	@GetMapping("/auth/success")
	@ResponseBody
	public String loginSuccess() {
		return "Logged in successfully!";
	}
}
