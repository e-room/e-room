package com.project.Project.controller.building.dto;

import com.project.Project.domain.embedded.Address;
import com.project.Project.validator.ExistBuilding;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BuildingRequestDto {

    @NoArgsConstructor @Getter @AllArgsConstructor @Builder
    public static class BuildingListRequest{
        @ExistBuilding
        private Long buildingId;
    }

//    @NoArgsConstructor @Getter @AllArgsConstructor @Builder
//    public static class AddressSearchParamRequest{
//        private Address address;
//    }
//    @NoArgsConstructor @Getter @AllArgsConstructor @Builder
//    public static class BuildingSearchParamRequest{
//        private String buildingName;
//    }
}