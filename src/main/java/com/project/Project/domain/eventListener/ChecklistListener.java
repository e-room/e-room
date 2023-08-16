package com.project.Project.domain.eventListener;

import com.project.Project.common.util.ApplicationContextServe;
import com.project.Project.domain.checklist.CheckList;
import com.project.Project.domain.checklist.CheckListImage;
import com.project.Project.domain.checklist.CheckListQuestion;
import com.project.Project.domain.history.CheckListHistory;

import com.project.Project.domain.history.CheckListImageHistory;
import com.project.Project.domain.history.CheckListQuestionHistory;
import com.project.Project.repository.EventListener;
import com.project.Project.repository.history.CheckListHistoryRepository;
import com.project.Project.repository.history.CheckListImageHistoryRepository;
import com.project.Project.repository.history.CheckListQuestionHistoryRepository;


import javax.persistence.PreRemove;
import java.util.ArrayList;
import java.util.List;

public class ChecklistListener implements EventListener {


    @PreRemove
    public void preRemove(Object object) {
        if (object instanceof CheckList) {
            CheckList checkList = (CheckList) object;

            CheckListHistoryRepository checkListHistoryRepository = ApplicationContextServe.getApplicationContext().getBean(CheckListHistoryRepository.class);
            CheckListHistory checkListHistory = CheckListHistory.toCheckListHistory(checkList);
            checkListHistoryRepository.save(checkListHistory);

            CheckListQuestionHistoryRepository checkListQuestionHistoryRepository =  ApplicationContextServe.getApplicationContext().getBean(CheckListQuestionHistoryRepository.class);
            List<CheckListQuestionHistory> checkListResponsesHistory = new ArrayList<>();
            for(CheckListQuestion checkListQuestion : checkList.getCheckListResponses()) {
                CheckListQuestionHistory checkListQuestionHistory = CheckListQuestionHistory.toCheckListQuestionHistory(checkListQuestion);
                checkListResponsesHistory.add(checkListQuestionHistory);
            }
            checkListQuestionHistoryRepository.saveAll(checkListResponsesHistory);

            CheckListImageHistoryRepository checkListImageHistoryRepository =  ApplicationContextServe.getApplicationContext().getBean(CheckListImageHistoryRepository.class);
            List<CheckListImageHistory> checkListImageListHistory = new ArrayList<>();
            for(CheckListImage checkListImage : checkList.getCheckListImageList()) {
                CheckListImageHistory checkListImageHistory = CheckListImageHistory.toCheckListImageHistory(checkListImage);
                checkListImageListHistory.add(checkListImageHistory);
            }
            checkListImageHistoryRepository.saveAll(checkListImageListHistory);

        }
    }
}
