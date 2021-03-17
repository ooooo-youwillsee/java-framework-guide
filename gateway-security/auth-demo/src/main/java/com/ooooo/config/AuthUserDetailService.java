package com.ooooo.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
	
	@Autowired(required = false)
	private JdbcTemplate jdbcTemplate;
	private boolean inited = false;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		init();
		for (UserDetails userDetail : userDetails) {
			if (userDetail.getUsername().equals(username)) {
				return userDetail;
			}
		}
		return null;
	}
	
	private synchronized void init() {
		if (inited) return;
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		if (jdbcTemplate == null) {
			userDetails.add(new AuthUserDetail("user", passwordEncoder.encode("password"), Collections.singletonList("ROLE_USER"), true));
			userDetails.add(new AuthUserDetail("admin", "{noop}password", Collections.singletonList("ROLE_ADMIN"), true));
		} else {
			List<Map<String, Object>> res = jdbcTemplate.queryForList("select username, password, roles, enabled  from oauth_user_details");
			res.forEach(m -> {
				String username = MapUtils.getString(m, "username");
				String password = MapUtils.getString(m, "password");
				List<String> roles = Arrays.stream(MapUtils.getString(m, "roles").split(",")).collect(Collectors.toList());
				boolean enabled = "1".equals(MapUtils.getString(m, "enabled"));
				AuthUserDetail userDetail = new AuthUserDetail(username, password, roles, enabled);
				userDetails.add(userDetail);
			});
		}
		inited = true;
	}
}
