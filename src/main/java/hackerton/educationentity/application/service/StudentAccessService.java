package hackerton.educationentity.application.service;

import hackerton.educationentity.application.dto.request.CreateStudentAccessRequest;
import hackerton.educationentity.application.dto.request.UpdateStudentAccessRequest;
import hackerton.educationentity.application.dto.response.StudentAccessResponse;
import hackerton.educationentity.domain.student.entity.Student;
import hackerton.educationentity.domain.student.repository.StudentRepository;
import hackerton.educationentity.domain.student_access.entity.StudentAccess;
import hackerton.educationentity.domain.student_access.repository.StudentAccessRepository;
import hackerton.educationentity.domain.student_access.type.AccessType;
import hackerton.educationentity.domain.user.entity.User;
import hackerton.educationentity.domain.user.entity.UserRole;
import hackerton.educationentity.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentAccessService {
    private final StudentAccessRepository studentAccessRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    public List<StudentAccessResponse> getStudentAccesses(Long studentId, Long teacherId, AccessType type) {
        return studentAccessRepository.search(studentId, teacherId, type).stream()
                .map(this::toResponse)
                .toList();
    }

    public StudentAccessResponse getStudentAccess(Long id) {
        return toResponse(getStudentAccessEntity(id));
    }

    @Transactional
    public StudentAccessResponse createStudentAccess(CreateStudentAccessRequest request) {
        Student student = getStudent(request.studentId());
        User teacher = getTeacher(request.teacherId());

        validateSchoolMatch(student, teacher);

        if (studentAccessRepository.existsByStudentIdAndTeacherId(student.getId(), teacher.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Student access already exists for this student and teacher");
        }

        StudentAccess studentAccess = StudentAccess.builder()
                .student(student)
                .teacher(teacher)
                .type(request.type())
                .build();

        return toResponse(studentAccessRepository.save(studentAccess));
    }

    @Transactional
    public StudentAccessResponse updateStudentAccess(Long id, UpdateStudentAccessRequest request) {
        StudentAccess studentAccess = getStudentAccessEntity(id);
        Student student = getStudent(request.studentId());
        User teacher = getTeacher(request.teacherId());

        validateSchoolMatch(student, teacher);

        if (studentAccessRepository.existsByStudentIdAndTeacherIdAndIdNot(student.getId(), teacher.getId(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Student access already exists for this student and teacher");
        }

        studentAccess.setStudent(student);
        studentAccess.setTeacher(teacher);
        studentAccess.setType(request.type());

        return toResponse(studentAccess);
    }

    @Transactional
    public void deleteStudentAccess(Long id) {
        StudentAccess studentAccess = getStudentAccessEntity(id);
        studentAccessRepository.delete(studentAccess);
    }

    private StudentAccess getStudentAccessEntity(Long id) {
        return studentAccessRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "StudentAccess not found"));
    }

    private Student getStudent(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
    }

    private User getTeacher(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found"));

        if (user.getRole() != UserRole.TEACHER) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is not a teacher");
        }

        return user;
    }

    private void validateSchoolMatch(Student student, User teacher) {
        if (!teacher.getSchool().getId().equals(student.getClassroom().getSchool().getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Teacher school does not match student school");
        }
    }

    private StudentAccessResponse toResponse(StudentAccess studentAccess) {
        return new StudentAccessResponse(
                studentAccess.getId(),
                studentAccess.getStudent().getId(),
                studentAccess.getTeacher().getId(),
                studentAccess.getType()
        );
    }
}
