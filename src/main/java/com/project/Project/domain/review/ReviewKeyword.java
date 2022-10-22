package com.project.Project.domain.review;

import com.project.Project.domain.BaseEntity;
import com.project.Project.domain.enums.DTypeEnum;
import com.project.Project.domain.enums.KeywordEnum;
import com.project.Project.repository.review.ReviewKeywordRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE review_keyword SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "UniqueReviewKeyword", columnNames = {"dType", "keywordType"})
        }
)
public class ReviewKeyword extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    // keywordType : 상속으로 해결하기 (DTYPE)
    @Enumerated(EnumType.STRING)
    private DTypeEnum dType;

    @OneToMany(mappedBy = "reviewKeyword", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<ReviewToReviewKeyword> reviewList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private KeywordEnum keywordType;

    public static void init(ReviewKeywordRepository reviewKeywordRepository) {

        List<KeywordEnum> keywordEnums = List.of(KeywordEnum.values());
        List<KeywordEnum> keywordEnumsInDB = reviewKeywordRepository.findAll().stream().map(reviewKeyword -> reviewKeyword.getKeywordType()).collect(Collectors.toList());
        if (reviewKeywordRepository.findAll().size() == keywordEnums.size()) {
            return;
        } else if (keywordEnumsInDB.isEmpty()) {
            keywordEnums.stream().forEach((reviewKeywordEnum) -> {
                ReviewKeyword advantageKeyword = ReviewKeyword.builder()
                        .dType(DTypeEnum.ADVANTAGE)
                        .keywordType(reviewKeywordEnum)
                        .build();
                ReviewKeyword disadvantageKeyword = ReviewKeyword.builder()
                        .dType(DTypeEnum.DISADVANTAGE)
                        .keywordType(reviewKeywordEnum)
                        .build();
                reviewKeywordRepository.saveAll(List.of(advantageKeyword, disadvantageKeyword));
            });
        } else {
            //something not in DB
            List<KeywordEnum> notInDbList = keywordEnums.stream().filter((keywordEnum) -> !(keywordEnumsInDB.contains(keywordEnum))).collect(Collectors.toList());
            //새롭게 저장

            notInDbList.stream().forEach(keywordEnum -> {
                ReviewKeyword advantageKeyword = ReviewKeyword.builder()
                        .dType(DTypeEnum.ADVANTAGE)
                        .keywordType(keywordEnum)
                        .build();
                ReviewKeyword disadvantageKeyword = ReviewKeyword.builder()
                        .dType(DTypeEnum.DISADVANTAGE)
                        .keywordType(keywordEnum)
                        .build();
                reviewKeywordRepository.saveAll(List.of(advantageKeyword, disadvantageKeyword));
            });
        }
    }
}
