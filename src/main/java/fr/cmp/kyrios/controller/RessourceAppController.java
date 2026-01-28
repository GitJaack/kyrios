package fr.cmp.kyrios.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.cmp.kyrios.model.App.RessourceAppModel;
import fr.cmp.kyrios.model.App.dto.RessourceAppDTOCreate;
import fr.cmp.kyrios.model.App.dto.RessourceAppDTOResponse;
import fr.cmp.kyrios.service.RessourceAppService;
import fr.cmp.kyrios.util.EntityFinder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Content;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/ressources-app")
@Tag(name = "Ressource App", description = "Gestion des ressources App")
public class RessourceAppController {
    @Autowired
    private EntityFinder entityFinder;

    @Autowired
    private RessourceAppService ressourceAppService;

    @GetMapping()
    @Operation(summary = "Récupérer toutes les ressources App")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des ressources App récupérée avec succès"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur", content = @Content())
    })
    public List<RessourceAppDTOResponse> list() {
        return ressourceAppService.listAll().stream()
                .map(ressourceAppService::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une ressource App par ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ressource App trouvée"),
            @ApiResponse(responseCode = "404", description = "Ressource App non trouvée", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erreur serveur", content = @Content())
    })
    public RessourceAppDTOResponse get(@PathVariable int id) {
        return ressourceAppService.toDTO(entityFinder.findRessourceAppOrThrow(id));
    }

    @GetMapping("/ressource-application/")
    @Operation(summary = "Récupérer les ressources App par Application ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ressources App trouvées"),
            @ApiResponse(responseCode = "404", description = "Ressources App non trouvées", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erreur serveur", content = @Content())
    })
    public List<RessourceAppDTOResponse> getByApplicationId(@RequestParam int id) {
        return ressourceAppService.toDTOList(ressourceAppService.getRessourcesByApp(id));
    }

    @PostMapping()
    @Operation(summary = "Créer une nouvelle ressource App")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ressource App créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Application non trouvée", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erreur serveur", content = @Content())
    })
    public ResponseEntity<RessourceAppDTOResponse> create(@Valid @RequestBody RessourceAppDTOCreate dto) {
        RessourceAppModel created = ressourceAppService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ressourceAppService.toDTO(created));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une ressource App")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ressource App supprimée avec succès", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Ressource App non trouvée", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erreur serveur", content = @Content())
    })
    public ResponseEntity<String> delete(@PathVariable int id) {
        ressourceAppService.delete(id);
        return ResponseEntity.ok("Ressource App avec l'ID " + id + " supprimée avec succès.");
    }

}
