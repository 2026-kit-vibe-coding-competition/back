package hackerton.educationentity.domain.relation.entity;

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
@Table(name = "relation")
public class Relation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "relation_id", nullable = false)
    private Long id;

    @JoinColumn(name = "parent_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User parent;

    @JoinColumn(name = "student_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Student student;

    @Column(name = "relation", nullable = false)
    private String relation;

}
