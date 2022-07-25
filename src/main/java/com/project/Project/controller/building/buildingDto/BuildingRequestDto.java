package com.project.Project.controller.building.buildingDto;

import com.project.Project.domain.embedded.Address;

public class BuildingRequestDto {

    public static class BuildingCountRequest{
        private Coordinate leftTop;
        private Coordinate rightBottom;
    }

    public static class AddressSearchParamRequest{
        private Address address;
    }

    public static class BuildingSearchParamRequest{
        private String buildingName;
    }
}
