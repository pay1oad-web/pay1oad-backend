package com.pay1oad.homepage.persistence.login;

import com.pay1oad.homepage.model.login.JwtList;

import org.springframework.data.repository.CrudRepository;

public interface JwtRedisRepository extends CrudRepository<JwtList, String> {
    JwtList findByUsername(String username);

    JwtList findByJwt(String jwt);

    void deleteByUsername(String username);
}
