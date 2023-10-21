package com.project.Project.domain.checklist;

import com.project.Project.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Question extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String query;

    private String description;

    private String keyword;
}
