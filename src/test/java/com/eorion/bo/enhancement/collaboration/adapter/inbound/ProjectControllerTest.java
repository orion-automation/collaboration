package com.eorion.bo.enhancement.collaboration.adapter.inbound;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.eorion.bo.enhancement.collaboration.adapter.outbound.*;
import com.eorion.bo.enhancement.collaboration.domain.entity.*;
import com.eorion.bo.enhancement.collaboration.domain.enums.ProjectType;
import com.eorion.bo.enhancement.collaboration.domain.enums.ResourceType;
import com.eorion.bo.enhancement.collaboration.domain.enums.RoleStatus;
import com.eorion.bo.enhancement.collaboration.utils.BatchSQLExecutor;
import org.camunda.bpm.engine.IdentityService;
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BatchSQLExecutor executor;


    @Autowired
    private ResourceDetailRepository detailRepository;

    @Autowired
    private ResourceBpmnNodeRepository nodeRepository;

    @Autowired
    private ResourceRepository resourceRepository;


    private final InputStreamReader projectDeleteReader = new InputStreamReader(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("sql/project/delete-all.sql")));
    private final InputStreamReader memberDeleteReader = new InputStreamReader(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("sql/member/delete-all.sql")));

    private final InputStreamReader xmlStreamReader = new InputStreamReader(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("bpmn/diagram_test.bpmn")));

    private final InputStreamReader sqlInitStreamReader = new InputStreamReader(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("sql/bpez_cooperation_res_detail/delete-all.sql")));

    private final InputStream userInfo = this.getClass().getClassLoader().getResourceAsStream("responseBody/userInfo.json");


    private static final HttpHeaders headers = new HttpHeaders();

    static {
        headers.set("Authorization", "Basic " + Base64.encodeBase64String("demo:demo".getBytes(StandardCharsets.UTF_8)));
    }

    @Autowired
    private IdentityService identityService;

    @BeforeEach
    public void clearUp() throws SQLException {
        executor.batchExecuteSqlFromFile(projectDeleteReader);
        executor.batchExecuteSqlFromFile(memberDeleteReader);
        executor.batchExecuteSqlFromFile(sqlInitStreamReader);
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
                                .content("{\"name\":\"name\",\"tenant\":\"tenant\",\"tags\":\"tags\",\"coeCode\":\"code\",\"type\":\"3\",\"configJson\": {\"key\": \"value\"}}")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/enhancement/collaboration/project")
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(headers)
                                .content("{\"name\":\"name\",\"tenant\":\"tenant\",\"tags\":\"tags\",\"coeCode\":\"code\",\"type\":\"4\",\"configJson\": {\"key\": \"value\"}}")
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
                                .content("{\"name\":\"name2\",\"tags\":\"tags\",\"configJson\": {\"key\": \"value\"}}")
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

    @Test
    public void getProjectForCodeEffortReturn200() throws Exception {

        Project project = new Project();
        project.setName("test project");
        project.setOwner("demo");
        project.setTenant("tenant");
        repository.save(project);

        Member member = new Member();
        member.setProjectId(project.getId());
        member.setRole(RoleStatus.EDIT);
        member.setUserId("demo");
        memberRepository.save(member);

        Resource resource = new Resource();
        resource.setName("test.bpmn");
        resource.setType(ResourceType.BPMN);
        resource.setParentNode(0);
        resource.setExternalResourceId("www.test.com");
        resource.setProjectId(project.getId());
        resource.setCreatedBy("test");
        resourceRepository.save(resource);

        StringBuilder stringBuilder = new StringBuilder();
        String line;

        BufferedReader br = new BufferedReader(xmlStreamReader);
        while ((line = br.readLine()) != null) {
            stringBuilder.append(line);
        }
        String xml = stringBuilder.toString();

        List<String> ids = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ResourceDetail detail = new ResourceDetail();
            detail.setName("name");
            detail.setResourceId(Long.valueOf(resource.getId()));
            detail.setVersion(1);
            detail.setXml(xml);
            detail.setZeroCodeEffort(30L);
            detail.setLowCodeEffort(30L);
            detail.setAdvanceCodeEffort(30L);
            detailRepository.save(detail);
            ids.add(detail.getId());
        }

        for (String id : ids) {
            for (int j = 0; j < 3; j++) {
                ResourceBpmnNode node = new ResourceBpmnNode();
                node.setResourceDetailId(id);
                node.setActivityId("ActivityId" + j);
                nodeRepository.save(node);
            }
        }

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/enhancement/collaboration/project/{projectId}/code-effort/statistics", project.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(headers)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.codeEffort").value(180));

    }

}
