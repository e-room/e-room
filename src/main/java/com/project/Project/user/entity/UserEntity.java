package com.project.Project.user.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class UserEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(name = "USERNAME")
    private String name;

    @Column(name = "AGE")
    private int age;
}
