package hackerton.educationentity.application.service;

import hackerton.educationentity.application.dto.request.CreateRelationRequest;
import hackerton.educationentity.application.dto.request.UpdateRelationRequest;
import hackerton.educationentity.application.dto.response.RelationResponse;
import hackerton.educationentity.domain.relation.entity.Relation;
import hackerton.educationentity.domain.relation.repository.RelationRepository;
import hackerton.educationentity.domain.student.entity.Student;
import hackerton.educationentity.domain.student.repository.StudentRepository;
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
public class RelationService {
    private final RelationRepository relationRepository;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;

    public List<RelationResponse> getRelations(Long parentId, Long studentId, String relation) {
        return relationRepository.search(parentId, studentId, relation).stream()
                .map(this::toResponse)
                .toList();
    }

    public RelationResponse getRelation(Long id) {
        return toResponse(getRelationEntity(id));
    }

    @Transactional
    public RelationResponse createRelation(CreateRelationRequest request) {
        User parent = getParent(request.parentId());
        Student student = getStudent(request.studentId());

        validateSchoolMatch(parent, student);

        if (relationRepository.existsByParentIdAndStudentId(parent.getId(), student.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Relation already exists for this parent and student");
        }

        Relation relation = Relation.builder()
                .parent(parent)
                .student(student)
                .relation(request.relation())
                .build();

        return toResponse(relationRepository.save(relation));
    }

    @Transactional
    public RelationResponse updateRelation(Long id, UpdateRelationRequest request) {
        Relation relation = getRelationEntity(id);
        User parent = getParent(request.parentId());
        Student student = getStudent(request.studentId());

        validateSchoolMatch(parent, student);

        if (relationRepository.existsByParentIdAndStudentIdAndIdNot(parent.getId(), student.getId(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Relation already exists for this parent and student");
        }

        relation.setParent(parent);
        relation.setStudent(student);
        relation.setRelation(request.relation());

        return toResponse(relation);
    }

    @Transactional
    public void deleteRelation(Long id) {
        Relation relation = getRelationEntity(id);
        relationRepository.delete(relation);
    }

    private Relation getRelationEntity(Long id) {
        return relationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Relation not found"));
    }

    private User getParent(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent not found"));

        if (user.getRole() != UserRole.PARENT) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is not a parent");
        }

        return user;
    }

    private Student getStudent(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
    }

    private void validateSchoolMatch(User parent, Student student) {
        if (!parent.getSchool().getId().equals(student.getClassroom().getSchool().getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parent school does not match student school");
        }
    }

    private RelationResponse toResponse(Relation relation) {
        return new RelationResponse(
                relation.getId(),
                relation.getParent().getId(),
                relation.getStudent().getId(),
                relation.getRelation()
        );
    }
}
