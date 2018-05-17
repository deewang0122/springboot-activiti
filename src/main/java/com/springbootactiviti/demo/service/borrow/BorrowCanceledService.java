package com.springbootactiviti.demo.service.borrow;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class BorrowCanceledService implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {

    }
}
