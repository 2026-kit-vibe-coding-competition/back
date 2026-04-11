package hackerton.educationentity.application.controller;

import hackerton.educationentity.application.dto.response.EvaluationResponse;
import hackerton.educationentity.application.dto.request.CreateStudentRequest;
import hackerton.educationentity.application.dto.request.UpdateStudentRequest;
import hackerton.educationentity.application.dto.response.StudentResponse;
import hackerton.educationentity.application.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;

    @GetMapping
    public List<StudentResponse> getStudents(@RequestParam(required = false) Long classroomId,
                                             @RequestParam(required = false) String name,
                                             @RequestParam(required = false) String status) {
        return studentService.getStudents(classroomId, name, status);
    }

    @GetMapping("/{id}")
    public StudentResponse getStudent(@PathVariable Long id) {
        return studentService.getStudent(id);
    }

    @GetMapping("/{id}/evaluations")
    public List<EvaluationResponse> getStudentEvaluations(@PathVariable Long id) {
        return studentService.getStudentEvaluations(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudentResponse createStudent(@RequestBody CreateStudentRequest request) {
        return studentService.createStudent(request);
    }

    @PutMapping("/{id}")
    public StudentResponse updateStudent(@PathVariable Long id, @RequestBody UpdateStudentRequest request) {
        return studentService.updateStudent(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }
}
