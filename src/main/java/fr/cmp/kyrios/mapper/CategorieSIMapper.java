package fr.cmp.kyrios.mapper;

import fr.cmp.kyrios.dao.CategorieSIDao;
import fr.cmp.kyrios.model.Si.dto.categorieSI.CategorieSIDTOResponse;

public final class CategorieSIMapper {
    private CategorieSIMapper() {
    }

    public static CategorieSIDTOResponse toDto(CategorieSIDao.CategorieReadRow categorie) {
        return CategorieSIDTOResponse.builder()
                .id(categorie.id())
                .name(categorie.name())
                .build();
    }
}
