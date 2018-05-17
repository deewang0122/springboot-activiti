package com.springbootactiviti.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "funder_company")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FunderCompany {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    public FunderCompany(Long id) {
        this.id = id;
    }
}
