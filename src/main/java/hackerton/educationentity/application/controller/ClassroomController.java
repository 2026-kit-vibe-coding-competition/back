package hackerton.educationentity.application.controller;

import hackerton.educationentity.application.dto.request.CreateClassroomRequest;
import hackerton.educationentity.application.dto.request.UpdateClassroomRequest;
import hackerton.educationentity.application.dto.response.ClassroomResponse;
import hackerton.educationentity.application.service.ClassroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/classrooms")
public class ClassroomController {
    private final ClassroomService classroomService;

    @GetMapping
    public List<ClassroomResponse> getClassrooms() {
        return classroomService.getClassrooms();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClassroomResponse createClassroom(@RequestBody CreateClassroomRequest request) {
        return classroomService.createClassroom(request);
    }

    @GetMapping("/{id}")
    public ClassroomResponse getClassroom(@PathVariable Long id) {
        return classroomService.getClassroom(id);
    }

    @PutMapping("/{id}")
    public ClassroomResponse updateClassroom(@PathVariable Long id, @RequestBody UpdateClassroomRequest request) {
        return classroomService.updateClassroom(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClassroom(@PathVariable Long id) {
        classroomService.deleteClassroom(id);
    }
}
