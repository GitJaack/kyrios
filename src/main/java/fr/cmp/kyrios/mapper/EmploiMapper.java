package fr.cmp.kyrios.mapper;

import fr.cmp.kyrios.dao.EmploiDao;
import fr.cmp.kyrios.model.Emploi.EmploiModel;
import fr.cmp.kyrios.model.Emploi.dto.EmploiDTOResponse;
import fr.cmp.kyrios.model.Emploi.dto.ProfilSISimpleDTO;

public final class EmploiMapper {
    private EmploiMapper() {
    }

    public static EmploiDTOResponse toDto(EmploiDao.EmploiReadRow row) {
        ProfilSISimpleDTO profilDTO = null;
        if (row.profilSIId() != null) {
            profilDTO = new ProfilSISimpleDTO(row.profilSIId(), row.profilSIName());
        }

        return EmploiDTOResponse.builder()
                .id(row.id())
                .emploi(row.emploiName())
                .direction(row.direction())
                .service(row.service())
                .domaine(row.domaine())
                .status(EmploiModel.Status.valueOf(row.status()))
                .profilSI(profilDTO)
                .dateCreated(row.dateCreated())
                .dateUpdated(row.dateUpdated())
                .build();
    }
}
