package fr.cmp.kyrios.model.Si.dto.profilSI;

import fr.cmp.kyrios.model.Emploi.dto.EmploiDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfilSIDTOCreate {
    // Etape 1 : Informations de l'emploi
    @Valid
    @NotNull(message = "Les informations de l'emploi sont requises")
    private EmploiDTO emploi;

    // Etape 2 : Informations du profil SI
    @Valid
    @NotNull(message = "Les informations du profil SI sont requises")
    private ProfilSIDTO profilSI;

}
