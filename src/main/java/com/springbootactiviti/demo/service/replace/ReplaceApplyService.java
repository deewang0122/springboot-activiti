package com.springbootactiviti.demo.service.replace;

import com.springbootactiviti.demo.vo.loan.LoanBill;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class ReplaceApplyService implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {

        delegateTask.getProcessInstanceId();

        LoanBill loanBill = delegateTask.getVariable("loanBill", LoanBill.class);

        delegateTask.setAssignee(String.valueOf(loanBill.getUser().getId()));
    }
}
