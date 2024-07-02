package br.com.viniciusmassari.gestao_vagas.modules.candidate.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.viniciusmassari.gestao_vagas.exceptions.UserFoundException;
import br.com.viniciusmassari.gestao_vagas.modules.candidate.CandidateEntity;
import br.com.viniciusmassari.gestao_vagas.modules.candidate.dto.CandidateProfileResponseDTO;
import br.com.viniciusmassari.gestao_vagas.modules.candidate.dto.CreateCandidateDTO;
import br.com.viniciusmassari.gestao_vagas.modules.candidate.dto.ListAllJobsByFilterResponseDTO;
import br.com.viniciusmassari.gestao_vagas.modules.candidate.useCases.CandidateProfileUseCase;
import br.com.viniciusmassari.gestao_vagas.modules.candidate.useCases.CreateCandidateUseCase;
import br.com.viniciusmassari.gestao_vagas.modules.candidate.useCases.ListAllJobsByFilterUseCase;
import br.com.viniciusmassari.gestao_vagas.modules.company.entities.JobEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/candidate")
@Tag(name = "Candidato", description = "Operações referentes ao candidato")

public class CandidateController {

    @Autowired
    private CreateCandidateUseCase createCandidateUseCase;

    @Autowired
    private CandidateProfileUseCase candidateProfileUseCase;

    @Autowired
    private ListAllJobsByFilterUseCase listAllJobsByFilterUseCase;

    @PostMapping("/")
    public ResponseEntity<Object> createCandidate(@Valid @RequestBody CreateCandidateDTO createCandidateDTO)
            throws UserFoundException {
        try {
            var candidateEntity = CandidateEntity.builder().description(createCandidateDTO.getDescription())
                    .email(createCandidateDTO.getEmail()).name(createCandidateDTO.getName())
                    .username(createCandidateDTO.getUsername()).password(createCandidateDTO.getPassword())
                    .curriculum(createCandidateDTO.getCurriculum()).build();
            var result = this.createCandidateUseCase.execute(candidateEntity);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Busca e retorna informações do perfil do usuário", method = "GET")
    @SecurityRequirement(name = "jwt_auth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = CandidateProfileResponseDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "User not found")
    })
    public ResponseEntity<Object> getUserProfile(HttpServletRequest request) {
        var candidateId = request.getAttribute("candidate_id").toString();
        try {
            var result = this.candidateProfileUseCase.execute(UUID.fromString(candidateId));
            return ResponseEntity.ok().body(result);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/job")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Retorna vagas de emprego de acordo com filtro passado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(array = @ArraySchema(schema = @Schema(implementation = JobEntity.class)))
            })
    })
    @SecurityRequirement(name = "jwt_auth")
    public ResponseEntity<List<ListAllJobsByFilterResponseDTO>> getJobsListByFilter(@RequestParam String filter) {
        var jobsList = this.listAllJobsByFilterUseCase.execute(filter);
        return ResponseEntity.ok().body(jobsList);
    }

}
