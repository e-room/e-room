package com.project.Project.common.converter;

import com.project.Project.common.exception.ErrorCode;
import com.project.Project.common.exception.checklist.ChecklistImageException;
import com.project.Project.domain.checklist.CheckListImage;
import com.project.Project.repository.checklist.ChecklistImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StringToChecklistImage implements Converter<String, CheckListImage> {
    private final ChecklistImageRepository checklistImageRepository;


    @Override
    public CheckListImage convert(String source) {
        Long checklistId = Long.parseLong(source);
        return checklistImageRepository.findById(checklistId).orElseThrow(() -> new ChecklistImageException(ErrorCode.CHECKLIST_IMAGE_NOT_FOUND));
    }
}