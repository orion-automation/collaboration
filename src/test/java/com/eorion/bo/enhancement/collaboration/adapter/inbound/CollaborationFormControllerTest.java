package com.eorion.bo.enhancement.collaboration.adapter.inbound;

import com.eorion.bo.enhancement.collaboration.adapter.outbound.FormRepository;
import com.eorion.bo.enhancement.collaboration.domain.entity.CollaborationForm;
import com.eorion.bo.enhancement.collaboration.utils.BatchSQLExecutor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
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
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Slf4j
public class CollaborationFormControllerTest {
    @Autowired
    private FormRepository formRepository;
    @Autowired
    private IdentityService identityService;

    private final ObjectMapper mapper = new ObjectMapper();

    private final InputStreamReader formDeleteInputStreamReader = new InputStreamReader(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("sql/form/delete-all.sql")));

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected BatchSQLExecutor executor;

    private static final HttpHeaders headers = new HttpHeaders();

    static {
        headers.set("Authorization", "Basic " + Base64.encodeBase64String("demo:demo".getBytes(StandardCharsets.UTF_8)));
    }

    @BeforeEach
    public void init() throws IOException, SQLException {
        executor.batchExecuteSqlFromFile(formDeleteInputStreamReader);
        identityService.setAuthenticatedUserId("demo");
    }

    @Test
    public void createFromReturn200() throws Exception {

        var file = ResourceUtils.getFile("classpath:data/form/normal-save.json");
        var requestBody = new String(Files.readAllBytes(file.toPath()));

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/enhancement/form")
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(headers)
                                .content(requestBody)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").isString())
                .andDo(print());

    }

    @Test
    public void updateFromReturn204() throws Exception {

        var file = ResourceUtils.getFile("classpath:data/form/normal-update.json");
        var updateBody = new String(Files.readAllBytes(file.toPath()));

        var formData = List.of(Map.of("name", "name", "value", "value"));

        CollaborationForm collaborationForm = new CollaborationForm();
        collaborationForm.setName("name");
        collaborationForm.setType("type");
        collaborationForm.setTenant("tenant");
        collaborationForm.setFormData(mapper.writeValueAsString(formData));
        formRepository.save(collaborationForm);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/enhancement/form/{id}", collaborationForm.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(headers)
                                .content(updateBody)
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print());

    }

    @Test
    public void deleteFromReturn204() throws Exception {

        var formData = List.of(Map.of("name", "name", "value", "value"));
        CollaborationForm collaborationForm = new CollaborationForm();
        collaborationForm.setName("name");
        collaborationForm.setType("type");
        collaborationForm.setTenant("tenant");
        collaborationForm.setFormData(mapper.writeValueAsString(formData));
        formRepository.save(collaborationForm);
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/enhancement/form/{id}", collaborationForm.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(headers)
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print());

    }

    @Test
    public void getFromByIdReturn200() throws Exception {

        var formData = List.of(Map.of("name", "name", "value", "value"));
        CollaborationForm collaborationForm = new CollaborationForm();
        collaborationForm.setName("name");
        collaborationForm.setType("type");
        collaborationForm.setTenant("tenant");
        collaborationForm.setFormData(mapper.writeValueAsString(formData));
        formRepository.save(collaborationForm);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/enhancement/form/{id}", collaborationForm.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(headers)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

    }

    @Test
    public void getFromListReturn200() throws Exception {

        var list = Lists.list("nameA", "nameB", "nameC", "nameD", "nameE");
        var formData = List.of(Map.of("name", "name", "value", "value"));
        for (String name : list) {
            CollaborationForm collaborationForm = new CollaborationForm();
            collaborationForm.setName(name);
            collaborationForm.setType("type");
            collaborationForm.setTenant("tenant");
            collaborationForm.setFormData(mapper.writeValueAsString(formData));
            formRepository.save(collaborationForm);
        }

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/enhancement/form")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("nameLike", "name")
                                .param("tenant", "tenant")
                                .param("type", "type")
                                .param("sortBy", "name")
                                .param("sortOrder", "desc")
                                .headers(headers)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("nameE"))
                .andDo(print());

    }

    @Test
    public void getFromCountReturn200() throws Exception {

        var formData = List.of(Map.of("name", "name", "value", "value"));
        for (int i = 0; i < 5; i++) {
            CollaborationForm collaborationForm = new CollaborationForm();
            collaborationForm.setName("name");
            collaborationForm.setType("type");
            collaborationForm.setTenant("tenant");
            collaborationForm.setFormData(mapper.writeValueAsString(formData));
            formRepository.save(collaborationForm);
        }
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/enhancement/form/count")
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(headers)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(5))
                .andDo(print());

    }
}
