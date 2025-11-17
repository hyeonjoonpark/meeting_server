package org.hyunjoonpark.meeting.domain.User;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hyunjoonpark.meeting.domain.User.enums.MBTI;
import org.hyunjoonpark.meeting.domain.User.enums.Role;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id private String id; // ID
    @Column(name = "username") private String name; // 사용자 이름
    @Column(name = "nickname") private String nickname; // 닉네임
    @Column(name = "birthday") private LocalDateTime birth; // 생일
    @Enumerated(value = EnumType.STRING) private Role role; // 권한
    
    private University university; // 소속대학
    
    private String description; // 설명
    private String personality; // 성격
    private List<String> hobby; // 취미
    private List<String> hashtags = new ArrayList<>(); // 해쉬태그
    @Enumerated(value = EnumType.STRING) private MBTI mbti; // MBTI
}
