package fr.cmp.kyrios.model.App;

import java.time.LocalDateTime;

import fr.cmp.kyrios.model.Emploi.DirectionModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "applications")
@Data
public class AppModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 16)
    private String name;

    @OneToOne
    @JoinColumn(name = "direction_id", nullable = false)
    private DirectionModel direction;

    @Column(length = 255)
    private String description;

    @Column(name = "date_created")
    private LocalDateTime dateCreated;

}
