package com.project.Project.repository;

import com.project.Project.domain.building.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BuildingRepository extends JpaRepository<Building,Long> {
    @Query("select distinct b from Building b join fetch b.roomList where b.id = :buildingId")
    Building findBuildingById(Long buildingId);

    @Query("select distinct b from Building  b join fetch b.roomList where b.id in :ids ")
    List<Building> findBuildingsByIdIn(List<Long> ids);

    @Query("select distinct b from Building b where b.address.metropolitanGovernment like %:params% or b.address.basicLocalGovernment like %:params% or b.address.siGunGu like %:params% or b.address.eupMyeon like %:params% or b.address.roadName like %:params% or b.address.buildingNumber like %:params% or b.buildingName like %:params%")
    List<Building> searchBuilding(String params);

    @Query("select distinct b from Building b where b.address = :address") // todo : 이게 동작할까?..
    Optional<Building> findByAddress(@Param("address") String address);

    List<Building> findAll();
}
