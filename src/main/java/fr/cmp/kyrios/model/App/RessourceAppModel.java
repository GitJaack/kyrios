package fr.cmp.kyrios.model.App;

import lombok.Data;
import lombok.ToString;

@Data
public class RessourceAppModel {

    private int id;

    private String name;

    private String description;

    @ToString.Exclude
    private AppModel application;
}
