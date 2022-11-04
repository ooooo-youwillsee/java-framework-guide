package com.ooooo.config;

import com.ooooo.constant.ProfileConstants;
import org.redisson.api.RedissonClient;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 2021/4/13 20:42
 */
@Configuration
@Profile(ProfileConstants.PROFILE_REDISSON)
public class RedissonConfig {

  @Bean
  public RedissonConnectionFactory redissonConnectionFactory(RedissonClient redisson) {
    return new RedissonConnectionFactory(redisson);
  }
}
