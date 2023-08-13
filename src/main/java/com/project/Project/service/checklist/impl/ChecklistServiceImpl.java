package com.project.Project.service.checklist.impl;

import com.project.Project.common.aws.s3.metadata.ChecklistImageMetadata;
import com.project.Project.common.exception.ErrorCode;
import com.project.Project.common.exception.checklist.ChecklistException;
import com.project.Project.common.serializer.checklist.ChecklistSerializer;
import com.project.Project.controller.checklist.dto.ChecklistRequestDto;
import com.project.Project.domain.Uuid;
import com.project.Project.domain.checklist.CheckList;
import com.project.Project.domain.checklist.CheckListImage;
import com.project.Project.domain.member.Member;
import com.project.Project.repository.checklist.ChecklistCustomRepository;
import com.project.Project.repository.checklist.ChecklistImageRepository;
import com.project.Project.repository.checklist.ChecklistRepository;
import com.project.Project.service.checklist.ChecklistService;
import com.project.Project.service.fileProcess.ChecklistImageProcess;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Component
@RequiredArgsConstructor
public class ChecklistServiceImpl implements ChecklistService {

    private final ChecklistRepository checklistRepository;
    private final ChecklistCustomRepository checklistCustomRepository;
    private final ChecklistImageRepository checklistImageRepository;
    private final ChecklistImageProcess checklistImageProcess;

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
        return ChecklistSerializer.toChecklist(request, member);
    }
}
