package com.springbootactiviti.demo.service;

import com.springbootactiviti.demo.dao.UserRepository;
import com.springbootactiviti.demo.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> findByRole(String role) {

        return userRepository.findByRole(role);
    }

    public List<User> findByRoleIn(List<String> roles) {
        return findByRoleIn(roles);
    }
}
