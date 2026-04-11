package hackerton.educationentity.application.controller;

import hackerton.educationentity.application.dto.request.CreateStudentAccessRequest;
import hackerton.educationentity.application.dto.request.UpdateStudentAccessRequest;
import hackerton.educationentity.application.dto.response.StudentAccessResponse;
import hackerton.educationentity.application.service.StudentAccessService;
import hackerton.educationentity.domain.student_access.type.AccessType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/student-accesses")
public class StudentAccessController {
    private final StudentAccessService studentAccessService;

    @GetMapping
    public List<StudentAccessResponse> getStudentAccesses(@RequestParam(required = false) Long studentId,
                                                          @RequestParam(required = false) Long teacherId,
                                                          @RequestParam(required = false) AccessType type) {
        return studentAccessService.getStudentAccesses(studentId, teacherId, type);
    }

    @GetMapping("/{id}")
    public StudentAccessResponse getStudentAccess(@PathVariable Long id) {
        return studentAccessService.getStudentAccess(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudentAccessResponse createStudentAccess(@RequestBody CreateStudentAccessRequest request) {
        return studentAccessService.createStudentAccess(request);
    }

    @PutMapping("/{id}")
    public StudentAccessResponse updateStudentAccess(@PathVariable Long id, @RequestBody UpdateStudentAccessRequest request) {
        return studentAccessService.updateStudentAccess(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudentAccess(@PathVariable Long id) {
        studentAccessService.deleteStudentAccess(id);
    }
}
