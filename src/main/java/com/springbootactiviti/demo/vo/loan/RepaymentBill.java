package com.springbootactiviti.demo.vo.loan;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "repayment_bill")
@Data
public class RepaymentBill {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private LoanBill loanBill;

    private Double repayMoney;
}
