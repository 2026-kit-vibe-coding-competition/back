package hackerton.educationentity.domain.guideline.entity;

import hackerton.educationentity.domain.ai_request.entity.AiRequest;
import hackerton.educationentity.domain.base.Base;
import hackerton.educationentity.domain.guideline.type.GuidelineAudience;
import hackerton.educationentity.domain.guideline.type.GuidelineStatus;
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
@Table(name = "guideline")
public class Guideline extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guideline_id", nullable = false)
    private Long id;

    @JoinColumn(name = "student_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Student student;

    @JoinColumn(name = "session_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Session session;

    @JoinColumn(name = "ai_request_id", nullable = false, unique = true)
    @OneToOne(fetch = FetchType.LAZY)
    private AiRequest aiRequest;

    @Column(name = "priority_gap", nullable = false)
    private String priorityGap;

    @Column(name = "summary", nullable = false)
    private String summary;

    @Column(name = "strength", nullable = false)
    private String strength;

    @Column(name = "improvement", nullable = false)
    private String improvement;

    @Column(name = "next_step", nullable = false)
    private String nextStep;

    @Column(name = "school_action", nullable = false)
    private String schoolAction;

    @Column(name = "home_action", nullable = false)
    private String homeAction;

    @Column(name = "next_check", nullable = false)
    private String nextCheck;

    @Enumerated(EnumType.STRING)
    @Column(name = "audience", nullable = false)
    private GuidelineAudience audience;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private GuidelineStatus status;

    @Column(name = "teacher_review_note")
    private String teacherReviewNote;

}
