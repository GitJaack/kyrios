package fr.cmp.kyrios.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;

import jakarta.validation.Valid;
import fr.cmp.kyrios.model.Emploi.EmploiModel;
import fr.cmp.kyrios.model.Emploi.dto.EmploiDTO;
import fr.cmp.kyrios.model.Emploi.dto.EmploiDTOResponse;
import fr.cmp.kyrios.service.EmploiService;

@RestController
@RequestMapping("/api/emplois")
@Tag(name = "Emplois", description = "Gestion des emplois")
public class EmploiController {
    @Autowired
    private EmploiService emploiService;

    public EmploiController(EmploiService emploiService) {
        this.emploiService = emploiService;
    }

    @GetMapping
    @Operation(summary = "Récupérer tous les emplois")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des emplois récupérée avec succès"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur", content = @Content())

    })
    public List<EmploiDTOResponse> list() {
        return emploiService.listAll().stream()
                .map(emploiService::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un emploi par ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Emploi trouvé"),
            @ApiResponse(responseCode = "404", description = "Emploi non trouvé", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erreur serveur", content = @Content())
    })
    public EmploiDTOResponse get(@PathVariable int id) {
        return emploiService.toDTO(emploiService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Créer un nouvel emploi")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Emploi créé avec succès", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Données invalides", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erreur serveur", content = @Content())
    })
    public ResponseEntity<EmploiDTOResponse> create(@Valid @RequestBody EmploiDTO dto) {
        EmploiModel created = emploiService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(emploiService.toDTO(created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un emploi")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Emploi mis à jour avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erreur serveur", content = @Content())
    })
    public ResponseEntity<EmploiDTOResponse> update(@PathVariable int id, @Valid @RequestBody EmploiDTO dto) {
        EmploiModel updated = emploiService.update(id, dto);
        return ResponseEntity.ok(emploiService.toDTO(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un emploi")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Emploi supprimé avec succès", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Emploi non trouvé", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erreur serveur", content = @Content())
    })
    public ResponseEntity<String> delete(@PathVariable int id) {
        emploiService.delete(id);
        return ResponseEntity.ok("Emploi avec l'ID " + id + " supprimé avec succès");
    }

}