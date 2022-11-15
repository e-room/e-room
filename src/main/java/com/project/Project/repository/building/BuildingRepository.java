package com.project.Project.repository.building;

import com.project.Project.domain.building.Building;
import com.project.Project.domain.embedded.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BuildingRepository extends JpaRepository<Building, Long> {
    @Query("select distinct b from Building b join fetch b.roomList where b.id = :buildingId")
    Building findBuildingById(@Param("buildingId") Long buildingId);

    @Query("select distinct b from Building  b join fetch b.roomList where b.id in :ids ")
    List<Building> findBuildingsByIdIn(@Param("ids") List<Long> ids);

    @Query("select distinct b from Building b where b.address.siDo like %:params% or b.address.siGunGu like %:params% or b.address.eupMyeon like %:params% or b.address.roadName like %:params% or b.address.buildingNumber like %:params% or b.buildingName like %:params%")
    List<Building> searchBuildings(@Param("params") String params);

    @Query("select distinct b from Building b left join fetch b.roomList where b.address = :address")
    Optional<Building> findByAddress(@Param("address") Address address);

    Boolean existsBuildingByAddress(Address address);

    Optional<Building> findBuildingByAddress(@Param("address") Address address);


    <T> List<T> findBy(Class<T> projection);
}
