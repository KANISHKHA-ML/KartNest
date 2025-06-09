package com.cts.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cts.entity.User;
import com.cts.repository.UserRepo;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepo userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> userOptional = userRepository.findByUsername(username);
		User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
		
		List<GrantedAuthority> authorities = new ArrayList<>();
		if(user.getRole() != null && !user.getRole().isEmpty()) {
			authorities.add(new SimpleGrantedAuthority(user.getRole()));
		}
		else
		{
			authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		}
		
		return new CustomUserDetails( 
				user.getUserId(),user.getUsername(),user.getPassword(),authorities
				);
		



	}

}
