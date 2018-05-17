package com.springbootactiviti.demo.dao;

import com.springbootactiviti.demo.vo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByRole(String role);

    List<User> findByRoleIn(List<String> roles);
 }
