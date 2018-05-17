package com.springbootactiviti.demo.vo.loan;

import javax.persistence.*;

@Entity
@Table(name = "replace_bill")
public class ReplaceBill {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private LoanBill loanBill;

    private Double replaceMoney;
}
