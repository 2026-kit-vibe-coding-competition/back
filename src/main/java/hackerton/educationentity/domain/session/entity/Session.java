package hackerton.educationentity.domain.session.entity;

import hackerton.educationentity.domain.subject.entity.Subject;
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
@Table(name = "session")
public class Session {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id", nullable = false)
    private Long id;

    @JoinColumn(name = "subject_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Subject subject;

    @Column(name = "session_title", nullable = false)
    private String title;

    @Column(name = "session_date", nullable = false)
    private String date;

}
