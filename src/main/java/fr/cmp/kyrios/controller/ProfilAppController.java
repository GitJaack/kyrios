package fr.cmp.kyrios.controller;

import org.springframework.web.bind.annotation.RestController;

import fr.cmp.kyrios.model.App.dto.ProfilAppDTOResponse;
import fr.cmp.kyrios.service.ProfilAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/profil-app")
@Tag(name = "Profils Application", description = "Gestion des profils Application")
public class ProfilAppController {
    @Autowired
    private ProfilAppService profilAppService;

    @GetMapping()
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

}
