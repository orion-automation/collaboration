package com.eorion.bo.enhancement.collaboration.adapter.inbound;

import com.eorion.bo.enhancement.collaboration.adapter.outbound.AssociationRepository;
import com.eorion.bo.enhancement.collaboration.adapter.outbound.ResourceRepository;
import com.eorion.bo.enhancement.collaboration.domain.entity.Association;
import com.eorion.bo.enhancement.collaboration.domain.entity.Resource;
import com.eorion.bo.enhancement.collaboration.domain.enums.ResourceAssociationType;
import com.eorion.bo.enhancement.collaboration.domain.enums.ResourceType;
import com.eorion.bo.enhancement.collaboration.utils.BatchSQLExecutor;
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

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CooperationAssociationControllerTest {
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private IdentityService identityService;

    @Autowired
    private AssociationRepository associationRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected BatchSQLExecutor executor;

    private static final HttpHeaders headers = new HttpHeaders();

    private final InputStreamReader sqlInitStreamReader = new InputStreamReader(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("sql/bpez_cooperation_res_association/delete-all.sql")));


    static {
        headers.set("Authorization", "Basic " + Base64.encodeBase64String("demo:demo".getBytes(StandardCharsets.UTF_8)));
    }

    @BeforeEach
    public void clearUp() throws SQLException, IOException {
        executor.batchExecuteSqlFromFile(sqlInitStreamReader);
        identityService.setAuthenticatedUserId("demo");
    }

    @Test
    public void createCooperationAssociationReturn200() throws Exception {

        Resource resource = new Resource();
        resource.setName("name");
        resource.setType(ResourceType.BPMN);
        resource.setProjectId(3);
        resource.setCreatedBy("test");
        resourceRepository.save(resource);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/enhancement/collaboration/resource/{resourceId}/association", resource.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(headers)
                                .content("{\"name\":\"runtimeService\",\"url\":\"https://www.google.com\",\"type\": \"1\"}")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
                .andDo(print());
    }


    @Test
    public void updateCooperationAssociationReturn204() throws Exception {

        Association association = new Association();
        association.setResourceId(12L);
        association.setName("name");
        association.setUrl("www.baidu.com");
        association.setType(ResourceAssociationType.RUNTIME);
        association.setCreatedBy("test");

        associationRepository.save(association);
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/enhancement/collaboration/resource/association/{associationId}", association.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(headers)
                                .content("{\"name\":\"runtimeService\",\"url\":\"https://www.google.com\",\"type\": \"2\"}")
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        var associationTest = associationRepository.getById(association.getId());
        assertEquals(associationTest.getName(), "runtimeService");
        assertEquals(associationTest.getUrl(), "https://www.google.com");
        assertEquals(associationTest.getType(), ResourceAssociationType.KNOWLEDGE);
    }

    @Test
    public void getListCooperationAssociationReturn200() throws Exception {

        Long resourceId = 12L;

        for (int i = 0; i < 2; i++) {
            Association association = new Association();
            association.setResourceId(resourceId);
            association.setName("name");
            association.setUrl("www.baidu.com");
            association.setType(ResourceAssociationType.RUNTIME);
            association.setCreatedBy("test");
            associationRepository.save(association);
        }

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/enhancement/collaboration/resource/{resourceId}/association", resourceId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(headers)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andDo(print());
    }

    @Test
    public void deleteCooperationAssociationReturn204() throws Exception {

        Long resourceId = 12L;

        Association association = new Association();
        association.setResourceId(resourceId);
        association.setName("name");
        association.setUrl("www.baidu.com");
        association.setType(ResourceAssociationType.RUNTIME);
        association.setCreatedBy("test");
        associationRepository.save(association);

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/enhancement/collaboration/resource/association/{associationId}", association.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(headers)
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        var associationTest = associationRepository.getById(association.getId());
        assertNull(associationTest);
    }

}
