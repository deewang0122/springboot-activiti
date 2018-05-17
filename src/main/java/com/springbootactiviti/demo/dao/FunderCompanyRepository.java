package com.springbootactiviti.demo.dao;

import com.springbootactiviti.demo.vo.FunderCompany;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FunderCompanyRepository extends JpaRepository<FunderCompany, Long> {
}
