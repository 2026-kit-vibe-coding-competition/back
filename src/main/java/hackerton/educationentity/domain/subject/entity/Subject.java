package hackerton.educationentity.domain.subject.entity;

import hackerton.educationentity.domain.classroom.entity.Classroom;
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
@Table(name = "subject")
public class Subject {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_id", nullable = false)
    private Long id;

    @JoinColumn(name = "classroom_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Classroom classroom;

    @JoinColumn(name = "teacher_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User teacher;

    @Column(name = "subject_name", nullable = false)
    private String name;

}
