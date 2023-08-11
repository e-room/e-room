package com.project.Project.service.checklist.impl;

import com.project.Project.common.aws.s3.metadata.ChecklistImageMetadata;
import com.project.Project.common.exception.ErrorCode;
import com.project.Project.common.exception.checklist.ChecklistException;
import com.project.Project.domain.Uuid;
import com.project.Project.domain.checklist.CheckList;
import com.project.Project.domain.checklist.CheckListImage;
import com.project.Project.repository.checklist.ChecklistRepository;
import com.project.Project.service.checklist.ChecklistService;
import com.project.Project.service.fileProcess.ChecklistImageProcess;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


@Component
@RequiredArgsConstructor
public class ChecklistServiceImpl implements ChecklistService {

    private final ChecklistRepository checklistRepository;
    private final ChecklistImageProcess checklistImageProcess;

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
}
