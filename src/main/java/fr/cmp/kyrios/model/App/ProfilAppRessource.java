package fr.cmp.kyrios.model.App;

import lombok.Data;

@Data
public class ProfilAppRessource {
    private int id;

    private ProfilAppModel profilApp;

    private RessourceAppModel ressource;
}
