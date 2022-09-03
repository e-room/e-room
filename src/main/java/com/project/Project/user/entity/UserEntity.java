package com.project.Project.user.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class UserEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(name = "USERNAME")
    private String name;

    @Column(name = "AGE")
    private int age;
}
