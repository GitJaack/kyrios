package fr.cmp.kyrios.controller;

import org.springframework.web.bind.annotation.RestController;

import fr.cmp.kyrios.model.App.ProfilAppModel;
import fr.cmp.kyrios.model.App.dto.ProfilAppDTOCreate;
import fr.cmp.kyrios.model.App.dto.ProfilAppDTODeleteResponse;
import fr.cmp.kyrios.model.App.dto.ProfilAppDTOResponse;
import fr.cmp.kyrios.service.ProfilAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Content;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/profil-app")
@Tag(name = "Profils Application", description = "Gestion des profils Application")
public class ProfilAppController {
        @Autowired
        private ProfilAppService profilAppService;

        @GetMapping
        @Operation(summary = "Récupérer les profils d'application")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Liste des profils d'application récupérée avec succès"),
                        @ApiResponse(responseCode = "500", description = "Erreur serveur", content = @Content())
        })
        public List<ProfilAppDTOResponse> list() {
                return profilAppService.listAll().stream()
                                .map(profilAppService::toDTO)
                                .toList();
        }

        @GetMapping("/by-application")
        @Operation(summary = "Récupérer les profils d'application par application")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Liste des profils d'application récupérée avec succès"),
                        @ApiResponse(responseCode = "404", description = "Application non trouvée", content = @Content()),
                        @ApiResponse(responseCode = "500", description = "Erreur serveur", content = @Content())
        })
        public List<ProfilAppDTOResponse> listByApplication(@RequestParam int applicationId) {
                return profilAppService.getByApplication(applicationId).stream()
                                .map(profilAppService::toDTO)
                                .toList();
        }

        @PostMapping
        @Operation(summary = "Créer un profil d'application")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Profil d'application créé avec succès"),
                        @ApiResponse(responseCode = "400", description = "Données invalides fournies", content = @Content()),
                        @ApiResponse(responseCode = "404", description = "Entité non trouvée", content = @Content()),
                        @ApiResponse(responseCode = "500", description = "Erreur serveur", content = @Content())
        })
        public ResponseEntity<ProfilAppDTOResponse> create(@Valid @RequestBody ProfilAppDTOCreate dto) {
                ProfilAppModel createdProfilApp = profilAppService.create(dto);
                return ResponseEntity.status(HttpStatus.CREATED).body(profilAppService.toDTO(createdProfilApp));
        }

        @PutMapping("/{id}")
        @Operation(summary = "Mettre à jour un profil d'application")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Profil d'application mis à jour avec succès"),
                        @ApiResponse(responseCode = "400", description = "Données invalides fournies", content = @Content()),
                        @ApiResponse(responseCode = "404", description = "Entité non trouvée", content = @Content()),
                        @ApiResponse(responseCode = "500", description = "Erreur serveur", content = @Content())
        })
        public ResponseEntity<ProfilAppDTOResponse> update(@PathVariable int id,
                        @Valid @RequestBody ProfilAppDTOCreate dto) {
                ProfilAppModel updatedProfilApp = profilAppService.update(id, dto);
                return ResponseEntity.ok(profilAppService.toDTO(updatedProfilApp));
        }

        @DeleteMapping("/{id}")
        @Operation(summary = "Supprimer un profil d'application")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Profil d'application supprimé avec succès", content = @Content()),
                        @ApiResponse(responseCode = "404", description = "Profil d'application non trouvée", content = @Content()),
                        @ApiResponse(responseCode = "500", description = "Erreur serveur", content = @Content())
        })
        public ResponseEntity<ProfilAppDTODeleteResponse> delete(@PathVariable int id) {
                ProfilAppDTODeleteResponse response = profilAppService.delete(id);
                return ResponseEntity.ok(response);
        }
}