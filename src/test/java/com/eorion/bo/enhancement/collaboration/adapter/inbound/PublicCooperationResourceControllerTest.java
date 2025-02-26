package com.eorion.bo.enhancement.collaboration.adapter.inbound;

import com.eorion.bo.enhancement.collaboration.adapter.outbound.ProjectRepository;
import com.eorion.bo.enhancement.collaboration.adapter.outbound.ResourceDetailRepository;
import com.eorion.bo.enhancement.collaboration.adapter.outbound.ResourceRepository;
import com.eorion.bo.enhancement.collaboration.domain.entity.Project;
import com.eorion.bo.enhancement.collaboration.domain.entity.Resource;
import com.eorion.bo.enhancement.collaboration.domain.entity.ResourceDetail;
import com.eorion.bo.enhancement.collaboration.domain.enums.ProjectType;
import com.eorion.bo.enhancement.collaboration.domain.enums.ResourceType;
import com.eorion.bo.enhancement.collaboration.utils.BatchSQLExecutor;
import com.eorion.bo.enhancement.collaboration.utils.Md5Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.sql.SQLException;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PublicCooperationResourceControllerTest extends BaseControllerTest {
    @Autowired
    private ResourceRepository repository;

    @Autowired
    private ResourceDetailRepository detailRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BatchSQLExecutor executor;

    @BeforeEach
    public void clearUp() throws SQLException {
        executor.batchExecuteSqlFromFile(getResourceDeleteInputStreamReader());
    }

    @Test
    public void getResourceByIdShouldReturn200() throws Exception {

        Project project = new Project();
        project.setType(ProjectType.PROCESS_DESIGN);
        project.setName("projectName");
        project.setOwner("demo");
        projectRepository.save(project);

        Resource resource = new Resource();
        resource.setName("test.bpmn");
        resource.setType(ResourceType.BPMN);
        resource.setParentNode(0);
        resource.setExternalResourceId("www.test.com");
        resource.setProjectId(project.getId());
        repository.save(resource);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/pb/enhancement/collaboration/resource/1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(resource.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.projectId").value(project.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.resourceType.value").value("2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.projectType.value").value("1"));
    }

    @Test
    public void getResourceDetailByIdShouldReturn200() throws Exception {

        ResourceDetail resourceDetail = new ResourceDetail();
        resourceDetail.setResourceId(12L);
        resourceDetail.setXml("xml");
        resourceDetail.setCreatedBy("test");
        resourceDetail.setName("name");
        resourceDetail.setConfigJson("{\"key\":\"value\"}");

        detailRepository.save(resourceDetail);
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/pb/enhancement/collaboration/resource/detail/{detailId}", resourceDetail.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(resourceDetail.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.configJson").isNotEmpty())
                .andDo(print());
    }


    @Test
    public void getResourceDetailByIdShouldReturn403() throws Exception {

        ResourceDetail resourceDetail = new ResourceDetail();
        resourceDetail.setResourceId(12L);
        resourceDetail.setXml("xml");
        resourceDetail.setPassword("password");
        resourceDetail.setCreatedBy("test");
        resourceDetail.setName("name");

        detailRepository.save(resourceDetail);
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/pb/enhancement/collaboration/resource/detail/{detailId}", resourceDetail.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isForbidden());

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/pb/enhancement/collaboration/resource/detail/{detailId}", resourceDetail.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {"password": "errorPassword"}""")
                )
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void getResourceDetailByIdWithPasswordShouldReturn200() throws Exception {

        ResourceDetail resourceDetail = new ResourceDetail();
        resourceDetail.setResourceId(12L);
        resourceDetail.setXml("xml");
        resourceDetail.setPassword(Md5Utils.getMD5("password"));
        resourceDetail.setCreatedBy("test");
        resourceDetail.setName("name");

        detailRepository.save(resourceDetail);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/pb/enhancement/collaboration/resource/detail/{detailId}", resourceDetail.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {"password": "password"}""")
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
