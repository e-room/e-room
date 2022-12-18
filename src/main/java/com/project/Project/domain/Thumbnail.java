package com.project.Project.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Getter
@AllArgsConstructor()
@NoArgsConstructor()
@Entity
public class Thumbnail extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne()
    @JoinColumn(name = "uuid_id")
    private Uuid uuid;

    private String url;

    private String fileName;
}
