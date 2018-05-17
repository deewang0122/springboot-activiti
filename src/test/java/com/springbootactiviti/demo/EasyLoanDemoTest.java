package com.springbootactiviti.demo;

import com.springbootactiviti.demo.dao.UserRepository;
import com.springbootactiviti.demo.service.borrow.BorrowApplyService;
import com.springbootactiviti.demo.vo.FunderCompany;
import com.springbootactiviti.demo.vo.User;
import com.springbootactiviti.demo.vo.loan.LoanBill;
import org.activiti.engine.*;
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
public class EasyLoanDemoTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BorrowApplyService borrowApplyService;
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


    @Test
    public void deleteProcessDefi(){
        //通过部署id来删除流程定义
        String deploymentId="42501";
        processEngine.getRepositoryService().deleteDeployment(deploymentId);
    }


    /**
    *   部署流程
    * */
    @Test
    public void deploy1() {

        //根据bpmn文件部署流程
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("processes/demo8.bpmn")
                .deploy();

        System.out.println("deploy id : "
                + deployment.getId()    //42501
                +" deploy name : "+ deployment.getName()  //车商借款流程
                +" deploy category : "+ deployment.getCategory());    //融资类别
    }

    @Test
    public void deploy() {

        //获取仓库服务 ：管理流程定义
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deploy = repositoryService.createDeployment()//创建一个部署的构建器
                .addClasspathResource("processes/demo8.bpmn")//从类路径中添加资源,一次只能添加一个资源
                .name("借款申请流程")         //设置部署的名称
                .category("借款/还款类别")        //设置部署的类别
                .deploy();

        System.out.println("部署的id： "+deploy.getId());
        System.out.println("部署的名称： "+deploy.getName());
    }

    /**
     *  启动流程示例
     * */
    @Test
    public void startProcessInstance() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        // 流程定义的key
        String processDefinitionKey = "easy_loan_v3";
        ProcessInstance pi = processEngine.getRuntimeService()// 与正在执行的流程实例和执行对象相关的Service
                                .startProcessInstanceByKey(processDefinitionKey);// 使用流程定义的key启动流程实例

        System.out.println("流程实例ID:" + pi.getId());// 流程实例ID:    10001
        System.out.println("流程定义ID:" + pi.getProcessDefinitionId());// 流程定义ID:  easy_loan_v3:1:7504
        System.out.println("流程定义key:" + pi.getProcessDefinitionKey());//easy_loan_v3
        System.out.println("流程定义name:" + pi.getName());

        //申请借款
        LoanBill loanBill = new LoanBill();
        {
            loanBill.setFunderCompany(new FunderCompany(1L));
            loanBill.setActivitiProcessInstanceId(pi.getProcessInstanceId());
            loanBill.setMoney(10.0);
            loanBill.setUser(new User(1L));
        }

        borrowApply(loanBill);

    }


    private void borrowApply(LoanBill loanBill) {
        Task task=taskService.createTaskQuery()
                .processInstanceId(loanBill.getActivitiProcessInstanceId())
                .singleResult();
        Map<String, Object> variables = new HashMap<String, Object>();

        variables.put("loanBill", loanBill);

        processEngine.getTaskService()
                .complete(task.getId());

        System.out.println("受理借款任务：任务ID：" + task.getId()+"; "+task.getName());
    }

    //submitted
    @Test
    public void borrowSubmitted() {
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
        String userId = "1";
        List<Task> list = processEngine.getTaskService()
                .createTaskQuery()
                .taskCandidateUser(userId)
                .list();

        list.forEach(task -> {
            System.out.println("task.getName() = " + task.getName());
            System.out.println("task.getId() = " + task.getId());
        });
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
                //.taskAssignee(assignee)// 指定个人任务查询，指定办理人
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

   @Test
    public void test12() {
        List<User> users = borrowApplyService.test();
        System.out.println(users.size());
   }
}
