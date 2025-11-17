package org.hyunjoonpark.meeting.domain.University;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hyunjoonpark.meeting.domain.User.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
public class University {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long universityId;
    
    private String name; // 대학이름
    private String college; // 단과대학(공과대학, 에체능대학 등)
    private String department; // 학과(학부)
    private int enterYear;
    
    @BatchSize(size = 100)
    @OneToMany(mappedBy = "university", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<User> users = new ArrayList<>();
    
    public void addUsers(User user) {
        this.users.add(user);
        user.setUniversity(this);
    }

    public void removeUser(User user) {
        users.remove(user);
        user.setUniversity(null);
    }
    
    @Builder
    public University(String name, String college, String department, int enterYear, List<User> users) {
        this.name = name;
        this.college = college;
        this.department = department;
        this.enterYear = enterYear;
        this.users = users;
    }
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof University that)) return false;
        return enterYear == that.enterYear && Objects.equals(universityId, that.universityId) && Objects.equals(name, that.name) && Objects.equals(college, that.college) && Objects.equals(department, that.department) && Objects.equals(users, that.users);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(universityId, name, college, department, enterYear, users);
    }
    
    @Override
    public String toString() {
        return "University{" +
                "universityId=" + universityId +
                ", name='" + name + '\'' +
                ", college='" + college + '\'' +
                ", department='" + department + '\'' +
                ", enterYear=" + enterYear +
                ", users=" + users +
                '}';
    }
}