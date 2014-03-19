package com.sitename.data.service;

import java.util.List;

import com.sitename.data.domain.User;
import com.sitename.data.repository.UserRepository;

public class UserService extends BaseService {

    private static final UserService SINGLETON = new UserService();
    
    private UserRepository userRepository;
    
    private UserService() {
        this.userRepository = BaseService.dataContext.getBean(UserRepository.class);
    }

    @SuppressWarnings("unchecked")
    public List<User> findAll() {
        return (List<User>) userRepository.findAll().as(List.class);
    }

    public User findById(Long id) {
        return userRepository.findOne(id);
    }

    public User save(User user) {
        return userRepository.save(user);
    }
    
    public static UserService getInstance() {
        return SINGLETON;
    }
    
}
