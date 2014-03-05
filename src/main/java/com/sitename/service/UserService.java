package com.sitename.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sitename.domain.User;
import com.sitename.repository.UserRepository;
import com.sitename.util.DateUtil;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findOne(id);
    }

    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }
    
    public User findBySignature(String signature) {
        return userRepository.findBySignatureAndSignatureExipryIsAfter(signature, DateUtil.getCurrentDateInIST());
    }
    
    public User save(User user) {
        return userRepository.save(user);
    }
}
