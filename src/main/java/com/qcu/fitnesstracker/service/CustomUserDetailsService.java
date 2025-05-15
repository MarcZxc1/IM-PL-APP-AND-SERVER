package com.qcu.fitnesstracker.service;

import com.qcu.fitnesstracker.model.GoogleUser;
import com.qcu.fitnesstracker.model.User;
import com.qcu.fitnesstracker.repository.GoogleUserRepository;
import com.qcu.fitnesstracker.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final GoogleUserRepository googleUserRepository;
	private final UserRepository userRepository;

	public CustomUserDetailsService(GoogleUserRepository googleUserRepository, UserRepository userRepository) {
		this.googleUserRepository = googleUserRepository;
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// First try to find a regular user
		User regularUser = userRepository.findByEmail(email);
		if (regularUser != null) {
			return new org.springframework.security.core.userdetails.User(
					regularUser.getEmail(),
					regularUser.getPassword(),
					Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
			);
		}

		// If not found, try to find a Google user
		GoogleUser googleUser = googleUserRepository.findByEmail(email);
		if (googleUser != null) {
			return new org.springframework.security.core.userdetails.User(
					googleUser.getEmail(),
					googleUser.getPassword(),
					Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
			);
		}

		throw new UsernameNotFoundException("User not found with email: " + email);
	}
}