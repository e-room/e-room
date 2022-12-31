package com.project.Project.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Builder
@Getter
@AllArgsConstructor()
@NoArgsConstructor()
@Entity
public class Uuid extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    private String uuid;
    
}
