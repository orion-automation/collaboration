package com.eorion.bo.enhancement.collaboration.adapter.inbound;

import com.eorion.bo.enhancement.collaboration.adapter.outbound.MemberRepository;
import com.eorion.bo.enhancement.collaboration.adapter.outbound.ProjectRepository;
import com.eorion.bo.enhancement.collaboration.domain.entity.Member;
import com.eorion.bo.enhancement.collaboration.domain.entity.Project;
import com.eorion.bo.enhancement.collaboration.domain.enums.RoleStatus;
import com.eorion.bo.enhancement.collaboration.utils.BatchSQLExecutor;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.impl.digest._apacheCommonsCodec.Base64;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Slf4j
public class MemberControllerTest {
    @Autowired
    private IdentityService identityService;
    @Autowired
    private MockMvc mockMvc;

    private final InputStreamReader memberDeleteReader = new InputStreamReader(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("sql/member/delete-all.sql")));

    private final InputStreamReader projectDeleteReader = new InputStreamReader(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("sql/project/delete-all.sql")));

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MemberRepository memberRepository;


    @Autowired
    private BatchSQLExecutor executor;


    private static final String auth;

    static {
        auth = "Basic " + Base64.encodeBase64String("demo:demo".getBytes(StandardCharsets.UTF_8));
    }

    @PostConstruct
    public void init() throws IOException {
        identityService.setAuthenticatedUserId("demo");

    }

    @BeforeEach
    public void clearUp() throws SQLException {
        executor.batchExecuteSqlFromFile(memberDeleteReader);
        executor.batchExecuteSqlFromFile(projectDeleteReader);
    }

    @Test
    public void createMemberShouldReturn200() throws Exception {
        Project project = new Project();
        project.setName("name");
        project.setOwner("demo");
        projectRepository.save(project);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/enhancement/collaboration/member")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"projectId\": 1,\"userId\":\"demo\",\"role\":\"1\"}")
                                .header("Authorization", auth)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    public void deleteMemberShouldReturn204() throws Exception {

        identityService.setAuthentication("demo", List.of("ROLE_ADMIN"));
        Project project = new Project();
        project.setName("name");
        project.setOwner("demo");
        projectRepository.save(project);

        Member member = new Member();
        member.setProjectId(project.getId());
        member.setRole(RoleStatus.OWNER);
        member.setUserId("demo");
        memberRepository.save(member);

        Member member2 = new Member();
        member2.setProjectId(project.getId());
        member2.setRole(RoleStatus.EDIT);
        member2.setUserId("user");
        memberRepository.save(member2);

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/enhancement/collaboration/member")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"projectId\": 1,\"userId\":\"user\"}")
                                .header("Authorization", auth)
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @Test
    public void deleteMemberShouldReturn500() throws Exception {
        Project project = new Project();
        project.setName("name");
        project.setOwner("demo");
        projectRepository.save(project);

        Member member = new Member();
        member.setProjectId(project.getId());
        member.setRole(RoleStatus.MANGER);
        member.setUserId("demo");
        memberRepository.save(member);

        Member member2 = new Member();
        member2.setProjectId(project.getId());
        member2.setRole(RoleStatus.EDIT);
        member2.setUserId("user");
        memberRepository.save(member2);

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/enhancement/collaboration/member")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"projectId\": 1,\"userId\":\"user\"}")
                                .header("Authorization", auth)
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }
}
