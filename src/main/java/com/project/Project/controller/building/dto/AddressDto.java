package com.project.Project.controller.building.dto;

import lombok.Builder;

@Builder
public class AddressDto {

    /**
     * 시도
     */
    private String siDo;

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
        return  siDo + " " +  siGunGu + " " + eupMyeon +  " " +  roadName + " " ;
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
