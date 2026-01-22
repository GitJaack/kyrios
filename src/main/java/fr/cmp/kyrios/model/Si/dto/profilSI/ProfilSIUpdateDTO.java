package fr.cmp.kyrios.model.Si.dto.profilSI;

import java.util.List;

import fr.cmp.kyrios.model.Si.dto.ressourceSI.RessourceTypeAccesDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ProfilSIUpdateDTO {
    @NotBlank(message = "Le profil SI ne peut pas être vide")
    @Size(max = 50, message = "Le profil SI ne doit pas dépasser 50 caractères")
    @Schema(description = "Nom du profil SI", example = "Developpeur fullstack")
    private String profilSI;

    @Valid
    @Schema(description = "Liste des ressources SI avec leur type d'accès spécifique")
    private List<RessourceTypeAccesDTO> ressources;
}
