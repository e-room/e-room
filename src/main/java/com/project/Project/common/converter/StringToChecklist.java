package com.project.Project.common.converter;

import com.project.Project.common.exception.ErrorCode;
import com.project.Project.common.exception.checklist.ChecklistException;
import com.project.Project.domain.checklist.CheckList;
import com.project.Project.repository.checklist.ChecklistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StringToChecklist implements Converter<String, CheckList> {
    private final ChecklistRepository checklistRepository;


    @Override
    public CheckList convert(String source) {
        Long checklistId = Long.parseLong(source);
        return checklistRepository.findById(checklistId).orElseThrow(() -> new ChecklistException(ErrorCode.CHECKLIST_NOT_FOUND));
    }
}
