package com.project.Project.domain.review;

import com.project.Project.domain.Member;
import com.project.Project.domain.enums.DTypeEnum;
import com.project.Project.domain.enums.KeywordEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE review_keyword SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@Entity
public class ReviewKeyword {

    @Id @GeneratedValue
    private Long id;

    // keywordType : 상속으로 해결하기 (DTYPE)
    @Enumerated(EnumType.STRING)
    private DTypeEnum dType;

    @OneToMany(mappedBy = "reviewKeyword", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<ReviewToReviewKeyword> reviewList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private KeywordEnum keywordType;
}
