package com.eorion.bo.enhancement.collaboration.adapter.inbound;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.eorion.bo.enhancement.collaboration.adapter.outbound.MemberRepository;
import com.eorion.bo.enhancement.collaboration.adapter.outbound.ProjectRepository;
import com.eorion.bo.enhancement.collaboration.domain.entity.Member;
import com.eorion.bo.enhancement.collaboration.domain.entity.Project;
import com.eorion.bo.enhancement.collaboration.domain.enums.ProjectType;
import com.eorion.bo.enhancement.collaboration.domain.enums.RoleStatus;
import com.eorion.bo.enhancement.collaboration.utils.BatchSQLExecutor;
import org.camunda.bpm.engine.impl.digest._apacheCommonsCodec.Base64;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProjectControllerTest extends BaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BatchSQLExecutor executor;

    private static final HttpHeaders headers = new HttpHeaders();

    static {
        headers.set("Authorization", "Basic " + Base64.encodeBase64String("demo:demo".getBytes(StandardCharsets.UTF_8)));
    }

    @BeforeEach
    public void clearUp() throws SQLException {
        executor.batchExecuteSqlFromFile(getProjectDeleteStreamReader());
        executor.batchExecuteSqlFromFile(getMemberDeleteStreamReader());
        executor.batchExecuteSqlFromFile(getResourceDetailDeleteStreamReader());
    }

    @Autowired
    private ProjectRepository repository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void createProjectShouldReturnId() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/enhancement/collaboration/project")
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(headers)
                                .content("""
                                        {"name":"name","tenant":"tenant","tags":"tags","coeCode":"code","type":"3","configJson": {"key": "value"}}""")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/enhancement/collaboration/project")
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(headers)
                                .content("""
                                        {"name":"name","tenant":"tenant","tags":"tags","coeCode":"code","type":"4","configJson": {"key": "value"}}""")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(2));
        var members = memberRepository.list(new LambdaQueryWrapper<Member>().eq(Member::getProjectId, 2));
        Assertions.assertEquals(1, members.size());
        Assertions.assertEquals("demo", members.get(0).getUserId());
        Assertions.assertEquals(RoleStatus.OWNER, members.get(0).getRole());
    }

    @Test
    public void deleteProjectShould204() throws Exception {
        Project project = new Project();
        project.setName("name");
        project.setOwner("demo");
        repository.save(project);
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/enhancement/collaboration/project/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(headers)
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void updatedProjectShould204() throws Exception {

        Project project = new Project();
        project.setName("name");
        project.setOwner("demo");
        repository.save(project);
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/enhancement/collaboration/project/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(headers)
                                .content("""
                                        {"name":"name2","tags":"tags","configJson": {"key": "value"}}""")
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        var updateProject = repository.getById(project.getId());
        Assertions.assertTrue(StringUtils.hasLength(updateProject.getConfigJson()));
    }

    @Test
    public void getProjectByIdShould200AndData() throws Exception {

        Project project = new Project();
        project.setName("test project");
        project.setOwner("demo");
        project.setTenant("tenant");
        project.setType(ProjectType.PROCESS_DESIGN);
        project.setTags("test1,test2");
        project.setConfigJson("{\"key\": \"value\"}");
        repository.save(project);

        Member member = new Member();
        member.setProjectId(project.getId());
        member.setRole(RoleStatus.EDIT);
        member.setUserId("demo");
        memberRepository.save(member);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/enhancement/collaboration/project/{id}", project.getId())
                                .headers(headers)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.configJson").isNotEmpty())
                .andDo(print());

    }

    @Test
    public void getProjectListShould200AndData() throws Exception {

        Project project = new Project();
        project.setName("test project");
        project.setOwner("demo");
        project.setTenant("tenant");
        project.setType(ProjectType.PROCESS_DESIGN);
        project.setTags("test1,test2");
        repository.save(project);

        Member member = new Member();
        member.setProjectId(project.getId());
        member.setRole(RoleStatus.EDIT);
        member.setUserId("demo");
        memberRepository.save(member);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/enhancement/collaboration/project/list")
                                .param("tenant", "tenant")
                                .param("type", "1")
                                .headers(headers)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].members.length()").value(1));

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/enhancement/collaboration/project/list")
                                .headers(headers)
                                .param("tenant", "tenant")
                                .param("nameLike", "test")
                                .param("type", "1")
                                .param("tags", "test1,test2")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].members.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(project.getName()));
    }

}
