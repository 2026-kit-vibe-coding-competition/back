package hackerton.educationentity.domain.ai_request.entity;

import hackerton.educationentity.domain.ai_request.type.AiRequestStatus;
import hackerton.educationentity.domain.ai_request.type.AiRequestType;
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

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
@Table(name = "ai_request")
public class AiRequest extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ai_request_id", nullable = false)
    private Long id;

    @Column(name = "job_id", nullable = false)
    private String jobId;

    @JoinColumn(name = "student_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Student student;

    @JoinColumn(name = "session_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Session session;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_type", nullable = false)
    private AiRequestType requestType;

    @Column(name = "model_name", nullable = false)
    private String modelName;

    @Column(name = "prompt_version", nullable = false)
    private String promptVersion;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "input_payload", columnDefinition = "jsonb")
    private Map<String, Object> inputPayload;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "rag_context", columnDefinition = "jsonb")
    private Map<String, Object> ragContext;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "raw_response", columnDefinition = "jsonb")
    private Map<String, Object> rawResponse;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "parsed_response", columnDefinition = "jsonb")
    private Map<String, Object> parsedResponse;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AiRequestStatus status;

    @Column(name = "latency_seconds", precision = 10, scale = 3)
    private BigDecimal latencySeconds;

    @Column(name = "prompt_eval_count")
    private Integer promptEvalCount;

    @Column(name = "eval_count")
    private Integer evalCount;

    @Column(name = "error_message")
    private String errorMessage;

}
