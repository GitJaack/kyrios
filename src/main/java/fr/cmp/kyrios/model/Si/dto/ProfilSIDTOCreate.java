package fr.cmp.kyrios.model.Si.dto;

import fr.cmp.kyrios.model.Emploi.dto.EmploiDTO;
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
    private EmploiDTO emploi;

    // Etape 2 : Informations du profil SI
    private ProfilSIDTO profilSI;

}
