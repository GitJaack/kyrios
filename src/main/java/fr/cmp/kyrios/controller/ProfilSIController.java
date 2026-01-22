package fr.cmp.kyrios.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import fr.cmp.kyrios.model.Si.ProfilSIModel;
import fr.cmp.kyrios.model.Si.dto.profilSI.ProfilSIDTOCreate;
import fr.cmp.kyrios.model.Si.dto.profilSI.ProfilSIDTOCreateResponse;
import fr.cmp.kyrios.model.Si.dto.profilSI.ProfilSIDTODeleteResponse;
import fr.cmp.kyrios.model.Si.dto.profilSI.ProfilSIDTOResponse;
import fr.cmp.kyrios.model.Si.dto.profilSI.ProfilSIUpdateDTO;
import fr.cmp.kyrios.service.ProfilSIService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/profils-si")
@Tag(name = "Profils SI", description = "Gestion des profils SI")
public class ProfilSIController {
        @Autowired
        private ProfilSIService profilSIService;

        @GetMapping()
        @Operation(summary = "Recuperer tous les profils SI")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Liste des profils SI récupérée avec succès"),
                        @ApiResponse(responseCode = "500", description = "Erreur serveur", content = @Content())
        })
        public List<ProfilSIDTOResponse> list() {
                return profilSIService.listAll().stream()
                                .map(profilSIService::toResponseDTO)
                                .toList();
        }

        @GetMapping("/{id}")
        @Operation(summary = "Récupérer un profil si par ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Profil si trouvé"),
                        @ApiResponse(responseCode = "404", description = "Profil si non trouvé", content = @Content()),
                        @ApiResponse(responseCode = "500", description = "Erreur serveur", content = @Content())
        })
        public ProfilSIDTOResponse get(@PathVariable int id) {
                ProfilSIModel profil = profilSIService.getById(id);
                return profilSIService.toResponseDTO(profil);
        }

        @PostMapping()
        @Operation(summary = "Créer un nouveau profil SI")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Profil SI créé avec succès"),
                        @ApiResponse(responseCode = "400", description = "Données invalides", content = @Content()),
                        @ApiResponse(responseCode = "500", description = "Erreur serveur", content = @Content())
        })
        public ResponseEntity<ProfilSIDTOCreateResponse> create(@Valid @RequestBody ProfilSIDTOCreate dto) {
                ProfilSIDTOCreateResponse created = profilSIService.create(dto);
                return ResponseEntity.status(HttpStatus.CREATED).body(created);
        }

        @PutMapping("/{id}")
        @Operation(summary = "Mettre à jour un profil SI")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Profil SI mis à jour avec succès"),
                        @ApiResponse(responseCode = "404", description = "Profil SI non trouvé", content = @Content()),
                        @ApiResponse(responseCode = "400", description = "Données invalides", content = @Content()),
                        @ApiResponse(responseCode = "500", description = "Erreur serveur", content = @Content())
        })
        public ResponseEntity<ProfilSIDTOResponse> update(@PathVariable int id,
                        @Valid @RequestBody ProfilSIUpdateDTO dto) {
                ProfilSIModel updated = profilSIService.update(id, dto);
                return ResponseEntity.ok(profilSIService.toResponseDTO(updated));
        }

        @DeleteMapping("/{id}")
        @Operation(summary = "Supprimer un profil SI")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Profil SI supprimé avec succès"),
                        @ApiResponse(responseCode = "404", description = "Profil SI non trouvé", content = @Content()),
                        @ApiResponse(responseCode = "500", description = "Erreur serveur", content = @Content())
        })
        public ResponseEntity<ProfilSIDTODeleteResponse> delete(@PathVariable int id) {
                ProfilSIDTODeleteResponse response = profilSIService.delete(id);
                return ResponseEntity.ok(response);
        }

}
