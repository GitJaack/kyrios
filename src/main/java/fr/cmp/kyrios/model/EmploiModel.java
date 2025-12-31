package fr.cmp.kyrios.model;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

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
    private DirectionModel direction;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private ServiceModel service;

    @ManyToOne
    @JoinColumn(name = "domaine_id")
    private DomaineModel domaine;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @OneToOne(mappedBy = "emploi")
    @JsonBackReference
    private ProfilSIModel profilSI;

    @Column(name = "date_created")
    private LocalDateTime dateCreated;

    @Column(name = "date_updated")
    private LocalDateTime dateUpdated;
}
