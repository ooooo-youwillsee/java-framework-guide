package com.ooooo.config;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author leizhijie
 * @since 2021/3/15 14:17
 */
@Service
public class AuthUserDetailService implements UserDetailsService {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Map<String, Object> m = jdbcTemplate.queryForObject("select username, password, roles, enabled  from oauth_user_details t where t.username = ?", new Object[]{username}, new ColumnMapRowMapper());
		if (MapUtils.isEmpty(m)) {
			return null;
		}
		String password = MapUtils.getString(m, "password");
		List<String> roles = Arrays.stream(MapUtils.getString(m, "roles").split(",")).collect(Collectors.toList());
		boolean enabled = "1".equals(MapUtils.getString(m, "enabled"));
		AuthUserDetail userDetail = new AuthUserDetail(username, password, roles, enabled);
		return userDetail;
	}
}
