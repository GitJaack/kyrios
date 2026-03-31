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
        List<RessourceAppDTOResponse> ressourcesAppDetails = profilAppDao
                .findRessourcesByProfilAppId(row.id()).stream()
                .map(ressource -> RessourceAppDTOResponse.builder()
                        .id(ressource.id())
                        .name(ressource.name())
                        .description(ressource.description())
                        .application(ressource.applicationName())
                        .build())
                .toList();

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
                .dateCreated(row.dateCreated())
                .dateUpdated(row.dateUpdated())
                .build();
    }
}
