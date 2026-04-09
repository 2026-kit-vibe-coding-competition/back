package hackerton.educationentity.domain.classroom.entity;

import hackerton.educationentity.domain.school.entity.School;
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
@Table(name = "classroom")
public class Classroom {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "classroom_id", nullable = false)
    private Long id;

    @JoinColumn(name = "school_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private School school;

    @JoinColumn(name = "teacher_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User teacher;

    @Column(name = "classroom_grade", nullable = false)
    private String grade;

    @Column(name = "classroom_room", nullable = false)
    private String room;
}
