package com.project.Project.util;


import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KakaoAddressAPI {

    private List<Document> documents;
    private MetaData meta;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class MetaData {
        private Boolean is_end;
        private Integer pageable_count;
        private Integer total_count;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Document {
        private Address address;
        private AddressByRoad road_address;
        private String address_name;
        private String address_type;
        private String x;
        private String y;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Address {
        private String address_name;
        private String b_code;
        private String h_code;
        private String main_address_no;
        private String mountain_yn;
        private String region_1depth_name;
        private String region_2depth_name;
        private String region_3depth_h_name;
        private String region_3depth_name;
        private String sub_address_no;
        private String x;
        private String y;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddressByRoad {
        private String address_name;
        private String building_name;
        private String main_building_no;
        private String region_1depth_name;
        private String region_2depth_name;
        private String region_3depth_name;
        private String road_name;
        private String sub_building_no;
        private String underground_yn;
        private String x;
        private String y;
        private String zone_no;
    }
}
