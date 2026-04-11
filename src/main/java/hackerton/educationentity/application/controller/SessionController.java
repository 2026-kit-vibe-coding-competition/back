package hackerton.educationentity.application.controller;

import hackerton.educationentity.application.dto.response.AssignmentResponse;
import hackerton.educationentity.application.dto.response.EvaluationResponse;
import hackerton.educationentity.application.dto.request.CreateSessionRequest;
import hackerton.educationentity.application.dto.request.UpdateSessionRequest;
import hackerton.educationentity.application.dto.response.SessionResponse;
import hackerton.educationentity.application.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sessions")
public class SessionController {
    private final SessionService sessionService;

    @GetMapping
    public List<SessionResponse> getSessions(@RequestParam(required = false) Long subjectId,
                                             @RequestParam(required = false) String title,
                                             @RequestParam(required = false) String date) {
        return sessionService.getSessions(subjectId, title, date);
    }

    @GetMapping("/{id}")
    public SessionResponse getSession(@PathVariable Long id) {
        return sessionService.getSession(id);
    }

    @GetMapping("/{id}/assignments")
    public List<AssignmentResponse> getSessionAssignments(@PathVariable Long id) {
        return sessionService.getSessionAssignments(id);
    }

    @GetMapping("/{id}/evaluations")
    public List<EvaluationResponse> getSessionEvaluations(@PathVariable Long id) {
        return sessionService.getSessionEvaluations(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponse createSession(@RequestBody CreateSessionRequest request) {
        return sessionService.createSession(request);
    }

    @PutMapping("/{id}")
    public SessionResponse updateSession(@PathVariable Long id, @RequestBody UpdateSessionRequest request) {
        return sessionService.updateSession(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSession(@PathVariable Long id) {
        sessionService.deleteSession(id);
    }
}
