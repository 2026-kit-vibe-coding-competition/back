package hackerton.educationentity.application.controller;

import hackerton.educationentity.application.dto.request.CreateRelationRequest;
import hackerton.educationentity.application.dto.request.UpdateRelationRequest;
import hackerton.educationentity.application.dto.response.RelationResponse;
import hackerton.educationentity.application.service.RelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/relations")
public class RelationController {
    private final RelationService relationService;

    @GetMapping
    public List<RelationResponse> getRelations(@RequestParam(required = false) Long parentId,
                                               @RequestParam(required = false) Long studentId,
                                               @RequestParam(required = false) String relation) {
        return relationService.getRelations(parentId, studentId, relation);
    }

    @GetMapping("/{id}")
    public RelationResponse getRelation(@PathVariable Long id) {
        return relationService.getRelation(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RelationResponse createRelation(@RequestBody CreateRelationRequest request) {
        return relationService.createRelation(request);
    }

    @PutMapping("/{id}")
    public RelationResponse updateRelation(@PathVariable Long id, @RequestBody UpdateRelationRequest request) {
        return relationService.updateRelation(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRelation(@PathVariable Long id) {
        relationService.deleteRelation(id);
    }
}
