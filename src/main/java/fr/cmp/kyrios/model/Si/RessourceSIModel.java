package fr.cmp.kyrios.model.Si;

import lombok.Data;
import lombok.ToString;

@Data
public class RessourceSIModel {

    public enum TypeAcces {
        LECTURE,
        LECTURE_ECRITURE
    }

    private int id;

    @ToString.Exclude
    private CategorieSIModel categorie;

    private String name;

    private TypeAcces typeAcces;
}
