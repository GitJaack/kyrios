package fr.cmp.kyrios.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import fr.cmp.kyrios.model.Emploi.EmploiModel;
import fr.cmp.kyrios.model.Si.ProfilSIModel;
import fr.cmp.kyrios.model.Si.dto.ProfilSIDTO;
import fr.cmp.kyrios.model.Si.dto.ProfilSIResponseDTO;
import fr.cmp.kyrios.service.ProfilSIService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/profils-si")
@Tag(name = "Profils SI", description = "Gestion des profils SI")
public class ProfilSIController {
    @Autowired
    private ProfilSIService profilSIService;

    ProfilSIController(ProfilSIService profilSIService) {
        this.profilSIService = profilSIService;
    }

    @GetMapping()
    @Operation(summary = "Recuperer tous les profils SI")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des profils SI récupérée avec succès"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur", content = @Content())
    })
    public List<ProfilSIResponseDTO> list() {
        return profilSIService.listAll().stream()
                .map(profil -> {
                    return profilSIService.toResponseDTO(profil,
                            profil.getEmplois().isEmpty() ? null : profil.getEmplois().get(0));
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un profil si par ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profil si trouvé"),
            @ApiResponse(responseCode = "404", description = "Profil si non trouvé", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erreur serveur", content = @Content())
    })
    public ProfilSIResponseDTO get(@PathVariable int id) {
        ProfilSIModel profil = profilSIService.getById(id);
        EmploiModel emploi = profil.getEmplois().isEmpty() ? null : profil.getEmplois().get(0);
        return profilSIService.toResponseDTO(profil, emploi);
    }

    @PostMapping()
    @Operation(summary = "Créer un nouveau profil SI")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Profil SI créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erreur serveur", content = @Content())
    })
    public ResponseEntity<ProfilSIResponseDTO> create(@Valid @RequestBody ProfilSIDTO dto) {
        ProfilSIResponseDTO created = profilSIService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

}
