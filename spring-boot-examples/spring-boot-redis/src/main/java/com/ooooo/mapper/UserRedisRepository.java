package com.ooooo.mapper;

import com.ooooo.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author leizhijie
 * @since 2021/4/12 21:48
 */
@Repository
public interface UserRedisRepository extends CrudRepository<User, Long> {
}
