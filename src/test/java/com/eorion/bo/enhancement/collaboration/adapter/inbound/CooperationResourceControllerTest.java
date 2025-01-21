package com.eorion.bo.enhancement.collaboration.adapter.inbound;

import com.eorion.bo.enhancement.collaboration.adapter.outbound.ProjectRepository;
import com.eorion.bo.enhancement.collaboration.adapter.outbound.ResourceBpmnNodeRepository;
import com.eorion.bo.enhancement.collaboration.adapter.outbound.ResourceDetailRepository;
import com.eorion.bo.enhancement.collaboration.adapter.outbound.ResourceRepository;
import com.eorion.bo.enhancement.collaboration.domain.entity.Project;
import com.eorion.bo.enhancement.collaboration.domain.entity.Resource;
import com.eorion.bo.enhancement.collaboration.domain.entity.ResourceBpmnNode;
import com.eorion.bo.enhancement.collaboration.domain.entity.ResourceDetail;
import com.eorion.bo.enhancement.collaboration.domain.enums.CoopResourceStatus;
import com.eorion.bo.enhancement.collaboration.domain.enums.ProjectType;
import com.eorion.bo.enhancement.collaboration.domain.enums.ResourceType;
import com.eorion.bo.enhancement.collaboration.utils.BatchSQLExecutor;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.impl.digest._apacheCommonsCodec.Base64;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CooperationResourceControllerTest {
    @Autowired
    private IdentityService identityService;
    @Autowired
    private ResourceRepository repository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BatchSQLExecutor executor;

    @Autowired
    private ResourceDetailRepository detailRepository;

    @Autowired
    private ResourceBpmnNodeRepository nodeRepository;

    private final InputStreamReader resourceDeleteReader = new InputStreamReader(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("sql/resource/delete-all.sql")));

    private final InputStreamReader xmlStreamReader = new InputStreamReader(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("bpmn/diagram_test.bpmn")));

    private final InputStreamReader sqlInitStreamReader = new InputStreamReader(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("sql/bpez_cooperation_res_detail/delete-all.sql")));

    private static final HttpHeaders headers = new HttpHeaders();

    static {
        headers.set("Authorization", "Basic " + Base64.encodeBase64String("demo:demo".getBytes(StandardCharsets.UTF_8)));
    }

    @PostConstruct
    public void init() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        identityService.setAuthenticatedUserId("demo");
    }


    @BeforeEach
    public void clearUp() throws SQLException {
        executor.batchExecuteSqlFromFile(resourceDeleteReader);
        executor.batchExecuteSqlFromFile(sqlInitStreamReader);
    }

    @Test
    public void saveResourceShouldReturnDataAnd200() throws Exception {
        var result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/enhancement/collaboration/resource")
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(headers)
                                .content("{\"projectId\": 1,\"name\": \"test.bpmn\",\"type\": \"3\",\"parentNode\": 1,\"externalResourceId\": \"www.baidu.com\",\"tags\": \"tags,cat\",\"configJson\": {\"key\": \"value\"}}")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        var map = objectMapper.readValue(result.getResponse().getContentAsString(), Map.class);
        var id = (int) map.get("id");
        var dbResource = repository.getById(id);
        assertEquals(dbResource.getTags(), "tags,cat");
        assertEquals(dbResource.getExternalResourceId(), "www.baidu.com");
        assertEquals(dbResource.getName(), "test.bpmn");
        assertNotNull(dbResource.getConfigJson());
    }

    @Test
    public void updateResourceShouldReturnDataAnd204() throws Exception {

        Resource resource = new Resource();
        resource.setProjectId(1);
        resource.setType(ResourceType.BPMN);
        resource.setName("name");
        resource.setTags("tags");

        repository.save(resource);
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/enhancement/collaboration/resource/{resourceId}", resource.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(headers)
                                .content("{\"name\": \"user.bpmn\",\"parentNode\": 1,\"projectId\": 2,\"externalResourceId\": \"www.google.com\",\"tags\": \"tags,cat\",\"configJson\": {\"key\": \"value\"}}")
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        var dbResource = repository.getById(resource.getId());
        assertEquals(dbResource.getTags(), "tags,cat");
        assertEquals(dbResource.getExternalResourceId(), "www.google.com");
        assertEquals(dbResource.getName(), "user.bpmn");
        assertNotNull(dbResource.getConfigJson());
    }

    @Test
    public void deleteResourceShouldReturn204() throws Exception {
        Resource resource = new Resource();
        resource.setProjectId(1);
        resource.setType(ResourceType.BPMN);
        resource.setName("name");
        resource.setTags("tags");
        repository.save(resource);

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/enhancement/collaboration/resource/{resourceId}", resource.getId())
                                .headers(headers)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void resourceSelectListShouldReturnDataAnd200() throws Exception {

        for (int i = 0; i < 5; i++) {
            Resource resource = new Resource();
            resource.setProjectId(2);
            resource.setParentNode(1);
            resource.setType(ResourceType.BPMN);
            resource.setName("user.bpmn");
            resource.setTags("cat,dog");
            resource.setCreatedTs(i + 1L);

            repository.save(resource);

            ResourceDetail resourceDetail = new ResourceDetail();
            resourceDetail.setResourceId(resource.getId().longValue());
            resourceDetail.setUpdatedBy("updateBy");
            resourceDetail.setUpdatedTs(1698981580507L);
            detailRepository.save(resourceDetail);
        }
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/enhancement/collaboration/resource/list")
                                .headers(headers)
                                .param("projectId", "2")
                                .param("parentNode", "1")
                                .param("nameLike", "user")
                                .param("tags", "cat,dog")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("user.bpmn"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].projectId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].detailLatestUpdatedBy").value("updateBy"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(5));
        //.andExpect(MockMvcResultMatchers.jsonPath("$[0].detailLatestUpdatedTs").value(1698981580507L));

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/enhancement/collaboration/resource/list")
                                .headers(headers)
                                .param("projectId", "2")
                                .param("parentNode", "1")
                                .param("nameLike", "user")
                                .param("tags", "tags")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(0));

    }


    @Test
    public void resourceSelectListCanWithOutProjectIdShouldReturnDataAnd200() throws Exception {

        Project project = new Project();
        project.setType(ProjectType.PROCESS_ATTACHMENTS);
        project.setName("name");
        project.setOwner("owner");
        projectRepository.save(project);

        Resource resource = new Resource();
        resource.setProjectId(project.getId());
        resource.setParentNode(1);
        resource.setType(ResourceType.BPMN);
        resource.setName("user.bpmn");
        resource.setTags("cat,dog");

        repository.save(resource);

        ResourceDetail resourceDetail = new ResourceDetail();
        resourceDetail.setResourceId(resource.getId().longValue());
        resourceDetail.setUpdatedBy("updateBy");
        resourceDetail.setUpdatedTs(System.currentTimeMillis());
        detailRepository.save(resourceDetail);
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/enhancement/collaboration/resource")
                                .headers(headers)
                                .param("projectId", project.getId().toString())
                                .param("parentNode", "1")
                                .param("nameLike", "user")
                                .param("tags", "cat,dog")
                                .param("typesIn", "1,2,3")
                                .param("projectTypesIn", "3")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("user.bpmn"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].projectId").value(project.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].detailLatestUpdatedBy").value(resourceDetail.getUpdatedBy()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].detailLatestUpdatedTs").value(resourceDetail.getUpdatedTs()));

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/enhancement/collaboration/resource")
                                .headers(headers)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1));

    }

    @Test
    public void getResourceByIdShouldReturn200() throws Exception {

        Resource resource = new Resource();
        resource.setName("test.bpmn");
        resource.setType(ResourceType.BPMN);
        resource.setParentNode(0);
        resource.setExternalResourceId("www.test.com");
        resource.setProjectId(1);
        resource.setCreatedBy("test");
        resource.setTags("tags");
        resource.setConfigJson("{\"key\": \"value\"}");
        repository.save(resource);


        mockMvc.perform(
                        MockMvcRequestBuilders.get("/enhancement/collaboration/resource/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(headers)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(resource.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.externalResourceId").value(resource.getExternalResourceId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.projectId").value(resource.getProjectId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.configJson").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.tags").value(resource.getTags()))
                .andDo(print());
    }

    @Test
    public void getResourceForCodeEffortReturn200() throws Exception {
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
            detail.setResourceId(34L);
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
                        MockMvcRequestBuilders.get("/enhancement/collaboration/resource/{resourceId}/code-effort/statistics", 34)
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(headers)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.codeEffort").value(180));

    }

    @Test
    public void cooperationResourcePublishReturn204() throws Exception {

        Resource resource = new Resource();
        resource.setName("name");
        resource.setProjectId(3);
        resource.setType(ResourceType.BPMN);
        repository.save(resource);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/enhancement/collaboration/resource/{id}/publish", resource.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(headers)
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        var dbApplication = repository.getById(1);
        assertEquals(dbApplication.getStatus(), CoopResourceStatus.PUBLISH);
    }

    @Test
    public void cooperationResourceTakeDownReturn204() throws Exception {

        Resource resource = new Resource();
        resource.setProjectId(3);
        resource.setName("name");
        resource.setType(ResourceType.BPMN);
        resource.setStatus(CoopResourceStatus.PUBLISH);
        repository.save(resource);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/enhancement/collaboration/resource/{id}/off", 1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(headers)
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        var dbApplication = repository.getById(1);
        assertEquals(dbApplication.getStatus(), CoopResourceStatus.DRAFT);
    }

    @Test
    public void cooperationResourceTagsUpdateReturn204() throws Exception {

        Resource resource = new Resource();
        resource.setProjectId(3);
        resource.setName("name");
        resource.setTags("tags");
        resource.setType(ResourceType.BPMN);
        resource.setStatus(CoopResourceStatus.PUBLISH);
        repository.save(resource);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/enhancement/collaboration/resource/{resourceId}/tags", 1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(headers)
                                .content("{\"tags\": \"tags,tags\"}")
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        var dbApplication = repository.getById(1);
        assertEquals(dbApplication.getTags(), "tags,tags");
    }

    @Test
    public void resourceStatisticsShouldReturnDataAnd200() throws Exception {

        Resource resource = new Resource();
        resource.setProjectId(2);
        resource.setParentNode(1);
        resource.setType(ResourceType.BPMN);
        resource.setName("user.bpmn");
        resource.setTags("cat,dog");
        repository.save(resource);

        resource.setId(null);
        resource.setType(ResourceType.FORM);
        repository.save(resource);

        resource.setId(null);
        resource.setType(ResourceType.DMN);
        repository.save(resource);

        resource.setId(null);
        resource.setType(ResourceType.PAGE);
        repository.save(resource);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/enhancement/collaboration/resource/statistics")
                                .headers(headers)
                                .param("projectId", "2")
                                .param("parentNode", "1")
                                .param("nameLike", "user")
                                .param("tags", "cat,dog")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(4))
                .andDo(print());


    }
}
