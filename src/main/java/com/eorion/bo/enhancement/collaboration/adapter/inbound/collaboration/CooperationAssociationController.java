package com.eorion.bo.enhancement.collaboration.adapter.inbound.collaboration;

import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.cooperation.AssociationSaveDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.IdDTO;
import com.eorion.bo.enhancement.collaboration.domain.entity.Association;
import com.eorion.bo.enhancement.collaboration.exception.DataNotExistException;
import com.eorion.bo.enhancement.collaboration.exception.InsertFailedException;
import com.eorion.bo.enhancement.collaboration.service.AssociationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 资源关联
 */
@RestController
@RequestMapping("/enhancement/collaboration")
@RequiredArgsConstructor
public class CooperationAssociationController {

    private final AssociationService associationService;

    @PostMapping("/resource/{resourceId}/association")
    public IdDTO<Long> saveAssociation(@PathVariable Long resourceId, @RequestBody AssociationSaveDTO saveDTO) throws InsertFailedException {
        return associationService.saveAssociation(resourceId, saveDTO);
    }

    @PutMapping("/resource/association/{associationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateById(@PathVariable Long associationId, @RequestBody AssociationSaveDTO saveDTO) throws DataNotExistException, InsertFailedException {
        associationService.updateAssociationById(associationId, saveDTO);
    }

    @GetMapping("/resource/{resourceId}/association")
    public List<Association> getListForAssociation(@PathVariable Long resourceId) {
        return associationService.getListByResourceId(resourceId);
    }

    @DeleteMapping("/resource/association/{associationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeById(@PathVariable Long associationId) {
        associationService.removeAssociationById(associationId);
    }
}
