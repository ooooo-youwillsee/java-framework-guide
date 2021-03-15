package com.ooooo.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author leizhijie
 * @since 2021/3/15 14:17
 */
@Service
public class AuthUserDetailService implements UserDetailsService {
	
	private static final List<UserDetails> userDetails = new ArrayList<>();
	
	public AuthUserDetailService() {
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		userDetails.add(new AuthUserDetail("user", passwordEncoder.encode("password"), Collections.singletonList("ROLE_USER"), true));
		userDetails.add(new AuthUserDetail("admin", "{noop}password", Collections.singletonList("ROLE_ADMIN"), true));
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		for (UserDetails userDetail : userDetails) {
			if (userDetail.getUsername().equals(username)) {
				return userDetail;
			}
		}
		return null;
	}
}
