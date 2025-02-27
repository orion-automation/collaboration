package com.eorion.bo.enhancement.collaboration.adapter.inbound;

import com.eorion.bo.enhancement.collaboration.adapter.outbound.ResourceBpmnNodeRepository;
import com.eorion.bo.enhancement.collaboration.adapter.outbound.ResourceDetailRepository;
import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.ProcessDevTimeSaveDTO;
import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.cooperation.ResourceDetailUpdateDTO;
import com.eorion.bo.enhancement.collaboration.domain.entity.ResourceBpmnNode;
import com.eorion.bo.enhancement.collaboration.domain.entity.ResourceDetail;
import com.eorion.bo.enhancement.collaboration.utils.BatchSQLExecutor;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CooperationResourceDetailControllerTest extends BaseControllerTest {

    @Autowired
    private IdentityService identityService;

    @Autowired
    private ResourceDetailRepository detailRepository;

    @Autowired
    private ResourceBpmnNodeRepository nodeRepository;

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected BatchSQLExecutor executor;

    private static final HttpHeaders headers = new HttpHeaders();

    private final InputStream xmlInputStream = Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("bpmn/diagram_test.bpmn"));

    static {
        headers.set("Authorization", "Basic " + Base64.encodeBase64String("demo:demo".getBytes(StandardCharsets.UTF_8)));
    }

    @BeforeEach
    public void clearUp() throws SQLException {
        executor.batchExecuteSqlFromFile(getResourceDetailDeleteStreamReader());
        identityService.setAuthenticatedUserId("demo");
    }

    @Test
    public void createCooperationResourceDetailReturn200() throws Exception {
        Long resourceId = 34L;
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/enhancement/collaboration/resource/{resourceId}/detail", resourceId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(headers)
                                .content("""
                                        {"name":"name","xml":"xml","configJson":{"key2":"value2","key1":"value1"},"nodes":["Flow_1l3tmoc","Flow_0und6nq","Activity_1km1o7h"]}
                                        """)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").isString())
                .andDo(print());


        mockMvc.perform(
                        MockMvcRequestBuilders.post("/enhancement/collaboration/resource/{resourceId}/detail", resourceId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(headers)
                                .content("""
                                        {"name":"name","xml":"xml","configJson":{"key2":"value2","key1":"value1"},"nodes":["Flow_1l3tmoc","Flow_0und6nq","Activity_1km1o7h"]}
                                        """)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

        var maxVersion = detailRepository.getMaxVersion(resourceId);
        assertEquals(2, maxVersion);
    }


    @Test
    public void updateCooperationResourceDetailReturn204() throws Exception {
        String xml = new String(xmlInputStream.readAllBytes());
        ResourceDetail detail = new ResourceDetail();
        detail.setName("name");
        detail.setResourceId(34L);
        detail.setXml(xml);

        detailRepository.save(detail);

        ObjectMapper objectMapper = new ObjectMapper();

        var dbDetail = detailRepository.getById(detail.getId());

        ResourceDetailUpdateDTO updateDTO = new ResourceDetailUpdateDTO();
        updateDTO.setName("name2");
        updateDTO.setXml("xml");
        updateDTO.setNodes(List.of("aaa", "bbb", "ccc"));
        updateDTO.setCurrentVersionTimestamp(dbDetail.getUpdatedTs());

        ResourceBpmnNode node = new ResourceBpmnNode();
        node.setResourceDetailId(detail.getId());
        node.setActivityId("ActivityId");
        nodeRepository.save(node);
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/enhancement/collaboration/resource/detail/{id}", detail.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(headers)
                                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print());
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/enhancement/collaboration/resource/detail/{id}", detail.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(headers)
                                .content("""
                                        {"name":"name2"}""")
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("currentVersionTimestamp not be empty !"))
                .andDo(print());

        updateDTO.setForceSave(true);
        updateDTO.setName("name3");
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/enhancement/collaboration/resource/detail/{id}", detail.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(headers)
                                .content(objectMapper.writeValueAsString(updateDTO))
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        var byId = detailRepository.getById(detail.getId());

        assertEquals(byId.getName(), updateDTO.getName());

    }

    @Test
    public void getResourceDetailListByResourceIdReturn200() throws Exception {
        String xml = new String(xmlInputStream.readAllBytes());

        for (int i = 0; i < 3; i++) {
            ResourceDetail detail = new ResourceDetail();
            detail.setName("name" + i);
            detail.setResourceId(34L);
            detail.setVersion(i + 1);
            detail.setXml(xml);
            detail.setConfigJson("{\"key\":\"value\"}");
            detailRepository.save(detail);

            for (int j = 0; j < 3; j++) {
                ResourceBpmnNode node = new ResourceBpmnNode();
                node.setResourceDetailId(detail.getId());
                node.setActivityId("ActivityId" + j);
                nodeRepository.save(node);
            }
        }

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/enhancement/collaboration/resource/{resourceId}/detail", 34)
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(headers)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].configJson").isNotEmpty())
                .andDo(print());

    }

    @Test
    public void getResourceDetailByDetailIdReturn200() throws Exception {
        String xml = new String(xmlInputStream.readAllBytes());

        var detail = new ResourceDetail();
        detail.setName("name");
        detail.setResourceId(34L);
        detail.setVersion(1);
        detail.setXml(xml);
        detail.setConfigJson("""
                {
                  "size": 0,
                  "query": {
                    "range": {
                      "startTime": {
                        "gte": "2018-01-01T00:00:00",
                        "lte": "2023-12-31T23:59:59"
                      }
                    }
                  },
                  "aggs": {
                    "monthly_groups": {
                      "date_histogram": {
                        "field": "startTime",
                        "calendar_interval": "month",
                        "format": "yyyy-MM"
                      }
                    }
                  }
                }""");
        detailRepository.save(detail);

        for (int j = 0; j < 3; j++) {
            ResourceBpmnNode node = new ResourceBpmnNode();
            node.setResourceDetailId(detail.getId());
            node.setActivityId("ActivityId" + j);
            nodeRepository.save(node);
        }

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/enhancement/collaboration/resource/detail/{resourceDetailId}", detail.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(headers)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(detail.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.version").value(1))
                .andDo(print());

    }

    @Test
    public void createPasswordForResourceDetailByDetailIdReturn200() throws Exception {
        String xml = new String(xmlInputStream.readAllBytes());

        ResourceDetail detail = new ResourceDetail();
        detail.setName("name");
        detail.setResourceId(34L);
        detail.setVersion(1);
        detail.setXml(xml);
        detailRepository.save(detail);

        for (int j = 0; j < 3; j++) {
            ResourceBpmnNode node = new ResourceBpmnNode();
            node.setResourceDetailId(detail.getId());
            node.setActivityId("ActivityId" + j);
            nodeRepository.save(node);
        }

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/enhancement/collaboration/resource/detail/{resourceDetailId}", detail.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(headers)
                                .content("""
                                        {"password" : "password"}""")
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void deleteResourceDetailByDetailIdReturn200() throws Exception {
        String xml = new String(xmlInputStream.readAllBytes());

        ResourceDetail detail = new ResourceDetail();
        detail.setName("name");
        detail.setResourceId(34L);
        detail.setVersion(1);
        detail.setXml(xml);
        detailRepository.save(detail);

        for (int j = 0; j < 3; j++) {
            ResourceBpmnNode node = new ResourceBpmnNode();
            node.setResourceDetailId(detail.getId());
            node.setActivityId("ActivityId" + j);
            nodeRepository.save(node);
        }

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/enhancement/collaboration/resource/detail/{resourceDetailId}", detail.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(headers)
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @Test
    public void removePasswordForResourceDetailByDetailIdReturn204() throws Exception {
        String xml = new String(xmlInputStream.readAllBytes());

        ResourceDetail detail = new ResourceDetail();
        detail.setName("name");
        detail.setResourceId(34L);
        detail.setVersion(1);
        detail.setXml(xml);
        detailRepository.save(detail);

        for (int j = 0; j < 3; j++) {
            ResourceBpmnNode node = new ResourceBpmnNode();
            node.setResourceDetailId(detail.getId());
            node.setActivityId("ActivityId" + j);
            nodeRepository.save(node);
        }

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/enhancement/collaboration/resource/detail/{resourceDetailId}", detail.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(headers)
                                .content("""
                                        {"password" : "password"}""")
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/enhancement/collaboration/resource/detail/{resourceDetailId}/password/remove", detail.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(headers)
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void saveResourceDetailForCodeEffortReturn204() throws Exception {
        String xml = new String(xmlInputStream.readAllBytes());

        ResourceDetail detail = new ResourceDetail();
        detail.setName("name");
        detail.setResourceId(34L);
        detail.setVersion(1);
        detail.setXml(xml);
        detailRepository.save(detail);

        for (int j = 0; j < 3; j++) {
            ResourceBpmnNode node = new ResourceBpmnNode();
            node.setResourceDetailId(detail.getId());
            node.setActivityId("ActivityId" + j);
            nodeRepository.save(node);
        }

        ProcessDevTimeSaveDTO saveDTO = new ProcessDevTimeSaveDTO();
        saveDTO.setId(detail.getId());
        saveDTO.setType("zero");

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/enhancement/collaboration/resource/detail/code-effort")
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(headers)
                                .content(mapper.writeValueAsString(saveDTO))
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        assertEquals(30, detailRepository.getById(detail.getId()).getZeroCodeEffort());
    }

    @Test
    public void getResourceDetailForCodeEffortReturn200() throws Exception {
        String xml = new String(xmlInputStream.readAllBytes());

        ResourceDetail detail = new ResourceDetail();
        detail.setName("name");
        detail.setResourceId(34L);
        detail.setVersion(1);
        detail.setXml(xml);
        detail.setZeroCodeEffort(30L);
        detail.setLowCodeEffort(30L);
        detail.setAdvanceCodeEffort(30L);
        detailRepository.save(detail);

        for (int j = 0; j < 3; j++) {
            ResourceBpmnNode node = new ResourceBpmnNode();
            node.setResourceDetailId(detail.getId());
            node.setActivityId("ActivityId" + j);
            nodeRepository.save(node);
        }

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/enhancement/collaboration/resource/detail/{resourceDetailId}/code-effort/statistics", detail.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(headers)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.codeEffort").value(90));

    }

    @Test
    public void checkPasswordForResourceDetailByDetailIdReturn200() throws Exception {
        String xml = new String(xmlInputStream.readAllBytes());

        ResourceDetail detail = new ResourceDetail();
        detail.setName("name");
        detail.setResourceId(34L);
        detail.setVersion(1);
        detail.setXml(xml);
        detail.setPassword("password");
        detailRepository.save(detail);

        for (int j = 0; j < 3; j++) {
            ResourceBpmnNode node = new ResourceBpmnNode();
            node.setResourceDetailId(detail.getId());
            node.setActivityId("ActivityId" + j);
            nodeRepository.save(node);
        }

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/enhancement/collaboration/resource/detail/checkPassword")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("resourceDetailId", detail.getId())
                                .headers(headers)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.hasPassword").value(true));
    }

}
