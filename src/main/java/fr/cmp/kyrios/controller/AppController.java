package fr.cmp.kyrios.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.cmp.kyrios.model.App.AppModel;
import fr.cmp.kyrios.model.App.dto.AppDTOCreate;
import fr.cmp.kyrios.model.App.dto.AppDTOResponse;
import fr.cmp.kyrios.service.AppService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/applications")
@Tag(name = "Applications", description = "Gestion des applications")
public class AppController {
    @Autowired
    private AppService appService;

    @Autowired
    private EntityFinder entityFinder;

    @GetMapping()
    @Operation(summary = "Récupérer toutes les applications")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des applications récupérée avec succès"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur", content = @Content())
    })
    public List<AppDTOResponse> list() {
        return appService.listAll().stream()
                .map(appService::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une application par ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Application trouvée"),
            @ApiResponse(responseCode = "404", description = "Application non trouvée", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erreur serveur", content = @Content())
    })
    public AppDTOResponse get(@PathVariable int id) {
        return appService.toDTO(entityFinder.findApplicationOrThrow(id));
    }

    @PostMapping
    @Operation(summary = "Créer une nouvelle application")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Application créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erreur serveur", content = @Content())
    })
    public ResponseEntity<AppDTOResponse> create(@Valid @RequestBody AppDTOCreate dto) {
        AppModel created = appService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(appService.toDTO(created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une application")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Application mise à jour avec succès"),
            @ApiResponse(responseCode = "404", description = "Application non trouvée", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Données invalides", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erreur serveur", content = @Content())
    })
    public ResponseEntity<AppDTOResponse> update(@PathVariable int id, @Valid @RequestBody AppDTOCreate dto) {
        AppModel updated = appService.update(id, dto);
        return ResponseEntity.ok(appService.toDTO(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une application")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Application supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Application non trouvée", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erreur serveur", content = @Content())
    })
    public ResponseEntity<String> delete(@PathVariable int id) {
        appService.delete(id);
        return ResponseEntity.ok("Application avec l'ID " + id + " supprimée avec succès");
    }

}
