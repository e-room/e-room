package com.project.Project.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Building {

    @Id @GeneratedValue
    private Long id;

    @Embedded
    private Address address;

    private Boolean hasElevator;

    @OneToMany(mappedBy = "building")
    private List<Room> roomList = new ArrayList<>();
}
