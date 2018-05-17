package com.springbootactiviti.demo;

import com.springbootactiviti.demo.dao.LoanBillRepository;
import com.springbootactiviti.demo.vo.FunderCompany;
import com.springbootactiviti.demo.vo.User;
import com.springbootactiviti.demo.vo.loan.LoanBill;
import org.activiti.engine.*;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Demo_9_Test {
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private LoanBillRepository loanBillRepository;


    @Test
    public void deleteProcessDefi(){
        //通过部署id来删除流程定义
        String deploymentId="45001";
        processEngine.getRepositoryService().deleteDeployment(deploymentId);
    }

    @Test
    public void deploy() {
        Deployment deploy = repositoryService.createDeployment()//创建一个部署的构建器
                .addClasspathResource("processes/demo9.bpmn")   //从类路径中添加资源,一次只能添加一个资源
                .name("借款申请流程demo9")                        //设置部署的名称
                .category("借款/还款类别demo9")                   //设置部署的类别
                .deploy();

        System.out.println("部署的id： "+deploy.getId());
        System.out.println("部署的名称： "+deploy.getName());
        System.out.println("部署的类别： "+deploy.getCategory());
        /*
        部署的id： 1
        部署的名称： 借款申请流程demo9
        部署的类别： 借款/还款类别demo9
        * */
    }

    /**
     *  启动流程示例
     * */
    @Test
    public void startProcessInstance() {
        // 流程定义的key
        String processDefinitionKey = "demo9_process";
        ProcessInstance pi = processEngine.getRuntimeService()
                .startProcessInstanceByKey(processDefinitionKey);

        System.out.println("流程实例ID:" + pi.getId());
        System.out.println("流程定义ID:" + pi.getProcessDefinitionId());
        System.out.println("流程定义key:" + pi.getProcessDefinitionKey());
        System.out.println("流程定义name:" + pi.getName());
        /*
            流程实例ID:5001
            流程定义ID:demo9_process:1:4
            流程定义key:demo9_process
            流程定义name:null
        * */
        //申请借款
        LoanBill loanBill = new LoanBill();
        {
            loanBill.setFunderCompany(new FunderCompany(1L));
            loanBill.setActivitiProcessInstanceId(pi.getProcessInstanceId());
            loanBill.setMoney(10.0);
            loanBill.setUser(new User(1L));
        }
        loanBillRepository.save(loanBill);
        apply(loanBill);
    }


    private void apply(LoanBill loanBill) {
        Task task=taskService.createTaskQuery()
                .processInstanceId(loanBill.getActivitiProcessInstanceId())
                .singleResult();

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("loanBill", loanBill);

        processEngine.getTaskService()
                .complete(task.getId(), variables);

        System.out.println("任务ID：" + task.getId());
        System.out.println("任务名称： " + task.getName());
        /*
            任务ID：5004
            任务名称： apply
        * */

        //System.out.println("funderCompanyName = " + appayBillBean.getFunderCompany().getName());

    }

    //submitted
    @Test
    public void submitted(DelegateExecution execution) {
        String processInstanceId = "";
        Task task=taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("status", "false");
        processEngine.getTaskService()
                .complete(task.getId(), variables);
        System.out.println("借款审核：任务ID：" + task.getId()+"; "+task.getName());
    }

    @Test
    public void test3() {
        String userId = "7";
        List<Task> list = processEngine.getTaskService()
                .createTaskQuery()
                .taskCandidateUser(userId)
                .list();
        System.out.println(list.size());
        list.forEach(task -> {
            System.out.println("任务名称 = " + task.getName());
            System.out.println("任务id = " + task.getId());
        });
    }
}
