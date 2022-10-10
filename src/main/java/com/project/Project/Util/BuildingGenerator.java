package com.project.Project.Util;

import com.project.Project.domain.building.Building;
import com.project.Project.domain.embedded.Address;
import com.project.Project.domain.embedded.Coordinate;
import com.project.Project.exception.ErrorCode;
import com.project.Project.exception.building.BuildingException;
import com.project.Project.repository.building.BuildingCustomRepository;
import com.project.Project.repository.building.BuildingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BuildingGenerator {

    private final WebClient kakaoMapWebClient;
    private final BuildingCustomRepository buildingCustomRepository;
    private final BuildingRepository buildingRepository;
    private static WebClient staticWebClient;
    private static BuildingCustomRepository staticBuildingCustomRepository;
    private static BuildingRepository staticBuildingRepository;

    @PostConstruct
    public void init() {
        staticBuildingCustomRepository = this.buildingCustomRepository;
        staticBuildingRepository = this.buildingRepository;
        staticWebClient = this.kakaoMapWebClient;
    }

    public static KakaoAddressAPI searchAddressByKakao(String param) {
        StringBuilder urlBuilder = new StringBuilder("https://dapi.kakao.com/v2/local/search/address.json"); /*URL*/
        KakaoAddressAPI kakaoAddress = staticWebClient.get()
                .uri(uriBuilder -> uriBuilder.queryParam("query", param).build())
                .retrieve()
                .bodyToMono(KakaoAddressAPI.class)
                .block();
        return kakaoAddress;
    }

    public static Building generateBuilding(Address address, Coordinate coordinate, String buildingName){
        return Building.builder()
                .coordinate(coordinate)
                .buildingName(buildingName)
                .address(address).build();
    }

    public static Building generateBuilding(Address address){
        String query = address.toString();

        KakaoAddressAPI kakaoAddress = BuildingGenerator.searchAddressByKakao(query);
        if(kakaoAddress.getDocuments().get(0).getAddress_type() == "ROAD") throw new BuildingException("주소가 완벽하지 않습니다.",ErrorCode.ADDRESS_NOT_FOUND);
        String type = kakaoAddress.getDocuments().get(0).getAddress_type();
        if(!kakaoAddress.getDocuments().get(0).getAddress_type().equals("ROAD_ADDR") ) throw new BuildingException("해당하는 도로명 주소가 없거나 지번 주소입니다.", ErrorCode.BUILDING_NOT_FOUND);
        return Building.builder()
                .coordinate(Coordinate.builder()
                        .longitude(Double.parseDouble(kakaoAddress.getDocuments().get(0).getX()))
                        .latitude(Double.parseDouble(kakaoAddress.getDocuments().get(0).getY()))
                        .build())
                .buildingName(kakaoAddress.getDocuments().get(0).getRoad_address().getBuilding_name())
                .address(address).build();
    }

    public static List<Building> generateBuildings(String param) {
        KakaoAddressAPI kakaoAddress = searchAddressByKakao(param);
        List<KakaoAddressAPI.Document> documents = kakaoAddress.getDocuments();
        List<Building> buildingList = new ArrayList<>();
        documents.stream().filter(document -> document.getAddress_type().equals("ROAD_ADDR")).forEach(document -> {
            Address address = Address.builder()
                    .siDo(document.getRoad_address().getRegion_1depth_name())
                    .siGunGu(document.getRoad_address().getRegion_2depth_name())
                    .eupMyeon(document.getRoad_address().getRegion_3depth_name())
                    .roadName(document.getRoad_address().getRoad_name())
                    .buildingNumber(document.getRoad_address().getSub_building_no())
                    .build();
            Coordinate coordinate = Coordinate.builder()
                    .longitude(Double.parseDouble(document.getX()))
                    .latitude(Double.parseDouble(document.getY()))
                    .build();
            String buildingName = document.getRoad_address().getBuilding_name();
            Building building = staticBuildingRepository.findBuildingByAddress(address)
                    .orElseGet(()->generateBuilding(address,coordinate,buildingName));
            buildingList.add(building);
        });
        return buildingList;
    }
}
