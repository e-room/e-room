package com.project.Project.domain.member;

import com.project.Project.auth.enums.MemberRole;
import com.project.Project.domain.BaseEntity;
import com.project.Project.domain.auth.Role;
import com.project.Project.domain.enums.AuthProviderType;
import com.project.Project.domain.interaction.Favorite;
import com.project.Project.domain.interaction.ReviewLike;
import com.project.Project.domain.review.Review;
import com.project.Project.repository.member.RoleRepository;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Role> roles = new ArrayList<>();

    private String refreshToken;

    private String name;

    private String email;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profile_image_id", nullable = false)
    private ProfileImage profileImage;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "recent_map_location_id")
    private RecentMapLocation recentMapLocation;

    public void setRecentMapLocation(RecentMapLocation recentMapLocation) {
        this.recentMapLocation = recentMapLocation;
    }


    @Enumerated(EnumType.STRING)
    private AuthProviderType authProviderType;

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

    public void setRoles(List<MemberRole> roleEnums, RoleRepository roleRepository) {
        List<Role> roleOfMembers = roleRepository.findRoleByMember(this);
        roleRepository.deleteAll(roleOfMembers);
        List<Role> newRoles = roleEnums.stream().map((roleEnum) -> Role.builder()
                .memberRole(roleEnum).member(this).build()).collect(Collectors.toList());
        List<Role> savedRoles = roleRepository.saveAll(newRoles);
        this.roles = savedRoles;
    }

    @PreRemove
    public void deleteHandler() {
        super.setDeleted(true);
    }
}
