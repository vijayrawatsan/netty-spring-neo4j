package com.sitename.repository;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.sitename.domain.User;

public interface UserRepository extends GraphRepository<User> {

}
