package hackerton.educationentity.application.service;

import hackerton.educationentity.application.dto.request.CreateAssignmentRequest;
import hackerton.educationentity.application.dto.request.UpdateAssignmentRequest;
import hackerton.educationentity.application.dto.response.AssignmentResponse;
import hackerton.educationentity.domain.assignment.entity.Assignment;
import hackerton.educationentity.domain.assignment.repository.AssignmentRepository;
import hackerton.educationentity.domain.session.entity.Session;
import hackerton.educationentity.domain.session.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AssignmentService {
    private final AssignmentRepository assignmentRepository;
    private final SessionRepository sessionRepository;

    public List<AssignmentResponse> getAssignments(Long sessionId, String title) {
        return assignmentRepository.search(sessionId, title).stream()
                .map(this::toResponse)
                .toList();
    }

    public AssignmentResponse getAssignment(Long id) {
        return toResponse(getAssignmentEntity(id));
    }

    @Transactional
    public AssignmentResponse createAssignment(CreateAssignmentRequest request) {
        Session session = getSession(request.sessionId());

        Assignment assignment = Assignment.builder()
                .session(session)
                .title(request.title())
                .description(request.description())
                .formRef(request.formRef())
                .optional(Boolean.TRUE.equals(request.optional()))
                .build();

        return toResponse(assignmentRepository.save(assignment));
    }

    @Transactional
    public AssignmentResponse updateAssignment(Long id, UpdateAssignmentRequest request) {
        Assignment assignment = getAssignmentEntity(id);
        Session session = getSession(request.sessionId());

        assignment.setSession(session);
        assignment.setTitle(request.title());
        assignment.setDescription(request.description());
        assignment.setFormRef(request.formRef());
        assignment.setOptional(Boolean.TRUE.equals(request.optional()));

        return toResponse(assignment);
    }

    @Transactional
    public void deleteAssignment(Long id) {
        Assignment assignment = getAssignmentEntity(id);
        assignmentRepository.delete(assignment);
    }

    private Assignment getAssignmentEntity(Long id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Assignment not found"));
    }

    private Session getSession(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found"));
    }

    private AssignmentResponse toResponse(Assignment assignment) {
        return new AssignmentResponse(
                assignment.getId(),
                assignment.getSession().getId(),
                assignment.getTitle(),
                assignment.getDescription(),
                assignment.getFormRef(),
                assignment.isOptional()
        );
    }
}
