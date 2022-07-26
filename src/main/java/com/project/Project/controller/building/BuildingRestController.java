package com.project.Project.controller.building;

import com.project.Project.controller.building.dto.BuildingRequestDto;
import com.project.Project.controller.building.dto.BuildingResponseDto;
import com.project.Project.controller.building.enums.SearchCode;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/building")
public class BuildingRestController {

    @GetMapping("/marking")
    public List<BuildingResponseDto.BuildingCountResponse> getBuildingMarker(){
        /*
        request: none
        service
            - 좌표 범위 내에 있는 건물 list를 뽑음(Model)
            - 건물 list를 바탕으로 response 객체를 만들어서 전달.
        return: marking poistion과 buildingId를 가지는 객체 List으로
         */
    }

    @GetMapping("/")
    public List<BuildingResponseDto.BuildingListResponse> getBuildingList(@RequestBody List<BuildingRequestDto.BuildingListRequest> request){
        /*
        when: 리스트 뷰로 볼 때
        request: 좌표값 list
        return: 해당하는 건물 list
         */
    }

    @GetMapping("/{buildingId}")
    public BuildingResponseDto.BuildingResponse getBuilding(@PathVariable("buildingId") Integer buildingId){
        /*
        request: buildingId
        return: 단일 건물 BuildingResponse
         */
    }

    @GetMapping("/search/{code}")
    public List<BuildingResponseDto.BuildingResponse> searchBuilding(@PathVariable("code") SearchCode searchCode, @RequestParam("params") String params){
        /*
        request: 검색 코드(Enum), searchParams(주소,단일 Or 복수, 집 주소 등등)
        service:
            - 검색 코드를 바탕으로 동적으로 각자 다른 클래스를 호출하도록 동적으로 처리
        return: 건물 정보
         */
    }
}
