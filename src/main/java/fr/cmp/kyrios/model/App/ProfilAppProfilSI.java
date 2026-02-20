package fr.cmp.kyrios.model.App;

import fr.cmp.kyrios.model.Si.ProfilSIModel;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "profil_app_profil_si", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "application_id", "profil_si_id" })
})
@Data
public class ProfilAppProfilSI {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "profil_app_id", nullable = false)
    @ToString.Exclude
    private ProfilAppModel profilApp;

    @ManyToOne(optional = false)
    @JoinColumn(name = "profil_si_id", nullable = false)
    private ProfilSIModel profilSI;

    @ManyToOne(optional = false)
    @JoinColumn(name = "application_id", nullable = false)
    private AppModel application;
}
