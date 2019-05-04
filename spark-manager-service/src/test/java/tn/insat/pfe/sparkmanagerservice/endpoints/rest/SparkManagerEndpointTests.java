package tn.insat.pfe.sparkmanagerservice.endpoints.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import tn.insat.pfe.sparkmanagerservice.dtos.SubmitJobDto;
import tn.insat.pfe.sparkmanagerservice.services.ISparkManagerService;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(SparkManagerEndpoint.class)
public class SparkManagerEndpointTests {

    private static ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ISparkManagerService sparkManagerService;

    @BeforeClass
    public static void init() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void submitJobTest() throws Exception {
        SubmitJobDto submitJobDto = new SubmitJobDto(10,20,30);
        given(this.sparkManagerService.submitJob(submitJobDto)).willReturn(true);
        this.mvc.perform(
                post("/v1/spark-manager/func/submitJob").contentType(MediaType.APPLICATION_JSON_UTF8).content(
                        objectMapper.writeValueAsString(submitJobDto)
                ))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void submitJobUnavailableTest() throws Exception {
        SubmitJobDto submitJobDto = new SubmitJobDto(10,20,30);
        given(this.sparkManagerService.submitJob(submitJobDto)).willReturn(false);
        this.mvc.perform(
                post("/v1/spark-manager/func/submitJob").contentType(MediaType.APPLICATION_JSON_UTF8).content(
                        objectMapper.writeValueAsString(submitJobDto)
                ))
                .andDo(print())
                .andExpect(status().isServiceUnavailable())
                .andExpect(content().string("false"));
    }
}
