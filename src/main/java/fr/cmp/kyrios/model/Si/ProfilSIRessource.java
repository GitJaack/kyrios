package fr.cmp.kyrios.model.Si;

import lombok.Data;
import lombok.ToString;

@Data
public class ProfilSIRessource {

    private int id;

    @ToString.Exclude
    private ProfilSIModel profilSI;

    @ToString.Exclude
    private RessourceSIModel ressource;

    private RessourceSIModel.TypeAcces typeAcces;
}
