package com.project.Project.controller.building.dto;

import com.project.Project.domain.embedded.Address;

public class BuildingRequestDto {

    public static class BuildingListRequest{
        private Integer buildingId;
    }

    public static class AddressSearchParamRequest{
        private Address address;
    }

    public static class BuildingSearchParamRequest{
        private String buildingName;
    }
}
