package com.eorion.bo.enhancement.collaboration.adapter.inbound.collaboration;

import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.ResourceSaveDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.ResourceUpdatedDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.cooperation.CoopResourceTagsDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.*;
import com.eorion.bo.enhancement.collaboration.exception.DataNotExistException;
import com.eorion.bo.enhancement.collaboration.exception.IllegalParameterException;
import com.eorion.bo.enhancement.collaboration.exception.InsertFailedException;
import com.eorion.bo.enhancement.collaboration.exception.UpdateFailedException;
import com.eorion.bo.enhancement.collaboration.service.ResourceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enhancement/collaboration/resource")
public class CooperationResourceController {

    private final ResourceService resourceService;

    public CooperationResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @PostMapping("")
    public IdDTO<?> saveResource(@Valid @RequestBody ResourceSaveDTO resource) throws InsertFailedException {
        return resourceService.saveResource(resource);
    }

    @GetMapping("/list")
    public List<ResourceDetailDTO> getResourceList(@RequestParam(value = "projectId") Integer projectId,
                                                   @RequestParam(value = "nameLike", required = false) String nameLike,
                                                   @RequestParam(value = "parentNode", required = false) Integer parentNode,
                                                   @RequestParam(value = "status", required = false) String status,
                                                   @RequestParam(value = "tags", required = false) String tags,
                                                   @RequestParam(value = "type", required = false) String type
    ) throws IllegalParameterException {
        return resourceService.getResourceList(projectId, nameLike, parentNode, status, tags, type);
    }

    @GetMapping("")
    public List<ResourceDetailDTO> selectResourceList(@RequestParam(value = "projectId", required = false) Integer projectId,
                                                      @RequestParam(value = "nameLike", required = false) String nameLike,
                                                      @RequestParam(value = "parentNode", required = false) Integer parentNode,
                                                      @RequestParam(value = "status", required = false) String status,
                                                      @RequestParam(value = "tags", required = false) String tags,
                                                      @RequestParam(value = "typesIn", required = false) String typesIn,
                                                      @RequestParam(value = "projectTypesIn", required = false) String projectTypesIn
    ) throws IllegalParameterException {
        return resourceService.selectResourceList(projectId, nameLike, parentNode, status, tags, typesIn, projectTypesIn);
    }

    @PutMapping("/{resourceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatedResource(@PathVariable Integer resourceId, @RequestBody ResourceUpdatedDTO updatedDto) throws UpdateFailedException, DataNotExistException {
        resourceService.updatedResource(resourceId, updatedDto);
    }

    @DeleteMapping("/{resourceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteResource(@PathVariable Integer resourceId) throws DataNotExistException {
        resourceService.deleteResource(resourceId);
    }

    @GetMapping("/{resourceId}")
    public CooperationResourceDetailDTO getResourceById(@PathVariable Integer resourceId) throws DataNotExistException {
        return resourceService.getResourceById(resourceId);
    }

    @GetMapping("/statistics")
    public List<CooperationResourceTypeStatisticsDTO> getResourceStatistics(@RequestParam(value = "projectId") Integer projectId,
                                                                            @RequestParam(value = "nameLike", required = false) String nameLike,
                                                                            @RequestParam(value = "parentNode", required = false) Integer parentNode,
                                                                            @RequestParam(value = "status", required = false) String status,
                                                                            @RequestParam(value = "tags", required = false) String tags,
                                                                            @RequestParam(value = "type", required = false) String type
    ) throws IllegalParameterException {
        return resourceService.getResourceStatistics(projectId, nameLike, parentNode, status, tags, type);
    }

    @GetMapping("/{resourceId}/code-effort/statistics")
    public CodeEffortDTO getProcessCodeEffort(@PathVariable Integer resourceId) {
        return resourceService.getProcessCodeEffort(resourceId);
    }

    /**
     * 修改应用状态，0 -> 1
     */
    @PutMapping("/{resourceId}/publish")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void coopResourcePublish(@PathVariable Long resourceId) throws DataNotExistException, UpdateFailedException {
        resourceService.coopResourcePublish(resourceId);
    }

    /**
     * 修改应用状态，1 -> 0
     */
    @PutMapping("/{resourceId}/off")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void coopResourceTakeDown(@PathVariable Long resourceId) throws DataNotExistException, UpdateFailedException {
        resourceService.coopResourceTakeDown(resourceId);
    }


    /**
     * 修改标签
     */
    @PutMapping("/{resourceId}/tags")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void coopResourceTagsUpdate(@PathVariable Long resourceId, @RequestBody CoopResourceTagsDTO tags) throws DataNotExistException, IllegalParameterException {
        resourceService.coopResourceTagsUpdate(resourceId, tags);
    }
}
