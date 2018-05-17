package com.springbootactiviti.demo.service.borrow;

import com.springbootactiviti.demo.dao.UserRepository;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BorrowRefusedService implements TaskListener {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void notify(DelegateTask delegateTask) {
        Set<String> userIds = new HashSet<>();
        {
            userRepository.findByRoleIn(Arrays.asList(new String[]{"staff","manager"})).forEach(user -> {
                userIds.add(String.valueOf(user.getId().longValue()));
            });
        }
        delegateTask.addCandidateUsers(userIds);
    }
}
