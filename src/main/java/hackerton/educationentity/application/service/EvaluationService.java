package hackerton.educationentity.application.service;

import hackerton.educationentity.application.dto.request.CreateEvaluationRequest;
import hackerton.educationentity.application.dto.request.UpdateEvaluationStatusRequest;
import hackerton.educationentity.application.dto.request.UpdateEvaluationRequest;
import hackerton.educationentity.application.dto.response.EvaluationResponse;
import hackerton.educationentity.domain.evaluation.entity.Evaluation;
import hackerton.educationentity.domain.evaluation.repository.EvaluationRepository;
import hackerton.educationentity.domain.evaluation.type.EvaluationStatus;
import hackerton.educationentity.domain.session.entity.Session;
import hackerton.educationentity.domain.session.repository.SessionRepository;
import hackerton.educationentity.domain.student.entity.Student;
import hackerton.educationentity.domain.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EvaluationService {
    private final EvaluationRepository evaluationRepository;
    private final SessionRepository sessionRepository;
    private final StudentRepository studentRepository;
    private final EvaluationStatusService evaluationStatusService;

    public List<EvaluationResponse> getEvaluations(Long sessionId, Long studentId) {
        return evaluationRepository.search(sessionId, studentId, EvaluationStatus.DELETED).stream()
                .map(this::toResponse)
                .toList();
    }

    public EvaluationResponse getEvaluation(Long id) {
        return toResponse(getEvaluationEntity(id));
    }

    @Transactional
    public EvaluationResponse createEvaluation(CreateEvaluationRequest request) {
        Session session = getSession(request.sessionId());
        Student student = getStudent(request.studentId());

        validateEvaluationIntegrity(session, student);
        if (evaluationRepository.existsBySessionIdAndStudentIdAndStatusNot(session.getId(), student.getId(), EvaluationStatus.DELETED)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Evaluation already exists for this session and student");
        }

        EvaluationStatus initialStatus = evaluationStatusService.resolveStatus(request.status());

        Evaluation evaluation = Evaluation.builder()
                .session(session)
                .student(student)
                .dataRef(request.dataRef())
                .memo(request.memo())
                .build();
        evaluationStatusService.applyStatus(evaluation, initialStatus, request.analyzingExpireTime());

        return toResponse(evaluationRepository.save(evaluation));
    }

    @Transactional
    public EvaluationResponse updateEvaluation(Long id, UpdateEvaluationRequest request) {
        Evaluation evaluation = getEvaluationEntity(id);
        Session session = getSession(request.sessionId());
        Student student = getStudent(request.studentId());

        validateEvaluationIntegrity(session, student);
        if (evaluationRepository.existsBySessionIdAndStudentIdAndIdNotAndStatusNot(session.getId(), student.getId(), id, EvaluationStatus.DELETED)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Evaluation already exists for this session and student");
        }

        evaluation.setSession(session);
        evaluation.setStudent(student);
        evaluation.setDataRef(request.dataRef());
        evaluation.setMemo(request.memo());
        if (request.status() != null || request.analyzingExpireTime() != null) {
            EvaluationStatus nextStatus = request.status() != null ? request.status() : evaluation.getStatus();
            evaluationStatusService.applyStatus(evaluation, nextStatus, request.analyzingExpireTime());
        }

        return toResponse(evaluation);
    }

    @Transactional
    public void deleteEvaluation(Long id) {
        Evaluation evaluation = getEvaluationEntity(id);
        evaluationStatusService.applyStatus(evaluation, EvaluationStatus.DELETED, null);
    }

    @Transactional
    public EvaluationResponse updateEvaluationStatus(Long id, UpdateEvaluationStatusRequest request) {
        if (request.status() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Evaluation status is required");
        }

        Evaluation evaluation = getEvaluationEntity(id);
        evaluationStatusService.applyStatus(evaluation, request.status(), request.analyzingExpireTime());
        return toResponse(evaluation);
    }

    private Evaluation getEvaluationEntity(Long id) {
        return evaluationRepository.findByIdAndStatusNot(id, EvaluationStatus.DELETED)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evaluation not found"));
    }

    private Session getSession(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found"));
    }

    private Student getStudent(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
    }

    private void validateEvaluationIntegrity(Session session, Student student) {
        Long sessionClassroomId = session.getSubject().getClassroom().getId();
        Long studentClassroomId = student.getClassroom().getId();

        if (!sessionClassroomId.equals(studentClassroomId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student classroom does not match session classroom");
        }
    }

    private EvaluationResponse toResponse(Evaluation evaluation) {
        return new EvaluationResponse(
                evaluation.getId(),
                evaluation.getSession().getId(),
                evaluation.getStudent().getId(),
                evaluation.getDataRef(),
                evaluation.getMemo(),
                evaluation.getStatus(),
                evaluation.getAnalyzingExpireTime()
        );
    }
}
