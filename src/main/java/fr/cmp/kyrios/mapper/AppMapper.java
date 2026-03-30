package fr.cmp.kyrios.mapper;

import fr.cmp.kyrios.dao.AppDao;
import fr.cmp.kyrios.model.App.dto.AppDTOResponse;

public final class AppMapper {
    private AppMapper() {
    }

    public static AppDTOResponse toDto(AppDao.AppReadRow app) {
        return AppDTOResponse.builder()
                .id(app.id())
                .name(app.name())
                .direction(app.directionName())
                .description(app.description())
                .dateCreated(app.dateCreated())
                .build();
    }
}
