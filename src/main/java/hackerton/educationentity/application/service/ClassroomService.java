package hackerton.educationentity.application.service;

import hackerton.educationentity.application.dto.request.CreateClassroomRequest;
import hackerton.educationentity.application.dto.request.UpdateClassroomRequest;
import hackerton.educationentity.application.dto.response.ClassroomResponse;
import hackerton.educationentity.domain.classroom.entity.Classroom;
import hackerton.educationentity.domain.classroom.repository.ClassroomRepository;
import hackerton.educationentity.domain.school.entity.School;
import hackerton.educationentity.domain.school.repository.SchoolRepository;
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
public class ClassroomService {
    private final ClassroomRepository classroomRepository;
    private final SchoolRepository schoolRepository;
    private final UserRepository userRepository;

    public List<ClassroomResponse> getClassrooms() {
        return classroomRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public ClassroomResponse getClassroom(Long id) {
        return toResponse(getClassroomEntity(id));
    }

    @Transactional
    public ClassroomResponse createClassroom(CreateClassroomRequest request) {
        School school = getSchool(request.schoolId());
        User teacher = getTeacher(request.teacherId());

        validateSchoolMatch(school, teacher);

        Classroom classroom = Classroom.builder()
                .school(school)
                .teacher(teacher)
                .grade(request.grade())
                .room(request.room())
                .build();

        return toResponse(classroomRepository.save(classroom));
    }

    @Transactional
    public ClassroomResponse updateClassroom(Long id, UpdateClassroomRequest request) {
        Classroom classroom = getClassroomEntity(id);
        School school = getSchool(request.schoolId());
        User teacher = getTeacher(request.teacherId());

        validateSchoolMatch(school, teacher);

        classroom.setSchool(school);
        classroom.setTeacher(teacher);
        classroom.setGrade(request.grade());
        classroom.setRoom(request.room());

        return toResponse(classroom);
    }

    @Transactional
    public void deleteClassroom(Long id) {
        Classroom classroom = getClassroomEntity(id);
        classroomRepository.delete(classroom);
    }

    private Classroom getClassroomEntity(Long id) {
        return classroomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classroom not found"));
    }

    private School getSchool(Long id) {
        return schoolRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "School not found"));
    }

    private User getTeacher(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found"));

        if (user.getRole() != UserRole.TEACHER) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is not a teacher");
        }

        return user;
    }

    private void validateSchoolMatch(School school, User teacher) {
        if (!teacher.getSchool().getId().equals(school.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Teacher school does not match classroom school");
        }
    }

    private ClassroomResponse toResponse(Classroom classroom) {
        return new ClassroomResponse(
                classroom.getId(),
                classroom.getSchool().getId(),
                classroom.getTeacher().getId(),
                classroom.getGrade(),
                classroom.getRoom()
        );
    }
}
