package com.springbootactiviti.demo;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
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
public class ExclusiveGateWayTest {
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ProcessEngine processEngine;

    /** 部署流程定义（从inputStream） */
    @Test
    public void deploymentProcessDefinition_inputStream() {
        //根据bpmn文件部署流程
        Deployment deployment = repositoryService.createDeployment().addClasspathResource("processes/demo3.bpmn").deploy();
        //获取流程定义
        /*ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        //启动流程定义，返回流程实例
        ProcessInstance pi = runtimeService.startProcessInstanceById(processDefinition.getId());*/
        System.out.println("部署ID：" + deployment.getId());//42501
        System.out.println("部署名称：" + deployment.getName());//
    }

    /** 启动流程实例 */
    @Test
    public void startProcessInstance() {
        // 流程定义的key
        String processDefinitionKey = "process";
        ProcessInstance pi = processEngine.getRuntimeService()// 与正在执行的流程实例和执行对象相关的Service
                .startProcessInstanceByKey(processDefinitionKey);// 使用流程定义的key启动流程实例，key对应helloworld.bpmn文件中id的属性值，使用key值启动，默认是按照最新版本的流程定义启动
        System.out.println("流程实例ID:" + pi.getId());// 流程实例ID 45001
        System.out.println("流程定义ID:" + pi.getProcessDefinitionId());// 流程定义ID  process:2:42504
                                                                    // helloworld:1:4
    }

    /** 查询当前人的个人任务 */
    @Test
    public void findMyPersonalTask() {
        String assignee = "admin";
        List<Task> list = processEngine.getTaskService()// 与正在执行的任务管理相关的Service
                .createTaskQuery()// 创建任务查询对象
                /** 查询条件（where部分） */
                .taskAssignee(assignee)// 指定个人任务查询，指定办理人
                // .taskCandidateUser(candidateUser)//组任务的办理人查询
                //.processDefinitionId(processDefinitionId)//使用流程定义ID查询
                //.processDefinitionId("process:2:42504")
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
                System.out.println("########################################################");
            }
        }
    }

    /** 完成我的任务 */
    @Test
    public void completeMyPersonalTask() {

        // 任务ID
        Task task=taskService.createTaskQuery().processInstanceId("47501").singleResult();
        // 完成任务的同时，设置流程变量，使用流程变量用来指定完成任务后，下一个连线，对应exclusiveGateWay.bpmn文件中${money>1000}
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("money", 200);
        processEngine.getTaskService()// 与正在执行的任务管理相关的Service
                .complete(task.getId(), variables);
        System.out.println("完成任务：任务ID：" + task.getId()+"; "+task.getName());//任务ID：47504; 部门

        /*
        *
        *
        * task = taskService.createTaskQuery().processInstanceId(processId).singleResult();
        System.out.println("第二次执行前，任务名称："+task.getName());
        taskService.complete(task.getId());
        * */
    }

    /*
    * 执行下一步任务
    * */
    @Test
    public void test2() {
        ProcessInstance pi = processEngine.getRuntimeService()// 与正在执行的流程实例和执行对象相关的Service
                .startProcessInstanceByKey("process");
        String processId = pi.getId();
        // 任务ID
        Task task=taskService.createTaskQuery().processInstanceId(processId).singleResult();

        taskService.complete(task.getId());
        System.out.println("第二次执行前，任务名称："+task.getName());
        System.out.println("task.toString() = " + task.toString());
    }
}