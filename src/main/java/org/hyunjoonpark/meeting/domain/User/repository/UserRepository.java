package org.hyunjoonpark.meeting.domain.User.repository;

import org.hyunjoonpark.meeting.domain.University.University;
import org.hyunjoonpark.meeting.domain.User.User;
import org.hyunjoonpark.meeting.domain.User.enums.Gender;
import org.hyunjoonpark.meeting.domain.User.enums.MBTI;
import org.hyunjoonpark.meeting.domain.User.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    // 닉네임으로 사용자 찾기
    Optional<User> findByNickname(String nickname);
    
    // 대학별 사용자 목록 조회 (페이징)
    Page<User> findByUniversity(University university, Pageable pageable);
    
    // MBTI별 사용자 검색 (인덱스 활용)
    List<User> findByMbti(MBTI mbti);
    
    // 상태별 사용자 검색
    List<User> findByStatus(Status status);
    
    // 2. 복합 조건 검색
    
    // 대학 및 MBTI로 검색 (페이징)
    Page<User> findByUniversityAndMbti(University university, MBTI mbti, Pageable pageable);
    
    // 대학 및 성별로 검색 (페이징)
    Page<User> findByUniversityAndGender(
            University university,
            Gender gender,
            Pageable pageable);
    
    // 나이 범위로 검색
    @Query("SELECT u FROM User u WHERE u.university = :university " +
            "AND FUNCTION('YEAR', CURRENT_TIMESTAMP) - FUNCTION('YEAR', u.birth) " +
            "BETWEEN :minAge AND :maxAge")
    Page<User> findByUniversityAndAgeRange(
            @Param("university") University university,
            @Param("minAge") int minAge,
            @Param("maxAge") int maxAge,
            Pageable pageable
    );
    
    // 취미로 검색
    @Query("SELECT u FROM User u WHERE :hobby MEMBER OF u.hobby")
    Page<User> findByHobby(@Param("hobby") String hobby, Pageable pageable);
    
    // 해시태그로 검색
    @Query("SELECT u FROM User u WHERE :hashtag MEMBER OF u.hashtags")
    Page<User> findByHashtag(@Param("hashtag") String hashtag, Pageable pageable);
    
    // 대학별 사용자 수 계산
    @Query("SELECT COUNT(u) FROM User u WHERE u.university = :university")
    long countByUniversity(@Param("university") University university);
    
    // MBTI별 사용자 수 계산
    long countByMbti(MBTI mbti);
    
    // 최근 가입한 사용자 검색
    @Query("SELECT u FROM User u WHERE u.university = :university " +
            "ORDER BY u.birth DESC")
    Page<User> findRecentUsersByUniversity(@Param("university") University university, Pageable pageable);
    
    // 동적 쿼리를 위한 Specification 메서드
    @Query("SELECT u FROM User u WHERE " +
            "(:university is null OR u.university = :university) AND " +
            "(:mbti is null OR u.mbti = :mbti) AND " +
            "(:gender is null OR u.gender = :gender) AND " +
            "(:status is null OR u.status = :status)")
    Page<User> findByFilters(
            @Param("university") University university,
            @Param("mbti") MBTI mbti,
            @Param("gender") Gender gender,
            @Param("status") Status status,
            Pageable pageable
    );
    
    // 닉네임 중복 확인
    boolean existsByNickname(String nickname);
    
    // 대학에 특정 MBTI 사용자 존재 여부
    boolean existsByUniversityAndMbti(University university, MBTI mbti);
}