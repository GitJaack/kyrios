package fr.cmp.kyrios.model.App;

import jakarta.persistence.Column;
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
@Table(name = "ressource_app", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "name", "application_id" })
})
@Data
public class RessourceAppModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 64)
    private String name;

    @Column(length = 255)
    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(name = "application_id", nullable = false)
    @ToString.Exclude
    private AppModel application;
}
