package com.project.Project.domain;

import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@SQLDelete(sql = "UPDATE building SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@Entity
public class Building extends BaseEntity{

    @Id @GeneratedValue
    private Long id;

    @Embedded
    private Address address;

    private Boolean hasElevator;

    @OneToMany(mappedBy = "building")
    private List<Room> roomList = new ArrayList<>();

    @PreRemove
    public void deleteHandler(){
        super.setDeleted(true);
    }
}
