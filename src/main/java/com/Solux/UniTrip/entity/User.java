package com.Solux.UniTrip.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    private UserType userType;

    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified=false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @OneToMany(mappedBy = "user")
    private List<Board> boardList;

    public enum UserType {
        PERSONAL,
        ORGANIZATION
    }

    public void update(String name, String email, String nickname, UserType userType) {
        this.name = name;
        this.email = email;
        if (nickname != null && !nickname.isEmpty() && !nickname.equals("defaultNickname")) {
            this.nickname = nickname;
        }
        if (userType != null && userType != UserType.PERSONAL) {
            this.userType = userType;
        }
    }

    public void updateProfile(String nickname, String phoneNumber, UserType userType, boolean emailVerified) {
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.userType = userType;
        this.emailVerified = emailVerified;
    }

    public void updateProfileImage(String imageUrl) {
        this.profileImageUrl = imageUrl;
    }

    public void verifyEmail() {
        this.emailVerified = true;
    }


}
