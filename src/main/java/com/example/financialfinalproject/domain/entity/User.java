package com.example.financialfinalproject.domain.entity;

import com.example.financialfinalproject.domain.enums.SocialType;
import com.example.financialfinalproject.domain.enums.UserRole;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static com.example.financialfinalproject.domain.enums.UserRole.*;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE user SET deleted_at = CURRENT_TIMESTAMP WHERE user_id = ?")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    private String userName;

    private String password;

    private String email;

    private String nickname;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, columnDefinition = "varchar(10) default 'USER'")
    private UserRole userRole;

    @Enumerated(EnumType.STRING)
    private SocialType socialType; // KAKAO, NAVER, GOOGLE

    private String socialId; // 로그인한 소셜 타입의 식별자 값 (일반 로그인인 경우 null)

    private String refreshToken; // 리프레시 토큰

    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();


    @PrePersist
    public void prePersist() {
        this.userRole = this.userRole == null ? USER : this.userRole;
    }

    public User promoteRole(User user) {
        user.userRole = ADMIN;
        return user;
    }

    public User demoteRole(User user) {
        user.userRole = USER;
        return user;
    }

    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }

    @Builder
    public User(Integer id, String userName, String password, String email, String nickname, String imageUrl, UserRole userRole, SocialType socialType, String socialId, String refreshToken, List<Post> posts) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.userRole = userRole;
        this.socialType = socialType;
        this.socialId = socialId;
        this.refreshToken = refreshToken;
        this.posts = posts;
    }
}
