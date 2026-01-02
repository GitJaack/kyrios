package fr.cmp.kyrios.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "profils_si")
@Data
public class ProfilSIModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 30)
    private String name;

    @OneToMany(mappedBy = "profilSI", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<EmploiModel> emplois;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "profil_si_id")
    private List<RessourceSIModel> ressourcesSI;

    @Column(name = "date_created")
    private LocalDateTime dateCreated;

    @Column(name = "date_updated")
    private LocalDateTime dateUpdated;

    public ProfilSIModel() {
        this.emplois = new ArrayList<>();
        this.ressourcesSI = new ArrayList<>();
    }
}
