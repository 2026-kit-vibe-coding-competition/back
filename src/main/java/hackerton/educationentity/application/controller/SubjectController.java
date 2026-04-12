package hackerton.educationentity.application.controller;

import hackerton.educationentity.application.dto.request.CreateSubjectRequest;
import hackerton.educationentity.application.dto.request.UpdateSubjectRequest;
import hackerton.educationentity.application.dto.response.SubjectResponse;
import hackerton.educationentity.application.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/subjects")
public class SubjectController {
    private final SubjectService subjectService;

    @GetMapping
    public List<SubjectResponse> getSubjects() {
        return subjectService.getSubjects();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubjectResponse createSubject(@RequestBody CreateSubjectRequest request) {
        return subjectService.createSubject(request);
    }

    @GetMapping("/{id}")
    public SubjectResponse getSubject(@PathVariable Long id) {
        return subjectService.getSubject(id);
    }

    @PutMapping("/{id}")
    public SubjectResponse updateSubject(@PathVariable Long id, @RequestBody UpdateSubjectRequest request) {
        return subjectService.updateSubject(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);
    }
}

