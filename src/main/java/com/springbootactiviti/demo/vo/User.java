package com.springbootactiviti.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="\"user\"")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;            //主键id

    private String name;        //姓名

    @Column(name = "user_type")
    private String userType;    //sp_company, funder_company

    @Column(name = "type_id")
    private Long typeId;        //对应类型id

    private String role;        //用户角色

    public User(Long id) {
        this.id = id;
    }
}
