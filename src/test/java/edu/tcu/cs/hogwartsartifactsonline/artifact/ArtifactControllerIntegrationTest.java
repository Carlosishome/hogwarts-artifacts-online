package edu.tcu.cs.hogwartsartifactsonline.artifact;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.hogwartsartifactsonline.system.StatusCode;
import net.minidev.json.JSONObject;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.support.discovery.SelectorResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.BDDAssertions.and;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Integration test for Artifact API endpoints")
@Tag("integration")
public class ArtifactControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    String token;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @BeforeEach
    void setUp(){

        ResultActions jonhn = this.mockMvc
                .perform(post(this.baseUrl + "/users/login")
                        .wtih(httpBasic("john" ,"123456")));
       MvcResult mvcResult =  resultAction.andDo(print()).andreturn();
       String contentAsString = mvcResult.getResponse().getContentAsString();
       JSONObject json  = new JSONObject(contentAsString);
        this.token = "Bearer" + json.getJSONObject("data").getString("token");

    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testFindAllArtifactsSuccess() throws Exception {
            this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseUrl + "/artifacts").accept(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.flag").value(true))
                    .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                    .andExpect(jsonPath("$.message").value("Find All Success"))
                    .andExpect(jsonPath("$.data", Matchers.hasSize(6));

    }

    @Test
    @DisplayName("Check AddArtifact with valid input (Post)")
    void testAddArtifactSuccess()throws Exception{
        Artifact a = new Artifact();
        a.setName("A Remembrall was magical large marble-sized glass ball that contained smoke");
        a.setImageUrl("ImageUrl");

        String json = this.objectMapper.writeValueAsString(a);

        this.mockMvc.perform(post(this.baseUrl  + "/artifacts").header("Authorization", this.token).contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value("Rememberall"))
                .andExpect(jsonPath("$.data.description").value("A Remembrall was here"))
                .andExpect(jsonPath("$.data.imageUrl").value("ImageUrl"))
this.mockMvc.perform(get(this.baseUrl + "/articafts").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(7))) ;

    }


}
