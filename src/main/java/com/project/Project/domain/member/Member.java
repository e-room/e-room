package com.project.Project.domain.member;

import com.project.Project.domain.BaseEntity;
import com.project.Project.domain.enums.MemberRole;
import com.project.Project.domain.interaction.Favorite;
import com.project.Project.domain.interaction.ReviewLike;
import com.project.Project.domain.review.Review;
import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Where(clause = "deleted=false")
//@SQLDelete(sql = "UPDATE member SET deleted = true WHERE id=?")
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
    private List<ReviewLike> reviewLikeList = new ArrayList<>();

//    // Oauth 회원번호
//    private Long userId;

    private String refreshToken;

    private String name;

    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profile_image_id", nullable = false)
    private ProfileImage profileImage;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "recent_map_location_id")
    private RecentMapLocation recentMapLocation;

    public void setRecentMapLocation(RecentMapLocation recentMapLocation) {
        this.recentMapLocation = recentMapLocation;
    }

    public Member update(String name) {
        this.name = name;
        return this;
    }

    // 우선 단방향으로 구현, 양방향이 필요한 요구사항 추가시, 편의메소드로 변경
    public void setProfileImage(ProfileImage profileImage) {
        this.profileImage = profileImage;
    }
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRoleKey() {
        return this.memberRole.getKey();
    }

    @PreRemove
    public void deleteHandler(){
        super.setDeleted(true);
    }
}
