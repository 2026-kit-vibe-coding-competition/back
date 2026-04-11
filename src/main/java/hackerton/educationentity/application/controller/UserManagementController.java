package hackerton.educationentity.application.controller;

import hackerton.educationentity.application.dto.request.CreateUserRequest;
import hackerton.educationentity.application.dto.request.UpdateUserRequest;
import hackerton.educationentity.application.dto.response.UserResponse;
import hackerton.educationentity.application.service.UserManagementService;
import hackerton.educationentity.domain.user.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserManagementController {
    private final UserManagementService userManagementService;

    @GetMapping
    public List<UserResponse> getUsers(@RequestParam(required = false) Long schoolId,
                                       @RequestParam(required = false) UserRole role,
                                       @RequestParam(required = false) String name,
                                       @RequestParam(required = false) String email) {
        return userManagementService.getUsers(schoolId, role, name, email);
    }

    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable Long id) {
        return userManagementService.getUser(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@RequestBody CreateUserRequest request) {
        return userManagementService.createUser(request);
    }

    @PutMapping("/{id}")
    public UserResponse updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest request) {
        return userManagementService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userManagementService.deleteUser(id);
    }
}
