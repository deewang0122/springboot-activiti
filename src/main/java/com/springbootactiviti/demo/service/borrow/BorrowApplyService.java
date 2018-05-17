package com.springbootactiviti.demo.service.borrow;

import com.springbootactiviti.demo.service.UserService;
import com.springbootactiviti.demo.vo.User;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BorrowApplyService implements TaskListener {

    @Autowired
    private UserService userService;

    public List<User> test() {
        return userService.findByRole("debtor");
    }

    @Override
    public void notify(DelegateTask delegateTask) {
        List<User> users = userService.findByRole("debtor");
        Set<String> userIds = new HashSet<>();
        userIds.add("1");
        userIds.add("2");
        userIds.add("6");
        delegateTask.addCandidateUsers(userIds);
    }
}
