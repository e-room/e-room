package com.project.Project.domain.interaction;

import com.project.Project.domain.BaseEntity;
import com.project.Project.domain.Member;
import com.project.Project.domain.review.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UniqueLike",
                        columnNames = {
                                "member_id",
                                "review_id"
                        }
                )
        }
)
public class ReviewLike extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column()
    @ColumnDefault("1")
    @Builder.Default
    private boolean status = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @PreRemove
    public void deleteHandler() {
        super.setDeleted(true);
    }
}
