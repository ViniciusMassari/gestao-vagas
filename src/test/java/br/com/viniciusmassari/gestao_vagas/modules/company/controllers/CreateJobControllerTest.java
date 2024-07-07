package br.com.viniciusmassari.gestao_vagas.modules.company.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.viniciusmassari.gestao_vagas.modules.company.dto.CreateJobDTO;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CreateJobControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();

    }

    private static String objectToJSON(Object obj) {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("Should be able to create a new job")
    @Test
    public void should_create_a_new_job() throws Exception {
        CreateJobDTO createJobDTO = CreateJobDTO.builder().benefits("BENEFITS_TEST").description("DESCRIPTION_TEST")
                .level("LEVEL_TEST").title("Repositor de estoque").build();
        var result = mvc.perform(
                MockMvcRequestBuilders.post("/job/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJSON(createJobDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        System.out.println(result);
    }
}
