package br.com.viniciusmassari.gestao_vagas.modules.company.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.viniciusmassari.gestao_vagas.exceptions.CompanyNotFoundException;
import br.com.viniciusmassari.gestao_vagas.modules.company.dto.CreateJobDTO;
import br.com.viniciusmassari.gestao_vagas.modules.company.entities.JobEntity;
import br.com.viniciusmassari.gestao_vagas.modules.company.useCases.CreateJobUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/job")
@Tag(description = "Métodos referentes a job", name = "Job Controller")
public class JobsController {

        @Autowired
        private CreateJobUseCase createJobUseCase;

        @PostMapping("/")
        @PreAuthorize("hasRole('COMPANY')")
        @Operation(summary = "Cria uma nova vaga de emprego (Disponível apenas para a role COMPANY)")
        @SecurityRequirement(name = "jwt_auth")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", content = {
                                        @Content(schema = @Schema(implementation = JobEntity.class))
                        })
        })
        public ResponseEntity<Object> create(@Valid @RequestBody CreateJobDTO createJobDTO,
                        HttpServletRequest request) {
                try {

                        var company_id = request.getAttribute("company_id").toString();
                        JobEntity jobEntity = JobEntity.builder()
                                        .benefits(createJobDTO.getBenefits())
                                        .description(createJobDTO.getDescription())
                                        .level(createJobDTO.getLevel())
                                        .companyId(UUID.fromString(company_id)).title(createJobDTO.getTitle())
                                        .build();

                        var response = createJobUseCase.execute(jobEntity);
                        return ResponseEntity.status(HttpStatus.CREATED).body(response);
                } catch (CompanyNotFoundException e) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

                } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

                }
        }
}