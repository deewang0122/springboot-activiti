package com.springbootactiviti.demo.controller;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "activiti")
public class DemoActivitiController {
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ProcessEngine processEngine;

    @GetMapping(value = "demo")
    public void demo() {

        //根据bpmn文件部署流程
        Deployment deployment = repositoryService.createDeployment().addClasspathResource("processes/demo2.bpmn").deploy();
        //获取流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        //启动流程定义，返回流程实例
        ProcessInstance pi = runtimeService.startProcessInstanceById(processDefinition.getId());
        String processId = pi.getId();
        System.out.println("流程创建成功，当前流程实例ID："+processId);

        Task task=taskService.createTaskQuery().processInstanceId(processId).singleResult();
        System.out.println("第一次执行前，任务名称："+task.getName());
        taskService.complete(task.getId());

        task = taskService.createTaskQuery().processInstanceId(processId).singleResult();
        System.out.println("第二次执行前，任务名称："+task.getName());
        taskService.complete(task.getId());
/*
        task = taskService.createTaskQuery().processInstanceId(processId).singleResult();
        System.out.println("第三次执行前，任务执行完毕："+task.getName());*/
    }

    @GetMapping(value = "find")
    public List<Task> findTasksByUserId(String userId) {

        List<Task> resultTask = taskService.createTaskQuery()
                .processDefinitionKey("demo_2_Process_1")
                .taskCandidateOrAssigned(userId)
                .list();

        return resultTask;
    }

    @GetMapping(value = "findtest")
    public void  test2(){
        TaskQuery query = processEngine.getTaskService().createTaskQuery();
        String assignee = "李四" ;
        query.taskAssignee(assignee) ;
        query.list().forEach(s-> System.out.println(s.getId()+"-----------\n"+s.getName()));
    }

}
