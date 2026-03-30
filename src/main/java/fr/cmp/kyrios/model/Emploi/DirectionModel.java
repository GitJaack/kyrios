package fr.cmp.kyrios.model.Emploi;

import java.util.ArrayList;
import java.util.List;

import fr.cmp.kyrios.model.Si.RessourceSIModel;
import lombok.Data;
import lombok.ToString;

@Data
public class DirectionModel {
    private int id;

    private String name;

    @ToString.Exclude
    private List<RessourceSIModel> ressourcesDefault;

    public DirectionModel() {
        this.ressourcesDefault = new ArrayList<>();
    }
}
