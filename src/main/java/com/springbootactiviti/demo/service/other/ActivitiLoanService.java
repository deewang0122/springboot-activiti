package com.springbootactiviti.demo.service.other;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.stereotype.Service;

@Service
public class ActivitiLoanService implements TaskListener {
    //借款申请
    public void borrowApplied() {

    }

    public void borrowSubmitted() {

    }

    public void reviewed() {

    }

    public void borrowConfirmed() {

    }

    public void borrowRefused() {

    }
    //换车
    public void replaceApply() {

    }

    public void replaceSubmitted() {

    }
    public void replaceReview() {

    }
    public void replaceConfirmed() {

    }
    public void replaceRefused() {

    }
    //还款
    public void returnApplied() {

    }
    public void returnSubmitted() {

    }
    public void returnConfirmed() {

    }
    public void returnClosed() {

    }
    public void returnCanceled() {

    }
    public void closed() {

    }
    public void canceled() {

    }

    @Override
    public void notify(DelegateTask delegateTask) {

    }
}
