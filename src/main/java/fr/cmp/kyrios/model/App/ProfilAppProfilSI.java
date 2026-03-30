package fr.cmp.kyrios.model.App;

import fr.cmp.kyrios.model.Si.ProfilSIModel;
import lombok.Data;
import lombok.ToString;

@Data
public class ProfilAppProfilSI {
    private int id;

    @ToString.Exclude
    private ProfilAppModel profilApp;

    private ProfilSIModel profilSI;

    private AppModel application;
}
