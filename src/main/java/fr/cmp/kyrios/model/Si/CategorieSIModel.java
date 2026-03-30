package fr.cmp.kyrios.model.Si;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
public class CategorieSIModel {
    private int id;

    private String name;

    @ToString.Exclude
    private List<RessourceSIModel> ressources;

    public CategorieSIModel() {
        this.ressources = new ArrayList<>();
    }
}