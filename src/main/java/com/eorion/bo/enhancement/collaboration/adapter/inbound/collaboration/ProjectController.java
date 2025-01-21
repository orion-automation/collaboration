package com.eorion.bo.enhancement.collaboration.adapter.inbound.collaboration;

import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.ProjectSaveDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.ProjectUpdateDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.CodeEffortDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.IdDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.ProjectDetailDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.ProjectListDTO;
import com.eorion.bo.enhancement.collaboration.exception.DataNotExistException;
import com.eorion.bo.enhancement.collaboration.exception.InsertFailedException;
import com.eorion.bo.enhancement.collaboration.exception.UpdateFailedException;
import com.eorion.bo.enhancement.collaboration.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enhancement/collaboration/project")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping()
    public IdDTO<?> createProject(@Validated @RequestBody ProjectSaveDTO dto) throws InsertFailedException {
        return projectService.saveProject(dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable(value = "id") Integer id) throws UpdateFailedException {
        projectService.deleteProjectById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProject(@PathVariable(value = "id") Integer id, @RequestBody ProjectUpdateDTO updateDto) throws UpdateFailedException {
        projectService.updateProject(id, updateDto);
    }

    @GetMapping("/list")
    public List<ProjectListDTO> queryProject(@RequestParam(value = "tenant") String tenant,
                                             @RequestParam(value = "coeCode", required = false) String coeCode,
                                             @RequestParam(value = "nameLike", required = false) String nameLike,
                                             @RequestParam(value = "tags", required = false) List<String> tags,
                                             @RequestParam(value = "type") String type
    ) {
        return projectService.queryProject(tenant, nameLike, coeCode, type, tags);
    }

    @GetMapping("/{id}")
    public ProjectDetailDTO getProjectById(@PathVariable Integer id) throws DataNotExistException {
        return projectService.getProjectById(id);
    }

    @GetMapping("/{projectId}/code-effort/statistics")
    public CodeEffortDTO getProcessCodeEffort(@PathVariable Integer projectId) {
        return projectService.getProcessCodeEffort(projectId);
    }
}
