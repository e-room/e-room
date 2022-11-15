package com.project.Project.domain;

import com.project.Project.domain.enums.MemberRole;
import com.project.Project.domain.interaction.Favorite;
import com.project.Project.domain.interaction.ReviewLike;
import com.project.Project.domain.review.Review;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@SQLDelete(sql = "UPDATE member SET deleted = true WHERE id=?")
//@Where(clause = "deleted=false")
@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "author")
    @Builder.Default
    private List<Review> reviewList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<Favorite> favoriteBuildingList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<ReviewLike> likeReviewList = new ArrayList<>();

//    // Oauth 회원번호
//    private Long userId;

    private String refreshToken;

    private String name;

    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    private String profileImageUrl;

    public Member update(String name, String profileImageUrl) {
        this.name = name;
        this.profileImageUrl = profileImageUrl;

        return this;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRoleKey() {
        return this.memberRole.getKey();
    }

//    @PreRemove
//    public void deleteHandler(){
//        super.setDeleted(true);
//    }
}
