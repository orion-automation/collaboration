package com.eorion.bo.enhancement.collaboration.adapter.inbound.pb;

import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.cooperation.ResourceDetailPasswordDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.ResourceBriefDTO;
import com.eorion.bo.enhancement.collaboration.exception.DataNotExistException;
import com.eorion.bo.enhancement.collaboration.service.ResourceDetailService;
import com.eorion.bo.enhancement.collaboration.service.ResourceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/pb/enhancement/collaboration/resource")
public class PublicResourceController {
    private final ResourceService resourceService;
    private final ResourceDetailService detailService;

    public PublicResourceController(ResourceService resourceService, ResourceDetailService detailService) {
        this.resourceService = resourceService;
        this.detailService = detailService;
    }

    @GetMapping("/{resourceId}")
    public ResourceBriefDTO getResourceById(@PathVariable Integer resourceId) throws DataNotExistException {
        return resourceService.getResourceBrieById(resourceId);
    }


    @PostMapping("/detail/{detailId}")
    public ResponseEntity<?> getResourceDetailById(@PathVariable String detailId, @RequestBody(required = false) ResourceDetailPasswordDTO passwordDTO) throws DataNotExistException, NoSuchAlgorithmException, JsonProcessingException {
        return detailService.getPublicResourceDetailById(detailId, passwordDTO);
    }
}
