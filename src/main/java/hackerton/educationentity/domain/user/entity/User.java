package hackerton.educationentity.domain.user.entity;

import hackerton.educationentity.domain.school.entity.School;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    private UserRole role;

    @Column(name = "user_name", nullable = false, length = 100)
    private String name;

    @Column(name = "user_email", unique = true, length = 255)
    private String email;

    @Column(name = "user_password", nullable = false, length = 255)
    private String password;

    @Builder
    public User(School school, UserRole role, String name, String email, String password) {
        this.school = school;
        this.role = role;
        this.name = name;
        this.email = email;
        this.password = password;
    }

}
