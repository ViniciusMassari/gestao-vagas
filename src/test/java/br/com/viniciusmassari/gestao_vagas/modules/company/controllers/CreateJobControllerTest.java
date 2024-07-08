package br.com.viniciusmassari.gestao_vagas.modules.company.controllers;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import br.com.viniciusmassari.gestao_vagas.exceptions.CompanyNotFoundException;
import br.com.viniciusmassari.gestao_vagas.modules.company.dto.CreateJobDTO;
import br.com.viniciusmassari.gestao_vagas.modules.company.entities.CompanyEntity;
import br.com.viniciusmassari.gestao_vagas.modules.company.repositories.CompanyRepository;
import br.com.viniciusmassari.gestao_vagas.utils.TestUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CreateJobControllerTest {

        @Value("${security.token.secret}")
        private String securityToken;

        private MockMvc mvc;

        @Autowired
        private WebApplicationContext context;

        @Autowired
        private CompanyRepository companyRepository;

        @Before
        public void setup() {
                mvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity())
                                .build();

        }

        @DisplayName("Should be able to create a new job")
        @Test
        public void should_create_a_new_job() throws Exception {
                CompanyEntity company = CompanyEntity.builder().description("COMPANY_DESCRIPTION")
                                .email("COMPANYEMAIL@EMAIL.COM").name("COMPANY_NAME").password("1234567890")
                                .username("COMPANY_USERNAME").build();

                company = companyRepository.saveAndFlush(company);

                CreateJobDTO createJobDTO = CreateJobDTO.builder().benefits("BENEFITS_TEST")
                                .description("DESCRIPTION_TEST")
                                .level("LEVEL_TEST").title("Repositor de estoque").build();
                try {
                        mvc.perform(
                                        MockMvcRequestBuilders.post("/job/")
                                                        .contentType(MediaType.APPLICATION_JSON)
                                                        .content(TestUtils.objectToJSON(createJobDTO))
                                                        .header("Authorization",
                                                                        TestUtils.generateToken(
                                                                                        company.getId(),
                                                                                        securityToken)))
                                        .andExpect(MockMvcResultMatchers.status().isCreated());
                } catch (Exception e) {
                        assertInstanceOf(CompanyNotFoundException.class, e);
                }

        }

        @DisplayName("Should not be able to create a new job if a company was not found")
        @Test
        public void should_not_be_able_to_create_a_new_job() throws Exception {
                CreateJobDTO createJobDTO = CreateJobDTO.builder().benefits("BENEFITS_TEST")
                                .description("DESCRIPTION_TEST")
                                .level("LEVEL_TEST").title("Repositor de estoque").build();
                mvc.perform(
                                MockMvcRequestBuilders.post("/job/")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(TestUtils.objectToJSON(createJobDTO))
                                                .header("Authorization",
                                                                TestUtils.generateToken(
                                                                                UUID.randomUUID(),
                                                                                securityToken)))
                                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }
}
