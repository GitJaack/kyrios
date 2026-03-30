package fr.cmp.kyrios.model.App;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import fr.cmp.kyrios.model.Emploi.DirectionModel;
import lombok.Data;

@Data
public class AppModel {
    private int id;

    private String name;

    private DirectionModel direction;

    private String description;

    private List<RessourceAppModel> ressources = new ArrayList<>();

    private LocalDateTime dateCreated;

}
