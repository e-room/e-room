package com.project.Project.controller.building;

import com.project.Project.Util.JsonResult;
import com.project.Project.controller.building.dto.BuildingRequestDto;
import com.project.Project.controller.building.dto.BuildingResponseDto;
import com.project.Project.controller.building.enums.SearchCode;
import com.project.Project.validator.ExistBuilding;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/building")
public class BuildingRestController {

    /*
    when: 3.0.1
    request: none
    service
        - 좌표 범위 내에 있는 건물 list를 뽑음(Model)
        - 건물 list를 바탕으로 response 객체를 만들어서 전달.
    return: marking poistion과 buildingId를 가지는 객체 List으로
     */
    @GetMapping("/marking")
    public List<BuildingResponseDto.BuildingCountResponse> getBuildingMarker() {
        return null;
    }

    /*
    when: 3.0.2
    request: buildingId list
    return: 해당하는 건물 list
     */
    @GetMapping("/")
    public List<BuildingResponseDto.BuildingListResponse> getBuildingList(@RequestBody @Valid List<BuildingRequestDto.BuildingListRequest> request) {
        return null;
    }

    /*
    when: 3.2
    request: buildingId
    return: 단일 건물 BuildingResponse
     */
    @GetMapping("/{buildingId}")
    public BuildingResponseDto.BuildingResponse getBuilding(@PathVariable("buildingId") @ExistBuilding Long buildingId) {
        return null;
    }

    /* 8.1
    when:
    request: 검색 코드(Enum), searchParams(주소,단일 Or 복수, 집 주소 등등)
    service:
        - 검색 코드를 바탕으로 동적으로 각자 다른 클래스를 호출하도록 동적으로 처리
    return: 건물 정보
     */
    @GetMapping("/search")
    public List<BuildingResponseDto.BuildingResponse> searchBuilding(@RequestParam("params") String params) {
        return null;
    }
}
