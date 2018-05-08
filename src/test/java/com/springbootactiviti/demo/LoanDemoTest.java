package com.springbootactiviti.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
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
public class LoanDemoTest {
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

    /**
    *   部署流程
    * */
    @Test
    public void deploy() {
        //根据bpmn文件部署流程
        Deployment deployment = repositoryService.createDeployment().addClasspathResource("processes/demo5.xml").deploy();

        System.out.println("deployment.getId()+deployment.getName()+deployment.getCategory() = "
                + deployment.getId()    //1
                + deployment.getName()
                + deployment.getCategory());
    }
    /**
     *  启动流程示例
     * */
    @Test
    public void startProcessInstance() {
        // 流程定义的key
        String processDefinitionKey = "loan_demo_process";
        ProcessInstance pi = processEngine.getRuntimeService()// 与正在执行的流程实例和执行对象相关的Service
                                .startProcessInstanceByKey(processDefinitionKey);// 使用流程定义的key启动流程实例

        System.out.println("流程实例ID:" + pi.getId());// 流程实例ID:    2501，15001
        System.out.println("流程定义ID:" + pi.getProcessDefinitionId());// 流程定义ID:  loan_demo_process:1:4，loan_demo_process:1:4
        System.out.println("流程定义key:" + pi.getProcessDefinitionKey());//loan_demo_process，loan_demo_process
        System.out.println("流程定义name:" + pi.getName());
    }
    /**
    *   执行任务-申请借款
    * */
    @Test
    public void executeTaskApplyLoan() {
        // 任务ID
        String processInstanceId = "15001";
        Task task=taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();

        processEngine.getTaskService()
                .complete(task.getId());
        System.out.println("申请借款任务：任务ID：" + task.getId()+"; "+task.getName());//申请借款任务：任务ID：2504; 申请借款
        //申请借款任务：任务ID：15004; 申请借款
    }
    /**
     * 执行任务-受理借款
    * */
    @Test
    public void executeTaskHearLoan() {
        // 任务ID
        String processInstanceId = "15001";
        Task task=taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("status", "false");
        processEngine.getTaskService()
                .complete(task.getId(), variables);
        System.out.println("受理借款任务：任务ID：" + task.getId()+"; "+task.getName());//受理借款任务：任务ID：5002; 受理借款
        //受理借款任务：任务ID：17502; 受理借款
    }
    /**
     * 执行任务-审核借款
     * */
    @Test
    public void executeTaskApproveLoan() {
        // 任务ID
        String processInstanceId = "2501";
        Task task=taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("status", true);
        processEngine.getTaskService()
                .complete(task.getId(), variables);
        System.out.println("审核借款：任务ID：" + task.getId()+"; "+task.getName());
    }
    /** 拒绝借款 */
    @Test
    public void executeTaskPassLoan() {
        // 任务ID
        String processInstanceId = "15001";
        Task task=taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        processEngine.getTaskService()
                .complete(task.getId());
        System.out.println("拒绝借款：任务ID：" + task.getId()+"; "+task.getName());
    }
    /**
     * 执行任务-放款
     * */
    @Test
    public void executeTaskRejectLoan() {
        // 任务ID
        String processInstanceId = "2501";
        Task task=taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        processEngine.getTaskService()
                .complete(task.getId());
        System.out.println("放款：任务ID：" + task.getId()+"; "+task.getName());
    }

    /**
     * 执行任务-放款
     * */
    @Test
    public void executeTaskFile() {
        // 任务ID
        Task task=taskService.createTaskQuery().processInstanceId("15001").singleResult();
        processEngine.getTaskService()
                .complete(task.getId());
        System.out.println("放款：任务ID：" + task.getId()+";任务名称："+task.getName()+ "; 流程id: "+task.getProcessInstanceId());
    }
    /**
     * 查询当前人的个人任务
     * */
    @Test
    public void findMyPersonalTask() {
        String assignee = "sp_file";
        List<Task> list = processEngine.getTaskService()// 与正在执行的任务管理相关的Service
                .createTaskQuery()// 创建任务查询对象
                /** 查询条件（where部分） */
                .taskAssignee(assignee)// 指定个人任务查询，指定办理人
                // .taskCandidateUser(candidateUser)//组任务的办理人查询
                //.processDefinitionId(processDefinitionId)//使用流程定义ID查询
                //.processDefinitionId("loan_demo_process:1:4")
                // .processInstanceId(processInstanceId)//使用流程实例ID查询
                // .executionId(executionId)//使用执行对象ID查询
                /** 排序 */
                .orderByTaskCreateTime().asc()// 使用创建时间的升序排列
                /** 返回结果集 */
                // .singleResult()//返回惟一结果集
                // .count()//返回结果集的数量
                // .listPage(firstResult, maxResults);//分页查询
                .list();// 返回列表
        if (list != null && list.size() > 0) {
            for (Task task : list) {
                System.out.println("任务ID:" + task.getId());
                System.out.println("任务名称:" + task.getName());
                System.out.println("任务的创建时间:" + task.getCreateTime());
                System.out.println("任务的办理人:" + task.getAssignee());
                System.out.println("流程实例ID：" + task.getProcessInstanceId());
                System.out.println("执行对象ID:" + task.getExecutionId());
                System.out.println("流程定义ID:" + task.getProcessDefinitionId());
            }
        }
        /*
        *
        * (借款审核：debtor)
        * 任务ID:5004
任务名称:申请借款
任务的创建时间:Tue May 08 15:26:35 CST 2018
流程实例ID：2501
执行对象ID:2501
流程定义ID:loan_demo_process:1:4
流程定义ID:loan_demo_process:1:2504
########################################################
(借款审核：sp_approve)
任务ID:7504
任务名称:审核借款
任务的创建时间:Tue May 08 15:45:12 CST 2018
任务的办理人:sp_approve
流程实例ID：2501
执行对象ID:2501
流程定义ID:loan_demo_process:1:4
        * */


    }

    /**
     *  历史流程实例查询
     *  http://blog.csdn.net/luckyzhoustar/article/details/48652783
     */
    @Test
    public void findHistoricProcessInstance() {
        // 查询已完成的流程
        List<HistoricProcessInstance> datas = historyService
                .createHistoricProcessInstanceQuery().finished().list();
        System.out.println("统计完成的流程：（使用finished方法）：" + datas.size());
        datas.forEach(data -> {
            try {
                System.out.println("finished流程" + new ObjectMapper().writeValueAsString(data));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        // 查询未完成的流程
        List<HistoricProcessInstance> unFinishedDatas = historyService
                .createHistoricProcessInstanceQuery().unfinished().list();
        System.out.println("统计未完成的流程：（unFinishedDatas）：" + datas.size());
        unFinishedDatas.forEach(data -> {
            try {
                System.out.println("unFinishedDatas流程" + new ObjectMapper().writeValueAsString(data));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });

        // 根据流程定义ID查询
        datas = historyService.createHistoricProcessInstanceQuery()
                .processDefinitionId("loan_demo_process:1:4").list();

        datas.forEach(data -> {
            try {
                System.out.println(new ObjectMapper().writeValueAsString(data));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });

        //根据processInstanceId查询流程详情
        List<HistoricTaskInstance> datas2 = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId("15001").list();//2501，15001
        datas2.forEach(data -> {
            try {
                System.out.println("datas2 = " + new ObjectMapper().writeValueAsString(data));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }
}
