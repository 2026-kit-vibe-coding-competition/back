package hackerton.educationentity.domain.student_access.entity;

import hackerton.educationentity.domain.student.entity.Student;
import hackerton.educationentity.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
@Table(name = "student_access")
public class StudentAccess {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_access_id", nullable = false)
    private Long id;

    @JoinColumn(name = "student_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Student student;

    @JoinColumn(name = "teacher_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User teacher;

    @Enumerated(EnumType.STRING)
    @Column(name = "student_access_type", nullable = false)
    private AccessType type;

}
