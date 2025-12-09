package com.eorion.bo.enhancement.collaboration.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.eorion.bo.enhancement.collaboration.adapter.outbound.AssociationRepository;
import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.cooperation.AssociationSaveDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.mapper.AssociationStructureMapper;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.IdDTO;
import com.eorion.bo.enhancement.collaboration.domain.entity.Association;
import com.eorion.bo.enhancement.collaboration.exception.DataNotExistException;
import com.eorion.bo.enhancement.collaboration.exception.InsertFailedException;
import com.eorion.bo.enhancement.collaboration.exception.UpdateFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssociationService {

    private final AssociationRepository repository;
    private final AssociationStructureMapper structureMapper;


    public IdDTO<Long> saveAssociation(Long resourceId, AssociationSaveDTO saveDTO) throws InsertFailedException {

        //if (Objects.nonNull(resourceRepository.getById(resourceId))){
        Association association = structureMapper.saveDtoToEntity(saveDTO);
        association.setResourceId(resourceId);
        try {
            repository.save(association);
            return new IdDTO<>(association.getId());
        } catch (RuntimeException e) {
            throw new InsertFailedException("保存失败！");
        }

//        }else {
//            throw new DataNotExistException("请检查对应资源是否存在！");
//        }

    }

    public void updateAssociationById(Long associationId, AssociationSaveDTO saveDTO) throws DataNotExistException, UpdateFailedException {
        var dbAssociation = repository.getById(associationId);
        if (Objects.nonNull(dbAssociation)) {
            var destination = structureMapper.saveDtoToEntity(saveDTO);
            destination.setId(associationId);
            try {
                repository.updateById(destination);
            } catch (RuntimeException e) {
                log.error("Insert Error : {}", e.getMessage());
                throw new UpdateFailedException("更新失败！");
            }

        } else {
            throw new DataNotExistException("对应关联资源不存在！");
        }
    }

    public List<Association> getListByResourceId(Long resourceId) {
        return repository.list(
                new LambdaQueryWrapper<Association>()
                        .eq(Association::getResourceId, resourceId)
        );
    }

    public void removeAssociationById(Long associationId) {
        repository.removeById(associationId);
    }
}
