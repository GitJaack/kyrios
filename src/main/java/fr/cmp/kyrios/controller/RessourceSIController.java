package fr.cmp.kyrios.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.cmp.kyrios.model.Si.dto.RessourceSIDTO;
import fr.cmp.kyrios.service.RessourceSIService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/ressources-si")
@Tag(name = "Ressources SI", description = "Gestion des ressources SI")
public class RessourceSIController {
    @Autowired
    private RessourceSIService ressourceSIService;

    @GetMapping
    @Operation(summary = "Récupérer toutes les ressources SI")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des ressources SI récupérée avec succès"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur", content = @Content())

    })
    public List<RessourceSIDTO> list() {
        return ressourceSIService.listAll().stream()
                .map(ressourceSIService::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une ressource SI par ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ressource trouvée"),
            @ApiResponse(responseCode = "404", description = "Ressource non trouvée", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erreur serveur", content = @Content())
    })
    public RessourceSIDTO get(@PathVariable int id) {
        return ressourceSIService.toDTO(ressourceSIService.getById(id));
    }

    @GetMapping("/defaut")
    @Operation(summary = "Récupérer les ressources par défaut d'une direction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ressources par défaut récupérées"),
            @ApiResponse(responseCode = "404", description = "Direction non trouvée", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erreur serveur", content = @Content())
    })
    public List<RessourceSIDTO> getRessourcesDefaut(@RequestParam int id) {
        return ressourceSIService.toDTOList(ressourceSIService.getRessourcesParDefautByDirection(id));
    }

    @GetMapping("/by-categorie")
    @Operation(summary = "Récupérer les ressources d'une catégorie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ressources de la catégorie récupérées"),
            @ApiResponse(responseCode = "404", description = "Catégorie non trouvée", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erreur serveur", content = @Content())
    })
    public List<RessourceSIDTO> getByCategorie(@RequestParam int id) {
        return ressourceSIService.toDTOList(ressourceSIService.getRessourcesByCategorie(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une ressource SI")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ressource supprimée avec succès", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Ressource non trouvée", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erreur serveur", content = @Content())
    })
    public ResponseEntity<String> delete(@PathVariable int id) {
        ressourceSIService.delete(id);
        return ResponseEntity.ok("Ressource SI avec l'ID " + id + " supprimée avec succès");
    }
}
