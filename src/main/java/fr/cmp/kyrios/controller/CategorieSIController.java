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

import fr.cmp.kyrios.model.Si.CategorieSIModel;
import fr.cmp.kyrios.model.Si.dto.CategorieSIDTOCreate;
import fr.cmp.kyrios.model.Si.dto.CategorieSIDTOResponse;
import fr.cmp.kyrios.service.CategorieSIService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categories-si")
@Tag(name = "Catégories SI", description = "Gestion des catégories de ressources SI")
public class CategorieSIController {
        @Autowired
        private CategorieSIService categorieSIService;

        @GetMapping()
        @Operation(summary = "Récupérer toutes les catégories SI")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Liste des catégories récupérée avec succès"),
                        @ApiResponse(responseCode = "500", description = "Erreur serveur", content = @Content())
        })
        public List<CategorieSIDTOResponse> list() {
                return categorieSIService.listAll().stream()
                                .map(categorieSIService::toDTO)
                                .toList();
        }

        @GetMapping("/{id}")
        @Operation(summary = "Récupérer une catégorie SI par ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Catégorie trouvée"),
                        @ApiResponse(responseCode = "404", description = "Catégorie non trouvée", content = @Content()),
                        @ApiResponse(responseCode = "500", description = "Erreur serveur", content = @Content())
        })
        public CategorieSIDTOResponse get(@PathVariable int id) {
                return categorieSIService.toDTO(categorieSIService.getById(id));
        }

        @PostMapping()
        @Operation(summary = "Créer une nouvelle catégorie SI")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Catégorie créée avec succès"),
                        @ApiResponse(responseCode = "400", description = "Données invalides", content = @Content()),
                        @ApiResponse(responseCode = "500", description = "Erreur serveur", content = @Content())
        })
        public ResponseEntity<CategorieSIDTOResponse> create(@Valid @RequestBody CategorieSIDTOCreate dto) {
                CategorieSIModel created = categorieSIService.create(dto);
                return ResponseEntity.status(HttpStatus.CREATED).body(categorieSIService.toDTO(created));
        }

        @PutMapping("/{id}")
        @Operation(summary = "Mettre à jour une catégorie SI")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Catégorie mise à jour avec succès"),
                        @ApiResponse(responseCode = "404", description = "Catégorie non trouvée", content = @Content()),
                        @ApiResponse(responseCode = "400", description = "Données invalides", content = @Content()),
                        @ApiResponse(responseCode = "500", description = "Erreur serveur", content = @Content())
        })
        public ResponseEntity<CategorieSIDTOResponse> update(@PathVariable int id,
                        @Valid @RequestBody CategorieSIDTOCreate dto) {
                CategorieSIModel updated = categorieSIService.update(id, dto.getName());
                return ResponseEntity.ok(categorieSIService.toDTO(updated));
        }

        @DeleteMapping("/{id}")
        @Operation(summary = "Supprimer une catégorie SI")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Catégorie supprimée avec succès", content = @Content()),
                        @ApiResponse(responseCode = "404", description = "Catégorie non trouvée", content = @Content()),
                        @ApiResponse(responseCode = "500", description = "Erreur serveur", content = @Content())
        })
        public ResponseEntity<String> delete(@PathVariable int id) {
                categorieSIService.delete(id);
                return ResponseEntity.ok("Catégorie avec l'ID " + id + " supprimée avec succès");
        }

}
