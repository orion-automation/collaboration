package com.eorion.bo.enhancement.collaboration.adapter.inbound.collaboration;

import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.ProcessDevTimeSaveDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.cooperation.ResourceDetailPasswordDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.cooperation.ResourceDetailSaveDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.cooperation.ResourceDetailUpdateDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.CheckPasswordDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.CodeEffortDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.CoopResourceDetailDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.IdDTO;
import com.eorion.bo.enhancement.collaboration.exception.DataNotExistException;
import com.eorion.bo.enhancement.collaboration.exception.InsertFailedException;
import com.eorion.bo.enhancement.collaboration.exception.RequestParamException;
import com.eorion.bo.enhancement.collaboration.service.ResourceDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/enhancement/collaboration")
@RequiredArgsConstructor
public class CooperationResourceDetailController {

    private final ResourceDetailService detailService;

    /**
     * 针对某个协同资源下的不同版本xml存储
     *
     * @param resourceId 对应协同资源id
     * @param saveDTO
     * @return
     */
    @PostMapping("/resource/{resourceId}/detail")
    public IdDTO<String> create(@PathVariable Long resourceId, @RequestBody ResourceDetailSaveDTO saveDTO) throws InsertFailedException {
        return detailService.saveResourceDetail(resourceId, saveDTO);
    }

    /**
     * 通过 resourceDetailId 更新
     */
    @PutMapping("/resource/detail/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateResourceDetailById(@PathVariable String id, @Validated @RequestBody ResourceDetailUpdateDTO saveDTO) {
        detailService.updateResourceDetailById(id, saveDTO);
    }

    /**
     * 查询通过某个协同资源的resourceId，查询所有的版本的resourceDetail
     *
     * @return
     */
    @GetMapping("/resource/{resourceId}/detail")
    public List<CoopResourceDetailDTO> getAllDetailByResourceId(@PathVariable Long resourceId) {
        return detailService.getAllDetailByResourceId(resourceId);
    }


    /**
     * 通过DetailId获取资源的Detail
     *
     * @return
     */
    @GetMapping("/resource/detail/{resourceDetailId}")
    public CoopResourceDetailDTO getResourceDetailById(@PathVariable String resourceDetailId) throws DataNotExistException {
        return detailService.getResourceDetailById(resourceDetailId);
    }

    /**
     * 通过资源的Detail 的id 创建公开访问的密码
     * POST /collaboration/resource/detail/{id}
     */
    @PostMapping("/resource/detail/{resourceDetailId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createPasswordById(@PathVariable String resourceDetailId, @RequestBody ResourceDetailPasswordDTO passwordDTO) throws DataNotExistException, NoSuchAlgorithmException {
        detailService.createPasswordById(resourceDetailId, passwordDTO);
    }

    /**
     * 通过资源的Detail 的id 移除公开访问的密码
     */

    @PostMapping("/resource/detail/{resourceDetailId}/password/remove")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createPasswordById(@PathVariable String resourceDetailId) {
        detailService.removePasswordById(resourceDetailId);
    }

    /**
     * 通过资源的Detail 的id删除资源，将对应的所有资源删除
     */
    @DeleteMapping("/resource/detail/{resourceDetailId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeResourceDetailById(@PathVariable String resourceDetailId) {
        detailService.removeResourceDetailById(resourceDetailId);
    }


    @PostMapping("/resource/detail/code-effort")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void saveDevProcessTime(@RequestBody ProcessDevTimeSaveDTO saveDTO) throws RequestParamException, DataNotExistException {
        detailService.saveDevProcessTime(saveDTO);
    }

    @GetMapping("/resource/detail/{resourceDetailId}/code-effort/statistics")
    public CodeEffortDTO getProcessCodeEffort(@PathVariable String resourceDetailId) throws DataNotExistException {
        return detailService.getProcessCodeEffort(resourceDetailId);
    }

    /**
     * 查询当前记录是否设置密码
     */
    @GetMapping("/resource/detail/checkPassword")
    public CheckPasswordDTO checkPasswordEmpty(@RequestParam("resourceDetailId") String resourceDetailId) {
        return detailService.checkPasswordEmpty(resourceDetailId);
    }

}
