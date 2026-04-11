package hackerton.educationentity.domain.evaluation.entity;

import hackerton.educationentity.domain.session.entity.Session;
import hackerton.educationentity.domain.student.entity.Student;
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
@Table(
        name = "evaluation",
        uniqueConstraints = @UniqueConstraint(name = "uk_evaluation_session_student", columnNames = {"session_id", "student_id"})
)
public class Evaluation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "evaluation_id", nullable = false)
    private Long id;

    @JoinColumn(name = "session_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Session session;

    @JoinColumn(name = "student_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Student student;

    @Column(name = "assignment_data_ref", nullable = false)
    private String dataRef;

    @Column(name = "evaluation_memo", nullable = false)
    private String memo;

}
