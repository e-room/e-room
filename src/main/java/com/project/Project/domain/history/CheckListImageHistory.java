package com.project.Project.domain.history;


import com.project.Project.domain.BaseEntity;
import com.project.Project.domain.Uuid;
import com.project.Project.domain.checklist.CheckList;
import com.project.Project.domain.checklist.CheckListImage;
import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class CheckListImageHistory extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String fileName;

    private String url;

    private Long uuidId;

    private Long checkListId;

    public static CheckListImageHistory toCheckListImageHistory(CheckListImage checkListImage) {
        return CheckListImageHistory.builder()
                .fileName(checkListImage.getFileName())
                .url(checkListImage.getUrl())
                .uuidId(checkListImage.getUuid().getId())
                .checkListId(checkListImage.getCheckList().getId())
                .build();
    }
}
