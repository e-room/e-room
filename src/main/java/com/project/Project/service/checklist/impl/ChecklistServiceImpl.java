package com.project.Project.service.checklist.impl;

import com.project.Project.common.aws.s3.metadata.ChecklistImageMetadata;
import com.project.Project.common.exception.ErrorCode;
import com.project.Project.common.exception.checklist.ChecklistException;
import com.project.Project.common.serializer.checklist.ChecklistSerializer;
import com.project.Project.controller.checklist.dto.ChecklistRequestDto;
import com.project.Project.controller.checklist.dto.ChecklistResponseDto;
import com.project.Project.controller.checklist.dto.ChecklistResponseDto.QuestionElementDto;
import com.project.Project.domain.Uuid;
import com.project.Project.domain.checklist.CheckList;
import com.project.Project.domain.checklist.CheckListImage;
import com.project.Project.domain.checklist.CheckListQuestion;
import com.project.Project.domain.enums.Expression;
import com.project.Project.domain.member.Member;
import com.project.Project.loader.checklist.ChecklistLoader;
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
        if (checklistImage.getCheckList().getId() != checklist.getId()) {
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

    @Transactional
    @Override
    public CheckList create(ChecklistRequestDto.ChecklistCreateDto request, Member member) {
        CheckList savedCheckList = ChecklistSerializer.toChecklist(request, member);

        List<CheckListQuestion> checkListQuestionList = questionRepository.findAll()
                .stream()
                .map(question -> {
                    CheckListQuestion newCheckListQuestion = CheckListQuestion.builder()
                            .question(question)
                            .expression(Expression.NONE)
                            .build();
                    newCheckListQuestion.setCheckList(savedCheckList);
                    return newCheckListQuestion;
                })
                .collect(Collectors.toList());
        checklistQuestionRepository.saveAll(checkListQuestionList);

        return savedCheckList;
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
    @Override
    public CheckListQuestion updateChecklistQuestion(Long checklistId, Long questionId, ChecklistRequestDto.ChecklistQuestionUpdateDto request, Member member) {
        CheckListQuestion checkListQuestion = checklistQuestionRepository.findByCheckList_IdAndQuestion_Id(checklistId, questionId)
                .orElseThrow(() -> new ChecklistException(ErrorCode.CHECKLIST_QUESTION_NOT_FOUND));

        if(!checkListQuestion.getCheckList().getAuthor().getId().equals(member.getId()))
            throw new ChecklistException(ErrorCode.CHECKLIST_QUESTION_ACCESS_DENIED);

        checkListQuestion.setExpression(request.getExpression());
        return checkListQuestion;
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
