package com.sitename.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sitename.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserName(String userName);
    
    User findBySignatureAndSignatureExipryIsAfter(String signature, Date date);
}
