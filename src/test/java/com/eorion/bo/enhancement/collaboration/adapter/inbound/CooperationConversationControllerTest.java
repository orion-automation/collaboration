package com.eorion.bo.enhancement.collaboration.adapter.inbound;

import com.eorion.bo.enhancement.collaboration.adapter.outbound.ConversationRepository;
import com.eorion.bo.enhancement.collaboration.adapter.outbound.ResourceBpmnNodeRepository;
import com.eorion.bo.enhancement.collaboration.domain.dto.inbound.cooperation.ConversationSaveDTO;
import com.eorion.bo.enhancement.collaboration.domain.entity.Conversation;
import com.eorion.bo.enhancement.collaboration.domain.entity.ResourceBpmnNode;
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

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CooperationConversationControllerTest {

    @Autowired
    private IdentityService identityService;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private ResourceBpmnNodeRepository nodeRepository;

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected BatchSQLExecutor executor;

    private static final HttpHeaders headers = new HttpHeaders();

    private final InputStreamReader sqlInitStreamReader = new InputStreamReader(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("sql/bpez_cooperation_node_conversation/delete-all.sql")));


    static {
        headers.set("Authorization", "Basic " + Base64.encodeBase64String("demo:demo".getBytes(StandardCharsets.UTF_8)));
    }

    @BeforeEach
    public void clearUp() throws SQLException, IOException {
        executor.batchExecuteSqlFromFile(sqlInitStreamReader);
        identityService.setAuthenticatedUserId("demo");
    }

    @Test
    public void createCooperationConversationReturn200() throws Exception {

        String resourceDetailId = UUID.randomUUID().toString().replace("-", "");

        ResourceBpmnNode resourceBpmnNode = new ResourceBpmnNode();
        resourceBpmnNode.setResourceDetailId(resourceDetailId);
        resourceBpmnNode.setActivityId("Activity_1oq0arg");

        nodeRepository.save(resourceBpmnNode);

        ConversationSaveDTO saveDTO = new ConversationSaveDTO();
        saveDTO.setMessage("hello");
        saveDTO.setActivityId("Activity_1oq0arg");

        var json = mapper.writeValueAsString(saveDTO);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/enhancement/collaboration/resource/detail/{detailId}/message", resourceDetailId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(headers)
                                .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
                .andDo(print());
    }


    @Test
    public void updateCooperationConversationReturn200() throws Exception {

        Conversation conversation = new Conversation();
        conversation.setMessage("message");
        conversation.setNodeId(12L);
        conversation.setCreatedBy("test");
        conversationRepository.save(conversation);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/enhancement/collaboration/resource/detail/message/{messageId}", conversation.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(headers)
                                .content("{\"message\": \"hello\"}")
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        conversation = conversationRepository.getById(conversation.getId());
        assertEquals(conversation.getMessage(), "hello");
    }


    @Test
    public void getCooperationConversationListReturn200() throws Exception {
        String resourceDetailId = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 2; i++) {

            ResourceBpmnNode resourceBpmnNode = new ResourceBpmnNode();
            resourceBpmnNode.setResourceDetailId(resourceDetailId);
            resourceBpmnNode.setActivityId("Activity_1oq0arg" + i);

            nodeRepository.save(resourceBpmnNode);

            for (int j = 0; j < 5; j++) {
                Conversation conversation = new Conversation();
                conversation.setMessage("message" + j);
                conversation.setNodeId(resourceBpmnNode.getId());
                conversation.setCreatedBy("test");
                conversationRepository.save(conversation);
            }

        }

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/enhancement/collaboration/resource/detail/{detailId}/message", resourceDetailId)
                                .param("activityId", "Activity_1oq0arg0")
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(headers)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(5))
                .andDo(print());
    }


    @Test
    public void deleteCooperationConversationReturn204() throws Exception {

        Conversation conversation = new Conversation();
        conversation.setMessage("message");
        conversation.setNodeId(1L);
        conversation.setCreatedBy("test");
        conversationRepository.save(conversation);

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/enhancement/collaboration/resource/detail/message/{messageId}", conversation.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(headers)
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        conversation = conversationRepository.getById(conversation.getId());
        assertNull(conversation);
    }


}
