package fr.cmp.kyrios.model.Emploi;

import java.util.ArrayList;
import java.util.List;

import fr.cmp.kyrios.model.Si.RessourceSIModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "directions")
@Data
public class DirectionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 50)
    private String name;

    @ManyToMany
    @JoinTable(name = "direction_ressources_default", joinColumns = @JoinColumn(name = "direction_id"), inverseJoinColumns = @JoinColumn(name = "ressource_id"))
    @ToString.Exclude
    private List<RessourceSIModel> ressourcesDefault;

    public DirectionModel() {
        this.ressourcesDefault = new ArrayList<>();
    }
}
