package com.project.Project.serializer.building;

import com.project.Project.controller.building.dto.BuildingResponseDto;
import com.project.Project.controller.room.dto.RoomResponseDto;
import com.project.Project.domain.building.Building;
import com.project.Project.domain.building.BuildingToReviewCategory;
import com.project.Project.domain.embedded.Address;
import com.project.Project.domain.embedded.Coordinate;
import com.project.Project.domain.enums.ReviewCategoryEnum;
import com.project.Project.repository.BuildingToReviewCategoryRepository;
import com.project.Project.repository.ReviewCategoryRepository;
import com.project.Project.repository.RoomRepository;
import com.project.Project.repository.projection.building.OnlyBuildingIdAndCoord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BuildingSerializer {

    private final RoomRepository roomRepository;
    private final BuildingToReviewCategoryRepository buildingToReviewCategoryRepository;
    private final ReviewCategoryRepository reviewCategoryRepository;

    private static RoomRepository staticRoomRepo;
    private static BuildingToReviewCategoryRepository staticBuildingToReviewCategoryRepo;
    private static ReviewCategoryRepository staticReviewCategoryRepo;
    @PostConstruct
    public void init(){
        staticRoomRepo = this.roomRepository;
        staticBuildingToReviewCategoryRepo = this.buildingToReviewCategoryRepository;
        staticReviewCategoryRepo = this.reviewCategoryRepository;
    }
    public static BuildingResponseDto.BuildingResponse toBuildingResponse(Building building){

        List<BuildingToReviewCategory> buildingToReviewCategoryList = building.getBuildingToReviewCategoryList();
        Map<ReviewCategoryEnum, Double> buildingSummary = new HashMap<>();
        buildingToReviewCategoryList.stream().forEach(buildingToReviewCategory -> buildingSummary.put(buildingToReviewCategory.getReviewCategory().getType(), buildingToReviewCategory.getAvgScore()));

        return BuildingResponseDto.BuildingResponse.builder()
                .buildingId(building.getId())
                .name(building.getBuildingName())
                .address(Address.toAddressDto(building.getAddress()).toString())
                .coordinate(Coordinate.toCoordinateDto(building.getCoordinate()))
                .isDirectDeal(false)
                .rooms(building.getRoomList().stream().map((room)-> RoomResponseDto.RoomListResponse.builder()
                        .roomId(room.getId())
                        .roomNumber(room.getRoomNumber())
                        .build()
                ).collect(Collectors.toList()))
                .buildingSummaries(buildingSummary)
                .build();
    }

    public static BuildingResponseDto.BuildingListResponse toBuildingListResponse(Building building){
        if(building.getBuildingToReviewCategoryList().isEmpty()){
            List<BuildingToReviewCategory> reviewCategories = staticBuildingToReviewCategoryRepo.findBuildingToReviewCategoriesByBuilding_Id(building.getId());
            building.setBuildingToReviewCategoryList(reviewCategories);
        }
        BuildingToReviewCategory maxScoreCategory = building.getBuildingToReviewCategoryList().stream().max(Comparator.comparing(BuildingToReviewCategory::getAvgScore)).get();
        return BuildingResponseDto.BuildingListResponse.builder()
                .buildingId(building.getId())
                .name(building.getBuildingName())
                .address(building.getAddress().toString())
                .isDirectDeal(false)
                .reviewCnt(building.getBuildingSummary().getReviewCnt())
                .scoreAvg(building.getBuildingSummary().getAvgScore())
                .bestCategory(maxScoreCategory.getReviewCategory().getType())
                .build();
    }

    public static BuildingResponseDto.BuildingCountResponse toBuildingCountResponse(OnlyBuildingIdAndCoord building){
        return BuildingResponseDto.BuildingCountResponse.builder()
                .buildingId(building.getId())
                .coordinateDto(Coordinate.toCoordinateDto(building.getCoordinate()))
                .build();
    }
}
