package com.project.Project.service.checklist.impl;

import com.project.Project.common.aws.s3.metadata.ChecklistImageMetadata;
import com.project.Project.common.exception.ErrorCode;
import com.project.Project.common.exception.checklist.ChecklistException;
import com.project.Project.common.serializer.checklist.ChecklistSerializer;
import com.project.Project.common.util.component.BuildingHelper;
import com.project.Project.controller.building.dto.AddressDto;
import com.project.Project.controller.checklist.dto.ChecklistRequestDto;
import com.project.Project.controller.checklist.dto.ChecklistResponseDto.QuestionElementDto;
import com.project.Project.domain.Uuid;
import com.project.Project.domain.building.Building;
import com.project.Project.domain.checklist.CheckList;
import com.project.Project.domain.checklist.CheckListImage;
import com.project.Project.domain.checklist.CheckListQuestion;
import com.project.Project.domain.embedded.Address;
import com.project.Project.domain.enums.Expression;
import com.project.Project.domain.member.Member;
import com.project.Project.loader.checklist.ChecklistLoader;
import com.project.Project.repository.building.BuildingRepository;
import com.project.Project.repository.checklist.ChecklistQuestionRepository;
import com.project.Project.repository.checklist.ChecklistCustomRepository;
import com.project.Project.repository.checklist.ChecklistImageRepository;
import com.project.Project.repository.checklist.ChecklistRepository;
import com.project.Project.repository.checklist.QuestionRepository;
import com.project.Project.service.checklist.ChecklistService;
import com.project.Project.service.fileProcess.ChecklistImageProcess;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class ChecklistServiceImpl implements ChecklistService {

    private final ChecklistRepository checklistRepository;
    private final ChecklistCustomRepository checklistCustomRepository;
    private final ChecklistImageRepository checklistImageRepository;
    private final ChecklistImageProcess checklistImageProcess;
    private final ChecklistQuestionRepository checklistQuestionRepository;
    private final QuestionRepository questionRepository;

    private final ChecklistLoader checklistLoader;
    private final BuildingRepository buildingRepository;
    private final BuildingHelper buildingHelper;

    /**
     * 주소가 제공된 경우에만 빌딩 정보를 반환합니다.
     * 주소가 없는 경우 Optional.empty()를 반환합니다.
     */
    /**
     * 주소가 제공된 경우에만 빌딩 정보를 반환합니다.
     * 주소가 없는 경우 Optional.empty()를 반환합니다.
     */
    private Optional<Building> getBuildingIfAddressProvided(Boolean hasBuildingAddress, AddressDto addressDto) {
        if(Boolean.TRUE.equals(hasBuildingAddress)) {
            Address address = AddressDto.toAddress(addressDto);
            return Optional.ofNullable(buildingRepository.findBuildingByAddress(address)
                    .orElseGet(() -> buildingHelper.searchOrCreateBuilding(address)));
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public CheckList getChecklist(Long checklistId) {
        return this.checklistRepository.findById(checklistId).orElseThrow(() -> new ChecklistException(ErrorCode.CHECKLIST_NOT_FOUND));
    }

    @Override
    @Transactional
    public CheckListImage saveChecklistImage(CheckList checklist, MultipartFile image) {
        Uuid uuid = checklistImageProcess.createUUID();
        ChecklistImageMetadata reviewImagePackageMeta = ChecklistImageMetadata.builder()
                .checklistId(checklist.getId())
                .uuid(uuid.getUuid())
                .uuidEntity(uuid)
                .build();
        return checklistImageProcess.uploadImageAndMapToChecklist(image, reviewImagePackageMeta, checklist);
    }

    @Override
    @Transactional
    public Long deleteChecklistImage(CheckList checklist, CheckListImage checklistImage) {
        Long checklistImageId = checklistImage.getId();
        if (!checklistImage.getCheckList().getId().equals(checklist.getId())) {
            throw new ChecklistException("삭제하려는 이미지는 해당 체크리스트에 속해있지 않습니다.", ErrorCode.CHECKLIST_IMAGE_NOT_FOUND);
        }
        this.checklistImageRepository.delete(checklistImage);
        return checklistImageId;
    }

    @Override
    @Transactional
    public List<CheckListImage> getCheckListImage(CheckList checklist) {
        return this.checklistCustomRepository.getCheckListImagesWithLock(checklist.getId());
    }

    /*
        TODO - 제약조건을 걸거나 유효성 검사를 통해 빌딩과 닉네임 중 하나만 갖도록 강제하기
     */
    @Transactional
    @Override
    public CheckList create(ChecklistRequestDto.ChecklistCreateDto request, Member member) {
        Optional<Building> optionalBuilding = getBuildingIfAddressProvided(request.getHasBuildingAddress(), request.getAddress());
        Building building = optionalBuilding.orElse(null);

        CheckList savedCheckList = checklistRepository.save(ChecklistSerializer.toChecklist(request, member, building, request.getNickname()));

        createCheckListQuestions(savedCheckList);

        return savedCheckList;
    }

    private void createCheckListQuestions(CheckList savedCheckList) {
        List<CheckListQuestion> checkListQuestionList = questionRepository.findAll()
                .stream()
                .map(question -> {
                    CheckListQuestion newCheckListQuestion = ChecklistSerializer.toCheckListQuestion(question, Expression.NONE);
                    newCheckListQuestion.setCheckList(savedCheckList);
                    return newCheckListQuestion;
                })
                .collect(Collectors.toList());
        checklistQuestionRepository.saveAll(checkListQuestionList);
    }

    @Override
    @Transactional
    public Long deleteById(Long checklistId) {
        CheckList checklist = checklistRepository.findById(checklistId).orElseThrow(() -> new ChecklistException(ErrorCode.CHECKLIST_NOT_FOUND));
        checklistLoader.loadAllRelations(checklist);
        checklistRepository.delete(checklist);
        return checklistId;
    }

    @Transactional
    @Override public CheckListQuestion updateChecklistQuestion(Long checklistId, Long questionId, ChecklistRequestDto.ChecklistQuestionUpdateDto request, Member member) {
        CheckListQuestion checkListQuestion = checklistQuestionRepository.findByCheckList_IdAndQuestion_Id(checklistId, questionId)
                .orElseThrow(() -> new ChecklistException(ErrorCode.CHECKLIST_QUESTION_NOT_FOUND));

        if(!checkListQuestion.getCheckList().getAuthor().getId().equals(member.getId()))
            throw new ChecklistException(ErrorCode.CHECKLIST_QUESTION_ACCESS_DENIED);

        /*
            NOTE
            PATCH 메소드로 요청으로 넘겨진 필드만 수정합니다.
            updateX 함수의 내부에서 null 체크를 수행합니다.
         */
        checkListQuestion.updateExpression(request.getExpression());
        checkListQuestion.updateMemo(request.getMemo());

        return checkListQuestion;
    }

    @Transactional
    @Override
    public CheckList updateChecklist(Long checklistId, ChecklistRequestDto.ChecklistUpdateDto request, Member member) {
        CheckList checkList = checklistRepository.findById(checklistId)
                .orElseThrow(() -> new ChecklistException(ErrorCode.CHECKLIST_NOT_FOUND));

        // 1. 주소 업데이트 - 빌딩 주소가 있을 경우
        Optional<Building> optionalBuilding = getBuildingIfAddressProvided(request.getHasBuildingAddress(), request.getAddress());
        Building building = optionalBuilding.orElse(null);
        checkList.updateBuilding(building);

        // 2. 별칭 업데이트 - 빌딩 주소가 없을 경우
        checkList.updateNickname(request.getNickname());

        // 3. 기본 정보 업데이트 - 동, 호수, 월세, 관리비, 보증금, 평수, 전체 메모, 별점
        checkList.updateLineNum(request.getLineNum());
        checkList.updateRoomNum(request.getRoomNum());
        checkList.updateMonthlyRent(request.getMonthlyRent());
        checkList.updateManagementFee(request.getManagementFee());
        checkList.updateDeposit(request.getDeposit());
        checkList.updateNetLeasableArea(request.getNetLeasableArea());
        checkList.updateMemo(request.getMemo());
        checkList.updateScore(request.getScore());

        return checkList;
    }

    @Override
    public List<CheckList> getUserCheckList(Long memberId) {
        return checklistRepository.findAllByAuthorId(memberId);
    }

    @Override
    public List<CheckList> getBuildingCheckList(Long buildingId) {
        return checklistRepository.findAllByBuildingId(buildingId);
    }
      
    @Override
    public List<QuestionElementDto> getChecklistQuestions(Long checklistId) {

        List<CheckListQuestion> checkListQuestionList = checklistQuestionRepository.findAllByCheckListId(checklistId);
        List<QuestionElementDto> questionElementList = new ArrayList<>();

        for(CheckListQuestion checkListQuestion : checkListQuestionList) {
            QuestionElementDto questionElementDto = QuestionElementDto.builder()
                    .id(checkListQuestion.getQuestion().getId())
                    .query(checkListQuestion.getQuestion().getQuery())
                    .description(checkListQuestion.getQuestion().getDescription())
                    .keyword(checkListQuestion.getQuestion().getKeyword())
                    .build();
            questionElementList.add(questionElementDto);
        }

        return questionElementList;
    }
}
