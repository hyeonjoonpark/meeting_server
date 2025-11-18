package org.hyunjoonpark.meeting.domain.User;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hyunjoonpark.meeting.domain.University.University;
import org.hyunjoonpark.meeting.domain.User.enums.Gender;
import org.hyunjoonpark.meeting.domain.User.enums.MBTI;
import org.hyunjoonpark.meeting.domain.User.enums.Role;
import org.hyunjoonpark.meeting.domain.User.enums.Status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Table(name = "users", indexes = {
        @Index(name = "idx_user_university", columnList = "university_id"),
        @Index(name = "idx_user_mbti", columnList = "mbti")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id private String id; // ID
    @Column(name = "username", nullable = false) private String name; // 사용자 이름
    @Column(name = "nickname", nullable = false) private String nickname; // 닉네임
    @Column(name = "birthday", nullable = false) private LocalDateTime birth; // 생일
    @Column(name = "age", nullable = false)
    // 나이 자동 계산식
    // 현재 년도 - 태어난 년도 + 1
    private int age = LocalDateTime.now().getYear() - this.birth.getYear() + 1; // 나이
    
    @Setter
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "university_id", nullable = false)
    private University university; // 소속대학
    
    private String description; // 설명
    private String personality; // 이상형
    private String profileImage; // 프로필 이미지
    private List<String> hobby; // 취미
    
    private List<String> hashtags = new ArrayList<>(); // 해쉬태그
    @Enumerated(value = EnumType.STRING) private Role role = Role.ROLE_USER; // 권한
    @Enumerated(value = EnumType.STRING) @Column(nullable = false) private MBTI mbti; // MBTI
    @Enumerated(value = EnumType.STRING) private Gender gender; // 성별
    @Enumerated(value = EnumType.STRING) private Status status = Status.NORMAL; // 유저 상태
    
    private Integer maxAge;
    @Min(20)
    private Integer minAge;
    
    // User 엔티티에 추가
    @PreRemove
    private void preRemove() {
        if (university != null) {
            university.getUsers().remove(this);
            university = null;
        }
    }
    
    @Builder
    public User(String name, String nickname, LocalDateTime birth, int age, University university, String description, String personality, String profileImage, List<String> hobby, List<String> hashtags, Role role, MBTI mbti, Gender gender, Status status, Integer maxAge, Integer minAge) {
        this.name = name;
        this.nickname = nickname;
        this.birth = birth;
        this.age = age;
        this.university = university;
        this.description = description;
        this.personality = personality;
        this.profileImage = profileImage;
        this.hobby = hobby;
        this.hashtags = hashtags;
        this.role = role;
        this.mbti = mbti;
        this.gender = gender;
        this.status = status;
        this.maxAge = maxAge;
        this.minAge = minAge;
    }
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User user)) return false;
        return age == user.age && Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(nickname, user.nickname) && Objects.equals(birth, user.birth) && Objects.equals(university, user.university) && Objects.equals(description, user.description) && Objects.equals(personality, user.personality) && Objects.equals(profileImage, user.profileImage) && Objects.equals(hobby, user.hobby) && Objects.equals(hashtags, user.hashtags) && role == user.role && mbti == user.mbti && gender == user.gender && status == user.status && Objects.equals(maxAge, user.maxAge) && Objects.equals(minAge, user.minAge);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, name, nickname, birth, age, university, description, personality, profileImage, hobby, hashtags, role, mbti, gender, status, maxAge, minAge);
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", birth=" + birth +
                ", age=" + age +
                ", university=" + university +
                ", description='" + description + '\'' +
                ", personality='" + personality + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", hobby=" + hobby +
                ", hashtags=" + hashtags +
                ", role=" + role +
                ", mbti=" + mbti +
                ", gender=" + gender +
                ", status=" + status +
                ", maxAge=" + maxAge +
                ", minAge=" + minAge +
                '}';
    }
}