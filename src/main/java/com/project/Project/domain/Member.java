package com.project.Project.domain;

import com.project.Project.domain.enums.MemberRole;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data @Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE member SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@Entity
public class Member extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "member")
    private List<Review> reviewList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<MemberRoom> favoriteRoomList = new ArrayList<>();

//    // Oauth 회원번호
//    private Long userId;

    private String name;

    private String email;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    private String profileImageUrl;

    public Member update(String name, String profileImageUrl) {
        this.name = name;
        this.profileImageUrl = profileImageUrl;

        return this;
    }

    public String getRoleKey() {
        return this.memberRole.getKey();
    }

    @PreRemove
    public void deleteHandler(){
        super.setDeleted(true);
    }
}
