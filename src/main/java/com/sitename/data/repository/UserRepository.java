package com.sitename.data.repository;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.sitename.data.domain.User;

public interface UserRepository extends GraphRepository<User> {

}
