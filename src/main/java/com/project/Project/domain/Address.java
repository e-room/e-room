package com.project.Project.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

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
     * 읍면
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
}
