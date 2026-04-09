package hackerton.educationentity.domain.student.entity;

import hackerton.educationentity.domain.classroom.entity.Classroom;
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
@Table(name = "student")
public class Student {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id", nullable = false)
    private Long id;

    @JoinColumn(name = "classroom_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Classroom classroom;

    @Column(name = "student_name", nullable = false)
    private String name;

    @Column(name = "student_phone", nullable = false)
    private String phone;

    @Column(name = "student_status", nullable = false)
    private String status;

}
