package fr.cmp.kyrios.model.Si;

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
@Table(name = "profil_si_ressources")
@Data
public class ProfilSIRessource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "profil_si_id", nullable = false)
    private ProfilSIModel profilSI;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ressource_id", nullable = false)
    private RessourceSIModel ressource;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_acces", nullable = false)
    private RessourceSIModel.TypeAcces typeAcces;
}
