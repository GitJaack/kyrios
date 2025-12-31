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
import fr.cmp.kyrios.model.EmploiModel;
import fr.cmp.kyrios.model.dto.EmploiDTO;
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
    @Operation(summary = "Récupérer tous les emplois", description = "Retourne la liste de tous les emplois")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des emplois récupérée avec succès"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur", content = @Content())

    })
    public List<EmploiModel> list() {
        return emploiService.listAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un emploi par ID", description = "Retourne un emploi spécifique par son identifiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Emploi trouvé"),
            @ApiResponse(responseCode = "404", description = "Emploi non trouvé", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erreur serveur", content = @Content())
    })
    public EmploiModel get(@PathVariable int id) {
        return emploiService.get(id);
    }

    @PostMapping
    @Operation(summary = "Créer un nouvel emploi", description = "Crée un nouvel emploi avec les informations fournies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Emploi créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Ressource liée non trouvée", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erreur serveur", content = @Content())
    })
    public ResponseEntity<EmploiModel> create(@Valid @RequestBody EmploiDTO dto) {
        EmploiModel created = emploiService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un emploi", description = "Modifie un emploi existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Emploi mis à jour avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Emploi ou ressource liée non trouvés", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erreur serveur", content = @Content())
    })
    public ResponseEntity<EmploiModel> update(@PathVariable int id, @Valid @RequestBody EmploiDTO dto) {
        EmploiModel updated = emploiService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un emploi", description = "Supprime un emploi par son identifiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Emploi supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Emploi non trouvé", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erreur serveur", content = @Content())
    })
    public ResponseEntity<Void> delete(@PathVariable int id) {
        emploiService.delete(id);
        return ResponseEntity.noContent().build();
    }

}