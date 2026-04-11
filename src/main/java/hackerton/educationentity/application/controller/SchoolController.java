package hackerton.educationentity.application.controller;

import hackerton.educationentity.application.dto.request.CreateSchoolRequest;
import hackerton.educationentity.application.dto.request.UpdateSchoolRequest;
import hackerton.educationentity.application.dto.response.SchoolResponse;
import hackerton.educationentity.application.service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schools")
public class SchoolController {
    private final SchoolService schoolService;

    @GetMapping
    public List<SchoolResponse> getSchools(@RequestParam(required = false) String name,
                                           @RequestParam(required = false) String type) {
        return schoolService.getSchools(name, type);
    }

    @GetMapping("/{id}")
    public SchoolResponse getSchool(@PathVariable Long id) {
        return schoolService.getSchool(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SchoolResponse createSchool(@RequestBody CreateSchoolRequest request) {
        return schoolService.createSchool(request);
    }

    @PutMapping("/{id}")
    public SchoolResponse updateSchool(@PathVariable Long id, @RequestBody UpdateSchoolRequest request) {
        return schoolService.updateSchool(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSchool(@PathVariable Long id) {
        schoolService.deleteSchool(id);
    }
}
