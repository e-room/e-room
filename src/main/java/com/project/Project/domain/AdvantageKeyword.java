package com.project.Project.domain;

import com.project.Project.domain.enums.AdvantageKeywordEnum;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Data
@SQLDelete(sql = "UPDATE review_form SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@Entity
public class AdvantageKeyword extends BaseEntity{

    @Id @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private AdvantageKeywordEnum advantageKeywordEnum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_form_id")
    private ReviewForm reviewForm;

    @PreRemove
    public void deleteHandler(){
        super.setDeleted(true);
    }
}
