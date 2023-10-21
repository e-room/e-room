package com.project.Project.service.fileProcess;

import com.project.Project.common.aws.s3.FileService;
import com.project.Project.common.aws.s3.metadata.ChecklistImageMetadata;
import com.project.Project.common.exception.ErrorCode;
import com.project.Project.common.exception.checklist.ChecklistException;
import com.project.Project.domain.checklist.CheckList;
import com.project.Project.domain.checklist.CheckListImage;
import com.project.Project.repository.checklist.ChecklistCustomRepository;
import com.project.Project.repository.checklist.ChecklistCustomRepositoryImpl;
import com.project.Project.repository.uuid.UuidCustomRepositoryImpl;
import com.project.Project.repository.uuid.UuidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Component
public class ChecklistImageProcess extends FileProcessServiceImpl<ChecklistImageMetadata> {

    private ChecklistCustomRepository checklistCustomRepository;

    private final Long MAX_IMAGE_COUNT = 5L;

    @Autowired
    public ChecklistImageProcess(FileService amazonS3Service, UuidCustomRepositoryImpl uuidCustomRepository, UuidRepository uuidRepository, ChecklistCustomRepositoryImpl checklistCustomRepository) {
        super(amazonS3Service, uuidCustomRepository, uuidRepository);
        this.checklistCustomRepository = checklistCustomRepository;
    }


    @Transactional
    public CheckListImage uploadImageAndMapToChecklist(MultipartFile file, ChecklistImageMetadata checklistImageMetadata, CheckList checkList) {
        Optional.ofNullable(checkList).orElseThrow(() -> new ChecklistException(ErrorCode.CHECKLIST_NOT_FOUND));
        Long imageCount = this.checklistCustomRepository.countByChecklistId(checkList.getId());
        if (imageCount > MAX_IMAGE_COUNT) {
            throw new ChecklistException(ErrorCode.CHECKLIST_IMAGE_COUNT_EXCEEDED);
        }
        String url = this.uploadImage(file, checklistImageMetadata);
        CheckListImage checkListImage = CheckListImage.builder().url(url)
                .uuid(checklistImageMetadata.getUuidEntity())
                .fileName(file.getOriginalFilename()).build();
        checkListImage.setChecklist(checkList);
        return checkListImage;
    }
}
