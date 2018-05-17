package com.springbootactiviti.demo.dao;

import com.springbootactiviti.demo.vo.loan.LoanBill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanBillRepository extends JpaRepository<LoanBill, Long> {
}
