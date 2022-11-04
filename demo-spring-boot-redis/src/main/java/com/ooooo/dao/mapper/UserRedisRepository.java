package com.ooooo.dao.mapper;

import com.ooooo.dao.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 2021/4/12 21:48
 */
@Repository
public interface UserRedisRepository extends CrudRepository<User, Long> {
}
