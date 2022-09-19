package com.project.Project.domain.embedded;

import com.project.Project.controller.building.dto.AddressDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter @NoArgsConstructor @AllArgsConstructor @Builder
@Embeddable
public class Address {
    /**
     * 광역지방자치단체
      */
    @Column(length = 20)
    private String metropolitanGovernment;

    /**
     * 기초지방자치단체
     */
    @Column(length = 20)
    private String basicLocalGovernment;

    /**
     * 시군구
     */
    @Column(length = 20)
    private String siGunGu;

    /**
     * 읍면동
     */
    @Column(length = 20)
    private String eupMyeon;

    /**
     * 도로명
     */
    private String roadName;

    /**
     * 건물 번호
     */
    private String buildingNumber;

//    /**
//     * 상세주소
//     */
//    private String detailedAddress;
//
//    /**
//     * 참고항목
//     */
//    private String referenceItem;

    public static AddressDto toAddressDto(Address address){
        return AddressDto.builder()
                .metropolitanGovernment(address.getMetropolitanGovernment())
                .basicLocalGovernment(address.getBasicLocalGovernment())
                .siGunGu(address.getSiGunGu())
                .eupMyeon(address.getEupMyeon())
                .roadName(address.getRoadName())
                .buildingNumber(address.getBuildingNumber())
                .build();
    }
}
