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

    @Query("select distinct b from Building b where b.address.detailedAddress like %:params% or b.address.roadName like %:params%")
    List<Building> findBuildingsByAddress(String params);

    List<Building> findByAddressDetailedAddressContaining(String params);

    List<Building> findByAddress_RoadNameContaining(String params);
    @Query("select distinct b from Building b where b.address = :address")
    Optional<Building> findByAddress(@Param("address") String address);
}
