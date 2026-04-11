package hackerton.educationentity.application.service;

import hackerton.educationentity.application.dto.request.CreateSchoolRequest;
import hackerton.educationentity.application.dto.request.UpdateSchoolRequest;
import hackerton.educationentity.application.dto.response.SchoolResponse;
import hackerton.educationentity.domain.school.entity.School;
import hackerton.educationentity.domain.school.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SchoolService {
    private final SchoolRepository schoolRepository;

    public List<SchoolResponse> getSchools(String name, String type) {
        return schoolRepository.search(name, type).stream()
                .map(this::toResponse)
                .toList();
    }

    public SchoolResponse getSchool(Long id) {
        return toResponse(getSchoolEntity(id));
    }

    @Transactional
    public SchoolResponse createSchool(CreateSchoolRequest request) {
        School school = School.builder()
                .name(request.name())
                .address(request.address())
                .phone(request.phone())
                .type(request.type())
                .build();

        return toResponse(schoolRepository.save(school));
    }

    @Transactional
    public SchoolResponse updateSchool(Long id, UpdateSchoolRequest request) {
        School school = getSchoolEntity(id);

        school.setName(request.name());
        school.setAddress(request.address());
        school.setPhone(request.phone());
        school.setType(request.type());

        return toResponse(school);
    }

    @Transactional
    public void deleteSchool(Long id) {
        School school = getSchoolEntity(id);
        schoolRepository.delete(school);
    }

    private School getSchoolEntity(Long id) {
        return schoolRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "School not found"));
    }

    private SchoolResponse toResponse(School school) {
        return new SchoolResponse(
                school.getId(),
                school.getName(),
                school.getAddress(),
                school.getPhone(),
                school.getType()
        );
    }
}
