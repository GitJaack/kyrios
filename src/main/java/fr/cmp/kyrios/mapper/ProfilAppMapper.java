package fr.cmp.kyrios.mapper;

import java.util.List;

import fr.cmp.kyrios.dao.ProfilAppDao;
import fr.cmp.kyrios.model.App.dto.ProfilAppDTOResponse;
import fr.cmp.kyrios.model.App.dto.RessourceAppDTOResponse;
import fr.cmp.kyrios.model.Emploi.dto.ProfilSISimpleDTO;

public final class ProfilAppMapper {
    private ProfilAppMapper() {
    }

    public static ProfilAppDTOResponse toDto(ProfilAppDao.ProfilAppReadRow row, ProfilAppDao profilAppDao) {
        List<ProfilAppDao.RessourceAppReadRow> ressourcesRows = profilAppDao.findRessourcesByProfilAppId(row.id());

        List<RessourceAppDTOResponse> ressourcesAppDetails = ressourcesRows.stream()
                .map(ressource -> RessourceAppDTOResponse.builder()
                        .id(ressource.id())
                        .name(ressource.name())
                        .description(ressource.description())
                        .application(ressource.applicationName())
                        .categoryId(ressource.categoryId())
                        .category(ressource.categoryName())
                        .permissionLevel(ressource.permissionLevel())
                        .build())
                .toList();

        Integer permissionLevel = ressourcesRows.stream()
                .filter(ressource -> "Niveau de permission".equals(ressource.name()))
                .map(ProfilAppDao.RessourceAppReadRow::permissionLevel)
                .filter(level -> level != null)
                .findFirst()
                .orElse(null);

        return ProfilAppDTOResponse.builder()
                .id(row.id())
                .name(row.name())
                .application(row.applicationName())
                .applicationId(row.applicationId())
                .profilSI(profilAppDao.findProfilSIByProfilAppId(row.id()).stream()
                        .map(profilSI -> new ProfilSISimpleDTO(profilSI.id(), profilSI.name()))
                        .toList())
                .ressourcesApp(ressourcesAppDetails.stream()
                        .map(RessourceAppDTOResponse::getName)
                        .toList())
                .ressourcesAppDetails(ressourcesAppDetails)
                .permissionLevel(permissionLevel)
                .dateCreated(row.dateCreated())
                .dateUpdated(row.dateUpdated())
                .build();
    }
}
