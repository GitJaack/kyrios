package fr.cmp.kyrios.model.Emploi;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fr.cmp.kyrios.model.Si.ProfilSIModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "emplois")
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 50)
    private String emploiName;

    @ManyToOne(optional = false)
    @JoinColumn(name = "direction_id", nullable = false)
    @ToString.Exclude
    private DirectionModel direction;

    @ManyToOne
    @JoinColumn(name = "service_id")
    @ToString.Exclude
    private ServiceModel service;

    @ManyToOne
    @JoinColumn(name = "domaine_id")
    @ToString.Exclude
    private DomaineModel domaine;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "profil_si_id", nullable = true)
    @ToString.Exclude
    private ProfilSIModel profilSI;

    @Column(name = "date_created")
    private LocalDateTime dateCreated;

    @Column(name = "date_updated")
    private LocalDateTime dateUpdated;
}
