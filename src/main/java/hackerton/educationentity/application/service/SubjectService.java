package hackerton.educationentity.application.service;

import hackerton.educationentity.application.dto.request.CreateSubjectRequest;
import hackerton.educationentity.application.dto.request.UpdateSubjectRequest;
import hackerton.educationentity.application.dto.response.SubjectResponse;
import hackerton.educationentity.domain.classroom.entity.Classroom;
import hackerton.educationentity.domain.classroom.repository.ClassroomRepository;
import hackerton.educationentity.domain.subject.entity.Subject;
import hackerton.educationentity.domain.subject.repository.SubjectRepository;
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
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final ClassroomRepository classroomRepository;
    private final UserRepository userRepository;

    public List<SubjectResponse> getSubjects() {
        return subjectRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public SubjectResponse getSubject(Long id) {
        return toResponse(getSubjectEntity(id));
    }

    @Transactional
    public SubjectResponse createSubject(CreateSubjectRequest request) {
        Classroom classroom = getClassroom(request.classroomId());
        User teacher = getTeacher(request.teacherId());

        validateSchoolMatch(classroom, teacher);

        Subject subject = Subject.builder()
                .classroom(classroom)
                .teacher(teacher)
                .name(request.name())
                .build();

        return toResponse(subjectRepository.save(subject));
    }

    @Transactional
    public SubjectResponse updateSubject(Long id, UpdateSubjectRequest request) {
        Subject subject = getSubjectEntity(id);
        Classroom classroom = getClassroom(request.classroomId());
        User teacher = getTeacher(request.teacherId());

        validateSchoolMatch(classroom, teacher);

        subject.setClassroom(classroom);
        subject.setTeacher(teacher);
        subject.setName(request.name());

        return toResponse(subject);
    }

    @Transactional
    public void deleteSubject(Long id) {
        Subject subject = getSubjectEntity(id);
        subjectRepository.delete(subject);
    }

    private Subject getSubjectEntity(Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject not found"));
    }

    private Classroom getClassroom(Long id) {
        return classroomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classroom not found"));
    }

    private User getTeacher(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found"));

        if (user.getRole() != UserRole.TEACHER) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is not a teacher");
        }

        return user;
    }

    private void validateSchoolMatch(Classroom classroom, User teacher) {
        if (!teacher.getSchool().getId().equals(classroom.getSchool().getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Teacher school does not match classroom school");
        }
    }

    private SubjectResponse toResponse(Subject subject) {
        return new SubjectResponse(
                subject.getId(),
                subject.getClassroom().getId(),
                subject.getTeacher().getId(),
                subject.getName()
        );
    }
}
