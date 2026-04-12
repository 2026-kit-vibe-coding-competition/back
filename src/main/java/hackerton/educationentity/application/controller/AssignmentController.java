package hackerton.educationentity.application.controller;

import hackerton.educationentity.application.dto.request.CreateAssignmentRequest;
import hackerton.educationentity.application.dto.request.UpdateAssignmentRequest;
import hackerton.educationentity.application.dto.response.AssignmentResponse;
import hackerton.educationentity.application.service.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/assignments")
public class AssignmentController {
    private final AssignmentService assignmentService;

    @GetMapping
    public List<AssignmentResponse> getAssignments(@RequestParam(required = false) Long sessionId,
                                                   @RequestParam(required = false) String title) {
        return assignmentService.getAssignments(sessionId, title);
    }

    @GetMapping("/{id}")
    public AssignmentResponse getAssignment(@PathVariable Long id) {
        return assignmentService.getAssignment(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AssignmentResponse createAssignment(@RequestBody CreateAssignmentRequest request) {
        return assignmentService.createAssignment(request);
    }

    @PutMapping("/{id}")
    public AssignmentResponse updateAssignment(@PathVariable Long id, @RequestBody UpdateAssignmentRequest request) {
        return assignmentService.updateAssignment(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAssignment(@PathVariable Long id) {
        assignmentService.deleteAssignment(id);
    }
}

