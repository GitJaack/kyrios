package fr.cmp.kyrios.model;

// import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "ressource_si")
@Data
public class RessourceSIModel {

    public enum TypeAcces {
        LECTURE,
        LECTURE_ECRITURE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "libelle_acces", nullable = false, length = 16)
    private String libelleAcces;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_acces", nullable = false)
    private TypeAcces typeAcces;

    // @Column(name = "date_expiration")
    // private LocalDate dateExpiration;
}
