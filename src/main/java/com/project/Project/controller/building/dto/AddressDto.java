package com.project.Project.controller.building.dto;

import lombok.Builder;

@Builder
public class AddressDto {

    /**
     * 광역지방자치단체
     */
    private String metropolitanGovernment;

    /**
     * 기초지방자치단체
     */
    private String basicLocalGovernment;

    /**
     * 시군구
     */
    private String siGunGu;

    /**
     * 읍면
     */
    private String eupMyeon;

    /**
     * 도로명
     */
    private String roadName;

    private String buildingNumber;


    @Override
    public String toString() {
        return  metropolitanGovernment + " " + basicLocalGovernment + " " +  siGunGu + " " + eupMyeon +  " " +  roadName + " " ;
    }

//    /**
//     * 상세주소
//     */
//    private String detailedAddress;
//
//    /**
//     * 참고항목
//     */
//    private String referenceItem;
}
