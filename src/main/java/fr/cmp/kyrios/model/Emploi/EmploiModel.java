package fr.cmp.kyrios.model.Emploi;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fr.cmp.kyrios.model.Si.ProfilSIModel;
import lombok.Data;
import lombok.ToString;

@Data
@JsonPropertyOrder({ "id", "emploiName", "direction", "service", "domaine", "status", "profilSI", "dateCreated",
        "dateUpdated" })
public class EmploiModel {

    public enum Status {
        PERMANENT,
        STAGIAIRE,
        ALTERNANT,
        PRESTATAIRES_EXTERIEURS,
        SAISONNIER
    }

    private int id;

    private String emploiName;

    @ToString.Exclude
    private DirectionModel direction;

    @ToString.Exclude
    private ServiceModel service;

    @ToString.Exclude
    private DomaineModel domaine;

    private Status status;

    @ToString.Exclude
    private ProfilSIModel profilSI;

    private LocalDateTime dateCreated;

    private LocalDateTime dateUpdated;
}
