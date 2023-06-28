package com.project.Project.service.building;

import com.project.Project.domain.building.Building;
import com.project.Project.domain.building.BuildingSummary;
import com.project.Project.domain.embedded.Address;
import com.project.Project.domain.embedded.Coordinate;
import com.project.Project.exception.ErrorCode;
import com.project.Project.exception.building.BuildingException;
import com.project.Project.repository.building.BuildingCustomRepository;
import com.project.Project.repository.building.BuildingRepository;
import com.project.Project.repository.building.BuildingSummaryRepository;
import com.project.Project.util.KakaoAddressAPI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/*
Building Generator는 Address나 Coordinate과 같은 필수 값만을 보장합니다.
buildingName이나 hasElevator와 같은 이외의 값은 변경될 수도 있고 nullable이기도 하기에, service 단에서 update하여 사용합니다.
 */
@Component
@RequiredArgsConstructor
public class BuildingGenerator {

    private final WebClient kakaoMapWebClient;
    private final BuildingCustomRepository buildingCustomRepository;
    private final BuildingRepository buildingRepository;
    private final BuildingSummaryRepository buildingSummaryRepository;
    private static WebClient staticWebClient;
    private static BuildingCustomRepository staticBuildingCustomRepository;
    private static BuildingRepository staticBuildingRepository;
    private static BuildingSummaryRepository staticBuildingSummaryRepository;

    @PostConstruct
    public void init() {
        staticBuildingCustomRepository = this.buildingCustomRepository;
        staticBuildingRepository = this.buildingRepository;
        staticBuildingSummaryRepository = this.buildingSummaryRepository;
        staticWebClient = this.kakaoMapWebClient;
    }

    public static KakaoAddressAPI searchAddressByKakao(String param) {
        return staticWebClient.get()
                .uri(uriBuilder -> uriBuilder.queryParam("query", param).build())
                .retrieve()
                .bodyToMono(KakaoAddressAPI.class)
                .block();
    }

    public static Building generateBuilding(Address address, Coordinate coordinate) {
        return staticBuildingRepository.findByAddress(address).orElseGet(() -> {

            // building.create()
            Building buildingCreated = Building.builder()
                    .coordinate(coordinate)
                    .address(address).build();

            //buildingSummary.create()
            BuildingSummary buildingSummary = new BuildingSummary();
            buildingSummary.setBuilding(buildingCreated);

            return staticBuildingRepository.save(buildingCreated);
        });
    }

    public static Building generateBuilding(Address address) {
        return staticBuildingRepository.findByAddress(address)
                .orElseGet(() -> {
                    //주소 검색
                    String query = address.toString();
                    KakaoAddressAPI kakaoAddress = BuildingGenerator.searchAddressByKakao(query);
                    if (kakaoAddress.getDocuments().isEmpty()) {
                        System.out.println(query);
                        throw new BuildingException("해당하는 도로명 주소가 존재하지 않습니다..", ErrorCode.BUILDING_NOT_FOUND);
                    }
                    if (kakaoAddress.getDocuments().get(0).getAddress_type() == "ROAD")
                        throw new BuildingException("주소가 완벽하지 않습니다.", ErrorCode.ADDRESS_NOT_FOUND);
                    String type = kakaoAddress.getDocuments().get(0).getAddress_type();
                    if (!type.equals("ROAD_ADDR"))
                        throw new BuildingException("해당하는 도로명 주소가 없거나 지번 주소입니다.", ErrorCode.BUILDING_NOT_FOUND);
                    // building.create
                    Building buildingCreated = Building.builder()
                            .coordinate(Coordinate.builder()
                                    .longitude(Double.parseDouble(kakaoAddress.getDocuments().get(0).getX()))
                                    .latitude(Double.parseDouble(kakaoAddress.getDocuments().get(0).getY()))
                                    .build())
                            .buildingName(kakaoAddress.getDocuments().get(0).getRoad_address().getBuilding_name())
                            .address(address)
                            .build();
                    // buildingSummary.create
                    BuildingSummary buildingSummary = new BuildingSummary();
                    buildingSummary.setBuilding(buildingCreated);
                    staticBuildingRepository.save(buildingCreated);
                    return buildingCreated;
                });
    }

    public static List<Building> generateBuildings(String param) {
        //주소 검색
        KakaoAddressAPI kakaoAddress = searchAddressByKakao(param);
        List<KakaoAddressAPI.Document> documents = kakaoAddress.getDocuments();
        if (documents.isEmpty())
            throw new BuildingException("해당하는 도로명 주소가 존재하지 않습니다..", ErrorCode.BUILDING_NOT_FOUND);
        List<Building> buildingList = new ArrayList<>();

        //stream 처리
        documents.stream().filter(document -> document.getAddress_type().equals("ROAD_ADDR")).forEach(document -> {
            Address address = Address.builder()
                    .siDo(document.getRoad_address().getRegion_1depth_name())
                    .siGunGu(document.getRoad_address().getRegion_2depth_name())
                    .eupMyeon(document.getRoad_address().getRegion_3depth_name())
                    .roadName(document.getRoad_address().getRoad_name())
                    .buildingNumber(
                            document.getRoad_address().getSub_building_no().isEmpty() ?
                            document.getRoad_address().getMain_building_no() :
                            document.getRoad_address().getMain_building_no() + "-" + document.getRoad_address().getSub_building_no()
                            )
                    .build();
            Coordinate coordinate = Coordinate.builder()
                    .longitude(Double.parseDouble(document.getX()))
                    .latitude(Double.parseDouble(document.getY()))
                    .build();
            String buildingName = document.getRoad_address().getBuilding_name();
            //building.create()
            Building building = staticBuildingRepository.findBuildingByAddress(address)
                    .orElseGet(() -> generateBuilding(address, coordinate));
            building.setBuildingName(buildingName);
            buildingList.add(building);
        });
        return buildingList;
    }
}
