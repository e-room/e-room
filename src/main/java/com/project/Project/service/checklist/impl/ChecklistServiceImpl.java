package com.project.Project.service.checklist.impl;

import com.project.Project.common.aws.s3.metadata.ChecklistImageMetadata;
import com.project.Project.common.exception.ErrorCode;
import com.project.Project.common.exception.checklist.ChecklistException;
import com.project.Project.common.serializer.checklist.ChecklistSerializer;
import com.project.Project.controller.checklist.dto.ChecklistRequestDto;
import com.project.Project.domain.Uuid;
import com.project.Project.domain.checklist.CheckList;
import com.project.Project.domain.checklist.CheckListImage;
import com.project.Project.domain.checklist.CheckListQuestion;
import com.project.Project.domain.enums.Expression;
import com.project.Project.domain.member.Member;
import com.project.Project.repository.checklist.ChecklistQuestionRepository;
import com.project.Project.repository.checklist.ChecklistRepository;
import com.project.Project.repository.checklist.QuestionRepository;
import com.project.Project.service.checklist.ChecklistService;
import com.project.Project.service.fileProcess.ChecklistImageProcess;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class ChecklistServiceImpl implements ChecklistService {

    private final ChecklistRepository checklistRepository;
    private final ChecklistImageProcess checklistImageProcess;
    private final ChecklistQuestionRepository checklistQuestionRepository;
    private final QuestionRepository questionRepository;

    @Override
    public CheckList getChecklist(Long checklistId) {
        return this.checklistRepository.findById(checklistId).orElseThrow(() -> new ChecklistException(ErrorCode.CHECKLIST_NOT_FOUND));
    }

    @Override
    public CheckListImage saveChecklistImage(CheckList checklist, MultipartFile image) {
        Uuid uuid = checklistImageProcess.createUUID();
        ChecklistImageMetadata reviewImagePackageMeta = ChecklistImageMetadata.builder()
                .checklistId(checklist.getId())
                .uuid(uuid.getUuid())
                .uuidEntity(uuid)
                .build();
        return checklistImageProcess.uploadImageAndMapToChecklist(image, reviewImagePackageMeta, checklist);
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
}
