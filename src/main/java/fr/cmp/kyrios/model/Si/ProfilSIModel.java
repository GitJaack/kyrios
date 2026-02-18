package fr.cmp.kyrios.model.Si;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import fr.cmp.kyrios.model.App.ProfilAppProfilSI;
import fr.cmp.kyrios.model.Emploi.DirectionModel;
import fr.cmp.kyrios.model.Emploi.EmploiModel;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "profils_si")
@Data
public class ProfilSIModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @ManyToOne
    @JoinColumn(name = "direction_id")
    private DirectionModel direction;

    @OneToMany(mappedBy = "profilSI")
    @JsonManagedReference
    @ToString.Exclude
    private List<EmploiModel> emplois;

    @OneToMany(mappedBy = "profilSI", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<ProfilSIRessource> profilSIRessources;

    @OneToMany(mappedBy = "profilSI", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<ProfilAppProfilSI> profilApps;

    @Column(name = "date_created")
    private LocalDateTime dateCreated;

    @Column(name = "date_updated")
    private LocalDateTime dateUpdated;

    public ProfilSIModel() {
        this.emplois = new ArrayList<>();
        this.profilSIRessources = new ArrayList<>();
        this.profilApps = new ArrayList<>();
    }
}
