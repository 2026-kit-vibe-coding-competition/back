package hackerton.educationentity.domain.feedback_report.entity;

import hackerton.educationentity.domain.ai_request.entity.AiRequest;
import hackerton.educationentity.domain.base.Base;
import hackerton.educationentity.domain.session.entity.Session;
import hackerton.educationentity.domain.student.entity.Student;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
@Table(name = "feedback_report")
public class FeedbackReport extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_report_id", nullable = false)
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

    @Column(name = "student_name", nullable = false)
    private String studentName;

    @Column(name = "subject_name", nullable = false)
    private String subject;

    @Column(name = "lesson_topic", nullable = false)
    private String lessonTopic;

    @Column(name = "understanding_level", nullable = false)
    private Integer understandingLevel;

    @Column(name = "assignment_level", nullable = false)
    private Integer assignmentLevel;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "weakness_tags", columnDefinition = "jsonb")
    private List<String> weaknessTags;

    @Column(name = "teacher_memo", nullable = false)
    private String teacherMemo;

    @Column(name = "ocr_summary", nullable = false)
    private String ocrSummary;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "trend", columnDefinition = "jsonb")
    private List<Double> trend;
}
