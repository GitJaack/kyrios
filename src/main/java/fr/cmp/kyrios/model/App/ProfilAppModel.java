package fr.cmp.kyrios.model.App;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
public class ProfilAppModel {
    private int id;

    private String name;

    @ToString.Exclude
    private AppModel application;

    private List<ProfilAppProfilSI> profilSI = new ArrayList<>();

    private List<ProfilAppRessource> profilAppRessources = new ArrayList<>();

    private LocalDateTime dateCreated;

    private LocalDateTime dateUpdated;
}
