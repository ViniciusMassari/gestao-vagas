package br.com.viniciusmassari.gestao_vagas.modules.candidate.controllers;

import org.springframework.web.bind.annotation.*;

import br.com.viniciusmassari.gestao_vagas.modules.company.entities.JobEntity;

import br.com.viniciusmassari.gestao_vagas.modules.candidate.useCases.*;

import br.com.viniciusmassari.gestao_vagas.exceptions.*;

import br.com.viniciusmassari.gestao_vagas.modules.candidate.dto.*;

import br.com.viniciusmassari.gestao_vagas.modules.candidate.entity.CandidateEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;

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

    @Autowired
    private ApplyJobUseCase applyJobUseCase;

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
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = CandidateProfileResponseDTO.class))
    })
    @ApiResponse(responseCode = "400", description = "User not found")
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

    @Operation(summary = "Aplica o candidato à uma vaga de emprego")
    @SecurityRequirement(name = "jwt_auth")
    @PreAuthorize("hasRole('CANDIDATE')")
    @PostMapping("/job/apply")
    public ResponseEntity<Object> applyJob(HttpServletRequest request, @RequestBody UUID jobId) {
        var candidateId = request.getAttribute("candidate_id").toString();
        try {
            applyJobUseCase.execute(UUID.fromString(candidateId), jobId);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (JobNotFoundException | UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

        }

    }

}
