package fr.cmp.kyrios.model.Si;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import fr.cmp.kyrios.model.App.ProfilAppProfilSI;
import fr.cmp.kyrios.model.Emploi.DirectionModel;
import fr.cmp.kyrios.model.Emploi.EmploiModel;
import lombok.Data;
import lombok.ToString;

@Data
public class ProfilSIModel {
    private int id;

    private String name;

    private DirectionModel direction;

    @JsonManagedReference
    @ToString.Exclude
    private List<EmploiModel> emplois;

    @ToString.Exclude
    private List<ProfilSIRessource> profilSIRessources;

    @ToString.Exclude
    private List<ProfilAppProfilSI> profilApps;

    private LocalDateTime dateCreated;

    private LocalDateTime dateUpdated;

    public ProfilSIModel() {
        this.emplois = new ArrayList<>();
        this.profilSIRessources = new ArrayList<>();
        this.profilApps = new ArrayList<>();
    }
}
