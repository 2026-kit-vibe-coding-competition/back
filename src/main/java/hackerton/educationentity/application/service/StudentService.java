package hackerton.educationentity.application.service;

import hackerton.educationentity.application.dto.request.CreateStudentRequest;
import hackerton.educationentity.application.dto.request.UpdateStudentRequest;
import hackerton.educationentity.application.dto.response.StudentResponse;
import hackerton.educationentity.application.dto.response.EvaluationResponse;
import hackerton.educationentity.domain.evaluation.repository.EvaluationRepository;
import hackerton.educationentity.domain.classroom.entity.Classroom;
import hackerton.educationentity.domain.classroom.repository.ClassroomRepository;
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
public class StudentService {
    private final StudentRepository studentRepository;
    private final ClassroomRepository classroomRepository;
    private final EvaluationRepository evaluationRepository;

    public List<StudentResponse> getStudents(Long classroomId, String name, String status) {
        return studentRepository.search(classroomId, name, status).stream()
                .map(this::toResponse)
                .toList();
    }

    public StudentResponse getStudent(Long id) {
        return toResponse(getStudentEntity(id));
    }

    public List<EvaluationResponse> getStudentEvaluations(Long id) {
        getStudentEntity(id);
        return evaluationRepository.findByStudentId(id).stream()
                .map(evaluation -> new EvaluationResponse(
                        evaluation.getId(),
                        evaluation.getSession().getId(),
                        evaluation.getStudent().getId(),
                        evaluation.getDataRef(),
                        evaluation.getMemo()
                ))
                .toList();
    }

    @Transactional
    public StudentResponse createStudent(CreateStudentRequest request) {
        Classroom classroom = getClassroom(request.classroomId());

        Student student = Student.builder()
                .classroom(classroom)
                .name(request.name())
                .phone(request.phone())
                .status(request.status())
                .build();

        return toResponse(studentRepository.save(student));
    }

    @Transactional
    public StudentResponse updateStudent(Long id, UpdateStudentRequest request) {
        Student student = getStudentEntity(id);
        Classroom classroom = getClassroom(request.classroomId());

        student.setClassroom(classroom);
        student.setName(request.name());
        student.setPhone(request.phone());
        student.setStatus(request.status());

        return toResponse(student);
    }

    @Transactional
    public void deleteStudent(Long id) {
        Student student = getStudentEntity(id);
        studentRepository.delete(student);
    }

    private Student getStudentEntity(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
    }

    private Classroom getClassroom(Long id) {
        return classroomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classroom not found"));
    }

    private StudentResponse toResponse(Student student) {
        return new StudentResponse(
                student.getId(),
                student.getClassroom().getId(),
                student.getName(),
                student.getPhone(),
                student.getStatus()
        );
    }
}
