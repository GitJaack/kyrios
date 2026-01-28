package fr.cmp.kyrios.model.App;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

@Entity
@Table(name = "profil_app")
@Data
public class ProfilAppModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 16)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "application_id", nullable = false)
    private AppModel application;

    @OneToMany(mappedBy = "profilApp", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProfilAppProfilSI> profilSI = new ArrayList<>();

    @OneToMany(mappedBy = "profilApp", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProfilAppRessource> profilAppRessources = new ArrayList<>();

    @Column(name = "date_created")
    private LocalDateTime dateCreated;

    @Column(name = "date_updated")
    private LocalDateTime dateUpdated;
}
