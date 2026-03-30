package fr.cmp.kyrios.mapper;

import fr.cmp.kyrios.dao.RessourceSIDao;
import fr.cmp.kyrios.model.Si.RessourceSIModel;
import fr.cmp.kyrios.model.Si.dto.ressourceSI.RessourceSIDTO;

public final class RessourceSIMapper {
    private RessourceSIMapper() {
    }

    public static RessourceSIDTO toDto(RessourceSIDao.RessourceReadRow ressource) {
        return RessourceSIDTO.builder()
                .id(ressource.id())
                .categorie(ressource.categorieName())
                .name(ressource.name())
                .typeAcces(RessourceSIModel.TypeAcces.valueOf(ressource.typeAcces()))
                .build();
    }
}
