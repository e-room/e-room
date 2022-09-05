package com.project.Project.domain.embedded;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.ArrayList;
import java.util.List;

@Getter @NoArgsConstructor @AllArgsConstructor @Builder
@Embeddable
public class Address {
    /*
        도로명 주소 체계 설명
        광역지자체(ex. 서울시, 경기도 등) - 기초지자체(ex. 종로구, 수원시 등) - 시군구(영통구 등) - 읍면 - 도로명 - 건물번호 - 상세주소 - 참고항목

        서울특별시 종로구 사직로 161(세종로)
        광주광역시 광산구 북문대로419번길 14-1(신창동)
        경기도 수원시 영통구 매영로425번길 4(영통동)

        - 도로명은 대로, 로, 길 중 하나임
        - 법정동은 읍면 필드가 아니라 참고항목에 들어감
        - 참고항목은 보통 법정동이나 공동주택명이 들어감
    */

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

    /**
     * 상세주소
     */
    private String detailedAddress;

    /**
     * 참고항목
     */
    private String referenceItem;

    /**
     * 문자열로 된 주소를 Address 객체로 변환
     * @param stringAddress 문자열로 된 주소
     * @return Address 객체 반환
     */
    public static Address valueOf(String stringAddress) {
        /*
            todo : 구현
            todo : Q. 우리의 Address 엔티티에 상세주소와 참고항목이 필요한가? Room 엔티티에 있지 않은가?
            광역지차체 : 시 or 도
            기초지자체 : 시 or 구
            시군구 : 구
            읍면 : 읍 or 면
            도로명 : 로 or 길
            건물번호 : 0~9
            상세주소 : 자유
            참고항목 : 동 or 주택명
        */
        return Address.builder()
                .metropolitanGovernment("경기도")
                .basicLocalGovernment("수원시")
                .siGunGu("영통구")
                .eupMyeon("")
                .roadName("매영로425번길")
                .buildingNumber("4")
                .detailedAddress("302호")
                .referenceItem("")
                .build();
    }

    @Override
    public String toString() {
        return  metropolitanGovernment + " " + basicLocalGovernment + " " +  siGunGu + " " + eupMyeon +  " " +  roadName + " " + buildingNumber;
    }
}
