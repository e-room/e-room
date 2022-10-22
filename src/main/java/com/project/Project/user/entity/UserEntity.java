package com.project.Project.user.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(name = "USERNAME")
    private String name;

    @Column(name = "AGE")
    private int age;

    @PostUpdate
    public void postUpdate() {
        System.out.println(">>> postUpdate");
    }

    @PreUpdate
    public void preUpdate() {
        System.out.println(">>> preUpdate");
    }

    @PrePersist
    public void prePersist() {
        System.out.println(">>> prePersist");
    }

    @PostPersist
    public void postPersist() {
        System.out.println(">>> postPersist");
    }
}
