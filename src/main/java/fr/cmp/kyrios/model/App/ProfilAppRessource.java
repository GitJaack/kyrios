package fr.cmp.kyrios.model.App;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "profil_app_ressources")
@Data
public class ProfilAppRessource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "profil_app_id", nullable = false)
    private ProfilAppModel profilApp;

    @ManyToOne(optional = true)
    @JoinColumn(name = "ressource_app_id", nullable = true)
    private RessourceAppModel ressource;
}
