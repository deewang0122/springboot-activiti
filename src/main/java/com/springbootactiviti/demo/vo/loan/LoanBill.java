package com.springbootactiviti.demo.vo.loan;

import com.springbootactiviti.demo.vo.FunderCompany;
import com.springbootactiviti.demo.vo.User;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "loan_bill")
@Data
public class LoanBill {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private FunderCompany funderCompany;

    private Double money;                       //单位(万元)

    private String activitiProcessInstanceId;     //对应的工作流进程id
}
