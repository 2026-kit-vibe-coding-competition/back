package hackerton.educationentity.domain.assignment.entity;

import hackerton.educationentity.domain.session.entity.Session;
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
@Table(name = "assignment")
public class Assignment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_id", nullable = false)
    private Long id;

    @JoinColumn(name = "session_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Session session;

    @Column(name = "assignment_title", nullable = false)
    private String title;

    @Column(name = "assignment_description", nullable = false)
    private String description;

    @Column(name = "assignment_form_ref", nullable = false)
    private String formRef;

}
