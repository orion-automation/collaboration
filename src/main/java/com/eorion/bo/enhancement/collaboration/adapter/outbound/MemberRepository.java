package com.eorion.bo.enhancement.collaboration.adapter.outbound;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eorion.bo.enhancement.collaboration.domain.entity.Member;
import com.eorion.bo.enhancement.collaboration.mapper.MemberMapper;
import org.springframework.stereotype.Service;

@Service
public class MemberRepository extends ServiceImpl<MemberMapper, Member> {
}
