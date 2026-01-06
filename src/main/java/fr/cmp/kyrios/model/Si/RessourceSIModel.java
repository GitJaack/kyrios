package fr.cmp.kyrios.model.Si;

// import java.time.LocalDate;

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

    @ManyToOne(optional = false)
    @JoinColumn(name = "categorie_id", nullable = false)
    private CategorieSIModel categorie;

    @Column(name = "libelle_acces", nullable = false, length = 50)
    private String libelleAcces;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_acces", nullable = false)
    private TypeAcces typeAcces;

    @Column(name = "is_default")
    private boolean isDefault = false;
}
