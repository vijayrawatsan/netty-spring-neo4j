package com.sitename.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sitename.domain.User;



public interface UserRepository extends JpaRepository<User, Long> {

}
