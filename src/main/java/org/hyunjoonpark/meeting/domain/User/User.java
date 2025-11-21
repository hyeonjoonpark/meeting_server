package org.hyunjoonpark.meeting.domain.User;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hyunjoonpark.meeting.domain.University.University;
import org.hyunjoonpark.meeting.domain.User.enums.Gender;
import org.hyunjoonpark.meeting.domain.User.enums.MBTI;
import org.hyunjoonpark.meeting.domain.User.enums.Role;
import org.hyunjoonpark.meeting.domain.User.enums.Status;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Getter
@Table(name = "users", indexes = {
        @Index(name = "idx_user_university", columnList = "university_id"),
        @Index(name = "idx_user_mbti", columnList = "mbti"),
        @Index(name = "idx_user_nickname", columnList = "nickname")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id private String id; // ID
    
    @NotBlank
    @Column(name = "username", nullable = false) 
    private String name; // 사용자 이름
    
    @NotBlank
    @Size(min = 2, max = 20, message = "닉네임은 2자 이상 20자 이하이여야 합니다") // 길이 제약
    @Column(name = "nickname", nullable = false)
    private String nickname; // 닉네임(20자 이하)
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "birthday", nullable = false)
    private LocalDate birth; // 생일
    
    @Setter
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "university_id", nullable = false)
    private University university; // 소속대학
    
    private String description; // 설명
    private String personality; // 이상형
    private String profileImage; // 프로필 이미지
    
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_hobby", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "hobby")
    private List<String> hobby = new ArrayList<>();
    
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_hashtags", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "hashtag")
    private List<String> hashtags = new ArrayList<>(); // 해쉬태그
    
    @Enumerated(value = EnumType.STRING) private Role role = Role.ROLE_USER; // 권한
    @Enumerated(value = EnumType.STRING) @Column(nullable = false) private MBTI mbti; // MBTI
    @Enumerated(value = EnumType.STRING) private Gender gender; // 성별
    @Enumerated(value = EnumType.STRING) private Status status = Status.NORMAL; // 유저 상태
    
    @Min(20) private Integer maxAge;
    @Min(20) private Integer minAge;
    
    // User 엔티티에 추가
    @PreRemove
    private void preRemove() {
        if (university != null) {
            university.getUsers().remove(this);
            university = null;
        }
    }
    
    @Builder
    public User(String name, String nickname, LocalDate birth, University university, String description, String personality, String profileImage, List<String> hobby, List<String> hashtags, Role role, MBTI mbti, Gender gender, Status status, Integer maxAge, Integer minAge) {
        this.name = name;
        this.nickname = nickname;
        this.birth = birth;
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
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        // id가 null이 아닌 경우(DB에 저장된 후) id만으로 비교
        return this.id != null && this.id.equals(user.id);
    }
    
    @Override
    public int hashCode() {
        // 클래스의 해시코드를 기본값으로 사용
        return getClass().hashCode();
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", birth=" + birth +
                ", universityId=" + (university != null ? university.getUniversityId() : "null") + // ID만 출력
                ", description='" + description + '\'' +
                ", personality='" + personality + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", hobbyCount=" + (hobby != null ? hobby.size() : 0) + // 개수만 출력
                ", hashtagsCount=" + (hashtags != null ? hashtags.size() : 0) + // 개수만 출력
                ", role=" + role +
                ", mbti=" + mbti +
                ", gender=" + gender +
                ", status=" + status +
                ", maxAge=" + maxAge +
                ", minAge=" + minAge +
                '}';
    }

    @Transient
    public int getAge() {
        // 만나이 계산
        return Period.between(this.birth, LocalDate.now()).getYears();
    }
}