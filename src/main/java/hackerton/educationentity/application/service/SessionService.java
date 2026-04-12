package hackerton.educationentity.application.service;

import hackerton.educationentity.application.dto.request.CreateSessionRequest;
import hackerton.educationentity.application.dto.request.UpdateSessionRequest;
import hackerton.educationentity.application.dto.response.SessionResponse;
import hackerton.educationentity.application.dto.response.AssignmentResponse;
import hackerton.educationentity.application.dto.response.EvaluationResponse;
import hackerton.educationentity.domain.assignment.repository.AssignmentRepository;
import hackerton.educationentity.domain.evaluation.repository.EvaluationRepository;
import hackerton.educationentity.domain.evaluation.type.EvaluationStatus;
import hackerton.educationentity.domain.session.entity.Session;
import hackerton.educationentity.domain.session.repository.SessionRepository;
import hackerton.educationentity.domain.subject.entity.Subject;
import hackerton.educationentity.domain.subject.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SessionService {
    private final SessionRepository sessionRepository;
    private final SubjectRepository subjectRepository;
    private final AssignmentRepository assignmentRepository;
    private final EvaluationRepository evaluationRepository;

    public List<SessionResponse> getSessions(Long subjectId, String title, String date) {
        return sessionRepository.search(subjectId, title, date).stream()
                .map(this::toResponse)
                .toList();
    }

    public SessionResponse getSession(Long id) {
        return toResponse(getSessionEntity(id));
    }

    public List<AssignmentResponse> getSessionAssignments(Long id) {
        getSessionEntity(id);
        return assignmentRepository.findBySessionId(id).stream()
                .map(assignment -> new AssignmentResponse(
                        assignment.getId(),
                        assignment.getSession().getId(),
                        assignment.getTitle(),
                        assignment.getDescription(),
                        assignment.getFormRef(),
                        assignment.isOptional()
                ))
                .toList();
    }

    public List<EvaluationResponse> getSessionEvaluations(Long id) {
        getSessionEntity(id);
        return evaluationRepository.findBySessionIdAndStatusNot(id, EvaluationStatus.DELETED).stream()
                .map(evaluation -> new EvaluationResponse(
                        evaluation.getId(),
                        evaluation.getSession().getId(),
                        evaluation.getStudent().getId(),
                        evaluation.getDataRef(),
                        evaluation.getMemo(),
                        evaluation.getStatus(),
                        evaluation.getAnalyzingExpireTime()
                ))
                .toList();
    }

    @Transactional
    public SessionResponse createSession(CreateSessionRequest request) {
        Subject subject = getSubject(request.subjectId());

        Session session = Session.builder()
                .subject(subject)
                .title(request.title())
                .date(request.date())
                .build();

        return toResponse(sessionRepository.save(session));
    }

    @Transactional
    public SessionResponse updateSession(Long id, UpdateSessionRequest request) {
        Session session = getSessionEntity(id);
        Subject subject = getSubject(request.subjectId());

        session.setSubject(subject);
        session.setTitle(request.title());
        session.setDate(request.date());

        return toResponse(session);
    }

    @Transactional
    public void deleteSession(Long id) {
        Session session = getSessionEntity(id);
        sessionRepository.delete(session);
    }

    private Session getSessionEntity(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found"));
    }

    private Subject getSubject(Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject not found"));
    }

    private SessionResponse toResponse(Session session) {
        return new SessionResponse(
                session.getId(),
                session.getSubject().getId(),
                session.getTitle(),
                session.getDate()
        );
    }
}
