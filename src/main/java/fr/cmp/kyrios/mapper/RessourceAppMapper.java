package fr.cmp.kyrios.mapper;

import fr.cmp.kyrios.dao.RessourceAppDao;
import fr.cmp.kyrios.model.App.dto.RessourceAppDTOResponse;

public final class RessourceAppMapper {
    private RessourceAppMapper() {
    }

    public static RessourceAppDTOResponse toDto(RessourceAppDao.RessourceAppReadRow ressourceApp) {
        return RessourceAppDTOResponse.builder()
                .id(ressourceApp.id())
                .name(ressourceApp.name())
                .description(ressourceApp.description())
                .application(ressourceApp.applicationName())
                .categoryId(ressourceApp.categoryId())
                .category(ressourceApp.categoryName())
                .build();
    }
}
