package com.springbootactiviti.demo.dao;

import com.springbootactiviti.demo.vo.Debtor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DebtorRepository extends JpaRepository<Debtor, Long> {
}
