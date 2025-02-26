package com.eorion.bo.enhancement.collaboration.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.eorion.bo.enhancement.collaboration.adapter.outbound.ConversationRepository;
import com.eorion.bo.enhancement.collaboration.adapter.outbound.ResourceBpmnNodeRepository;
import com.eorion.bo.enhancement.collaboration.adapter.outbound.ResourceDetailRepository;
import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.ProcessDevTimeSaveDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.cooperation.ResourceDetailPasswordDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.cooperation.ResourceDetailSaveDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.cooperation.ResourceDetailUpdateDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.mapper.ResourceDetailStructureMapper;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.*;
import com.eorion.bo.enhancement.collaboration.domain.entity.Conversation;
import com.eorion.bo.enhancement.collaboration.domain.entity.ResourceBpmnNode;
import com.eorion.bo.enhancement.collaboration.domain.entity.ResourceDetail;
import com.eorion.bo.enhancement.collaboration.domain.enums.ProcessDevTimeType;
import com.eorion.bo.enhancement.collaboration.exception.DataNotExistException;
import com.eorion.bo.enhancement.collaboration.exception.InsertFailedException;
import com.eorion.bo.enhancement.collaboration.exception.RequestParamException;
import com.eorion.bo.enhancement.collaboration.exception.ResourceConflictException;
import com.eorion.bo.enhancement.collaboration.utils.Md5Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResourceDetailService {

    private final ResourceDetailRepository detailRepository;
    private final ResourceBpmnNodeRepository nodeRepository;
    private final ConversationRepository conversationRepository;
    private final ResourceDetailStructureMapper structureMapper;


    @Transactional(rollbackFor = RuntimeException.class)
    public IdDTO<String> saveResourceDetail(Long id, ResourceDetailSaveDTO saveDTO) throws InsertFailedException {

        ResourceDetail resourceDetail = structureMapper.saveDtoToEntity(saveDTO);

        if (saveDTO.getConfigJson() != null && !saveDTO.getConfigJson().isEmpty())
            resourceDetail.setConfigJson(saveDTO.getConfigJson());

        Integer version = detailRepository.getMaxVersion(id);

        if (Objects.nonNull(version)) {
            resourceDetail.setVersion(version + 1);
        }

        try {
            resourceDetail.setResourceId(id);
            detailRepository.save(resourceDetail);
        } catch (RuntimeException e) {
            log.error("Insert resourceDetail to db error : {}", e.getMessage());
            throw new InsertFailedException("插入数据失败！");
        }

        //流程节点信息存储
        var nodeList = saveDTO.getNodes();
        List<ResourceBpmnNode> nodes = new ArrayList<>();
        if (!nodeList.isEmpty()) {
            nodeList.forEach(activityId -> {
                ResourceBpmnNode node = new ResourceBpmnNode();
                node.setResourceDetailId(resourceDetail.getId());
                node.setActivityId(activityId);
                nodes.add(node);
            });
        }
        try {
            nodeRepository.saveBatch(nodes);
        } catch (RuntimeException e) {
            log.error("Insert resourceBpmnNode to db error : {}", e.getMessage());
            throw new InsertFailedException("插入数据失败！");
        }

        return new IdDTO<>(resourceDetail.getId());
    }

    @Transactional
    public void updateResourceDetailById(String id, ResourceDetailUpdateDTO updateDTO) {

        var resourceDetail = detailRepository.getById(id);
        if (Objects.nonNull(resourceDetail)) {
            if (updateDTO.isForceSave()) {
                updatedResourceDetailById(id, updateDTO);
            } else {
                //如果用户拿到的资源和数据库中保存的资源updateTs相同则直接保存或者updateTs为空
                if (Objects.isNull(resourceDetail.getUpdatedTs()) || updateDTO.getCurrentVersionTimestamp().equals(resourceDetail.getUpdatedTs())) {
                    updatedResourceDetailById(id, updateDTO);
                } else {
                    log.warn("resource conflict with id : {}", id);
                    throw new ResourceConflictException(new ResourceDetailConflictDTO(resourceDetail.getUpdatedTs(), resourceDetail.getUpdatedBy()));
                }
            }
        }
    }

    public List<CoopResourceDetailDTO> getAllDetailByResourceId(Long resourceId) {
        var list = detailRepository.list(
                new LambdaQueryWrapper<ResourceDetail>()
                        .eq(ResourceDetail::getResourceId, resourceId)
                        .orderByDesc(ResourceDetail::getVersion)
        );
        return list.stream().map(CoopResourceDetailDTO::formDetail).collect(Collectors.toList());
    }

    public CoopResourceDetailDTO getResourceDetailById(String resourceDetailId) throws DataNotExistException {

        ResourceDetail dbResourceDetail = detailRepository.getById(resourceDetailId);
        if (Objects.nonNull(dbResourceDetail)) {
            CoopResourceDetailDTO destination = structureMapper.entityToDto(dbResourceDetail);
            destination.setConfigJson(dbResourceDetail.getConfigJson());
            return destination;
        } else {
            throw new DataNotExistException("对应资源不存在！");
        }
    }

    public void createPasswordById(String resourceDetailId, ResourceDetailPasswordDTO passwordDTO) throws NoSuchAlgorithmException, DataNotExistException {
        ResourceDetail resourceDetail = detailRepository.getById(resourceDetailId);
        if (Objects.nonNull(resourceDetail)) {
            var password = passwordDTO.getPassword();
            var encryptedPassword = Md5Utils.getMD5(password);
            UpdateWrapper<ResourceDetail> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("password_txt", encryptedPassword);
            updateWrapper.eq("id", resourceDetailId);
            detailRepository.update(updateWrapper);
        } else {
            throw new DataNotExistException("资源不存在");
        }
    }

    public void removeResourceDetailById(String resourceDetailId) {
        var detail = detailRepository.getById(resourceDetailId);
        if (Objects.nonNull(detail)) {
            detailRepository.removeById(resourceDetailId);

            var nodeIdList = nodeRepository.list(new LambdaQueryWrapper<ResourceBpmnNode>()
                            .select(ResourceBpmnNode::getId)
                            .eq(ResourceBpmnNode::getResourceDetailId, resourceDetailId))
                    .stream()
                    .map(ResourceBpmnNode::getId)
                    .collect(Collectors.toList());
            if (!nodeIdList.isEmpty()) {
                nodeRepository.removeByIds(nodeIdList);
                conversationRepository.remove(new LambdaQueryWrapper<Conversation>().in(Conversation::getNodeId, nodeIdList));
            }
        }
    }


    public ResponseEntity<?> getPublicResourceDetailById(String resourceDetailId, ResourceDetailPasswordDTO passwordDTO) throws DataNotExistException, NoSuchAlgorithmException {

        var dbResourceDetail = detailRepository.getById(resourceDetailId);
        if (Objects.nonNull(dbResourceDetail)) {
            String password = dbResourceDetail.getPassword();
            var publicResourceDetailDTO = structureMapper.entityToPublicDto(dbResourceDetail);
            publicResourceDetailDTO.setConfigJson(dbResourceDetail.getConfigJson());
            if (StringUtils.isBlank(password)) {
                return ResponseEntity.ok(publicResourceDetailDTO);
            } else if (Objects.nonNull(passwordDTO)) {
                return Md5Utils.getMD5(passwordDTO.getPassword()).equals(password) ? ResponseEntity.ok(publicResourceDetailDTO) : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

        } else {
            throw new DataNotExistException("对应资源不存在！");
        }
    }

    public void removePasswordById(String resourceDetailId) {
        var db = detailRepository.getById(resourceDetailId);
        if (Objects.nonNull(db) && StringUtils.isNotBlank(db.getPassword())) {
            UpdateWrapper<ResourceDetail> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("ID", resourceDetailId);
            updateWrapper.set("PASSWORD_TXT", null);
            detailRepository.update(updateWrapper);
        }
    }

    public void saveDevProcessTime(ProcessDevTimeSaveDTO saveDTO) throws RequestParamException, DataNotExistException {

        var from = ProcessDevTimeType.from(saveDTO.getType());
        if (Objects.nonNull(from)) {
            var db = detailRepository.getById(saveDTO.getId());
            if (Objects.nonNull(db)) {
                UpdateWrapper<ResourceDetail> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("ID", db.getId());
                updateWrapper.setSql(from.getDesc() + " = " + from.getDesc() + " + " + 30);
                detailRepository.update(updateWrapper);
            } else {
                throw new DataNotExistException();
            }
        } else {
            throw new RequestParamException("参数类型不支持！");
        }

    }

    public CodeEffortDTO getProcessCodeEffort(String resourceDetailId) throws DataNotExistException {

        var resourceDetail = detailRepository.getById(resourceDetailId);
        if (Objects.nonNull(resourceDetail)) {
            return new CodeEffortDTO(resourceDetail.getZeroCodeEffort() + resourceDetail.getLowCodeEffort() + resourceDetail.getAdvanceCodeEffort());
        } else {
            throw new DataNotExistException();
        }
    }

    public CheckPasswordDTO checkPasswordEmpty(String resourceDetailId) {
        var count = detailRepository.count(
                new LambdaQueryWrapper<ResourceDetail>()
                        .eq(ResourceDetail::getId, resourceDetailId)
                        .isNull(ResourceDetail::getPassword)
        );
        return new CheckPasswordDTO(count == 0);
    }

    public synchronized void updatedResourceDetailById(String id, ResourceDetailUpdateDTO updateDTO) {
        var updateDto = ResourceDetailUpdateDTO.toEntity(updateDTO);
        updateDto.setId(id);
        detailRepository.updateById(updateDto);
        //流程节点信息存储
        if (Objects.nonNull(updateDTO.getNodes())) {
            List<String> nodeList = updateDTO.getNodes();
            if (!nodeList.isEmpty()) {
                nodeList.forEach(activityId -> {
                    ResourceBpmnNode node = new ResourceBpmnNode();
                    node.setResourceDetailId(id);
                    UpdateWrapper<ResourceBpmnNode> updateWrapper = new UpdateWrapper<>();
                    node.setActivityId(activityId);
                    updateWrapper.eq("activity_id", activityId);
                    updateWrapper.eq("resource_detail_id", id);
                    nodeRepository.saveOrUpdate(node, updateWrapper);
                });
            }
        }
    }
}
