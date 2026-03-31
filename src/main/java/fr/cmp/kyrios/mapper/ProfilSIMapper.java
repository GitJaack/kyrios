package fr.cmp.kyrios.mapper;

import java.util.List;

import fr.cmp.kyrios.dao.ProfilSIDao;
import fr.cmp.kyrios.model.App.dto.ProfilAppDTOSimple;
import fr.cmp.kyrios.model.Si.RessourceSIModel;
import fr.cmp.kyrios.model.Si.dto.profilSI.ProfilSIDTOResponse;
import fr.cmp.kyrios.model.Si.dto.ressourceSI.RessourceSIDTO;

public final class ProfilSIMapper {
    private ProfilSIMapper() {
    }

    public static ProfilSIDTOResponse toDto(ProfilSIDao.ProfilSIReadRow row, ProfilSIDao profilSIDao) {
        List<RessourceSIDTO> ressourcesDTO = profilSIDao.findRessourcesByProfilSIId(row.id()).stream()
                .map(r -> RessourceSIDTO.builder()
                        .id(r.id())
                        .categorie(r.categorie())
                        .name(r.name())
                        .typeAcces(RessourceSIModel.TypeAcces.valueOf(r.typeAcces()))
                        .build())
                .toList();

        List<ProfilAppDTOSimple> profilAppsDTO = profilSIDao.findProfilAppsByProfilSIId(row.id()).stream()
                .map(p -> ProfilAppDTOSimple.builder()
                        .id(p.id())
                        .name(p.name())
                        .application(p.application())
                        .build())
                .toList();

        List<ProfilSIDTOResponse.EmploiInfo> emploisDTO = profilSIDao.findEmploisByProfilSIId(row.id()).stream()
                .map(e -> ProfilSIDTOResponse.EmploiInfo.builder()
                        .id(e.id())
                        .emploiName(e.emploiName())
                        .build())
                .toList();

        return ProfilSIDTOResponse.builder()
                .idProfilSI(row.id())
                .name(row.name())
                .direction(row.direction())
                .ressources(ressourcesDTO)
                .profilApps(profilAppsDTO)
                .emplois(emploisDTO)
                .dateCreated(row.dateCreated())
                .dateUpdated(row.dateUpdated())
                .build();
    }
}
