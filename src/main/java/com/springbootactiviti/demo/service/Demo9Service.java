package com.springbootactiviti.demo.service;

import com.springbootactiviti.demo.dao.UserRepository;
import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class Demo9Service {

    @Autowired
    private UserRepository userRepository;

    public List<String> findUsersForApply(DelegateExecution execution){
        List<String> userIds = new ArrayList<>();
        userRepository.findByRole("debtor").forEach(user -> {
            userIds.add(String.valueOf(user.getId()));
        });

        return userIds;
    }

    public List<String> findUsersForSubmitted(DelegateExecution execution){
        List<String> userIds = new ArrayList<>();
        userRepository.findByRole("manager").forEach(user -> {
            userIds.add(String.valueOf(user.getId()));
        });

        return userIds;
    }

    public List<String> findUsersForConfirmed(DelegateExecution execution){
        List<String> userIds = new ArrayList<>();
        userRepository.findByRole("boss").forEach(user -> {
            userIds.add(String.valueOf(user.getId()));
        });

        return userIds;
    }

    public List<String> findUsersForRefused(DelegateExecution execution){
        List<String> userIds = new ArrayList<>();
        userRepository.findByRoleIn(Arrays.asList("boss", "manager")).forEach(user -> {
            userIds.add(String.valueOf(user.getId()));
        });

        return userIds;
    }

    public List<String> findUsersForClosed(DelegateExecution execution){
        List<String> userIds = new ArrayList<>();
        userRepository.findByRoleIn(Arrays.asList("boss", "manager")).forEach(user -> {
            userIds.add(String.valueOf(user.getId()));
        });

        return userIds;
    }
}
