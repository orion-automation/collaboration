package com.eorion.bo.enhancement.collaboration.adapter.inbound.collaboration;

import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.MemberDeleteDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.MemberSaveDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.mapper.MemberStructureMapper;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.IdDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.outbound.MemberDetailDTO;
import com.eorion.bo.enhancement.collaboration.exception.DataNotExistException;
import com.eorion.bo.enhancement.collaboration.exception.InsertFailedException;
import com.eorion.bo.enhancement.collaboration.service.MemberService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/enhancement/collaboration/member")
public class MemberController {
    private final MemberService memberService;
    private final MemberStructureMapper mapper;

    public MemberController(MemberService memberService, MemberStructureMapper mapper) {
        this.memberService = memberService;
        this.mapper = mapper;
    }

    @PostMapping("")
    public IdDTO<Integer> saveMember(@Valid @RequestBody MemberSaveDTO dto) throws InsertFailedException {
        return memberService.saveOrUpdate(mapper.saveDtoToEntity(dto));
    }

    @DeleteMapping("")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMember(@RequestBody MemberDeleteDTO deleteDto) {
        memberService.deleteMember(deleteDto);
    }

    @GetMapping("/list/{projectId}")
    public List<MemberDetailDTO> queryMemberList(@PathVariable Integer projectId) throws DataNotExistException {
        return memberService.queryMemberList(projectId).stream().map(mapper::toMemberDetailDTO).collect(Collectors.toList());
    }
}
