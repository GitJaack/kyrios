package fr.cmp.kyrios.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.cmp.kyrios.dao.ReferenceDao;
import fr.cmp.kyrios.dao.ProfilSIDao;
import fr.cmp.kyrios.model.App.dto.ProfilAppDTOSimple;
import fr.cmp.kyrios.model.Si.RessourceSIModel;
import fr.cmp.kyrios.model.Si.dto.profilSI.ProfilSIDTO;
import fr.cmp.kyrios.model.Si.dto.profilSI.ProfilSIDTOCreate;
import fr.cmp.kyrios.model.Si.dto.profilSI.ProfilSIDTOCreateResponse;
import fr.cmp.kyrios.model.Si.dto.profilSI.ProfilSIDTODeleteResponse;
import fr.cmp.kyrios.model.Si.dto.profilSI.ProfilSIDTOResponse;
import fr.cmp.kyrios.model.Si.dto.profilSI.ProfilSIUpdateDTO;
import fr.cmp.kyrios.model.Si.dto.ressourceSI.RessourceSIDTO;

@Service
public class ProfilSIService {
    @Autowired
    private ProfilSIDao profilSIJdbcRepository;

    @Autowired
    private ReferenceDao jdbcReferenceRepository;

    public List<ProfilSIDTOResponse> listAll() {
        return profilSIJdbcRepository.findAll().stream()
                .map(this::toResponseDTOFromJdbcRow)
                .toList();
    }

    public List<ProfilSIDTOResponse> getByDirectionReadOnlyJdbc(int directionId) {
        if (!jdbcReferenceRepository.existsDirectionById(directionId)) {
            throw new IllegalArgumentException("Direction avec l'ID " + directionId + " non trouvee");
        }
        return profilSIJdbcRepository.findByDirectionId(directionId).stream()
                .map(this::toResponseDTOFromJdbcRow)
                .toList();
    }

    public ProfilSIDTOResponse getById(int id) {
        ProfilSIDao.ProfilSIReadRow row = profilSIJdbcRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Profil SI avec l'ID " + id + " non trouvé"));
        return toResponseDTOFromJdbcRow(row);
    }

    @Transactional
    public ProfilSIDTOCreateResponse create(ProfilSIDTOCreate dto) {
        String directionName = jdbcReferenceRepository.findDirectionNameById(dto.getEmploi().getDirection())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Direction avec l'ID " + dto.getEmploi().getDirection() + " non trouvee"));

        if (profilSIJdbcRepository.existsByName(dto.getProfilSI().getProfilSI())) {
            throw new IllegalArgumentException(
                    "Un profil SI avec le nom '" + dto.getProfilSI().getProfilSI() + "' existe déjà");
        }

        if (dto.getProfilSI().getModeCreation() == ProfilSIDTO.ModeCreation.COPIER) {
            if (dto.getProfilSI().getProfilSISourceId() == null) {
                throw new IllegalArgumentException("L'ID du profil source est requis en mode COPIER");
            }
        }

        java.util.Map<Integer, RessourceSIModel.TypeAcces> ressourcesMap = new java.util.LinkedHashMap<>();

        if (dto.getProfilSI().getModeCreation() == ProfilSIDTO.ModeCreation.COPIER) {
            ProfilSIDao.ProfilSIReadRow profilSource = profilSIJdbcRepository
                    .findById(dto.getProfilSI().getProfilSISourceId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Profil SI source avec l'ID " + dto.getProfilSI().getProfilSISourceId() + " non trouvé"));

            if (profilSource.directionId() == null || profilSource.directionId() != dto.getEmploi().getDirection()) {
                throw new IllegalArgumentException("Le profil source doit être dans la même direction");
            }

            for (ProfilSIDao.RessourceReadRow sourceRessource : profilSIJdbcRepository
                    .findRessourcesByProfilSIId(profilSource.id())) {
                ressourcesMap.put(sourceRessource.id(),
                        RessourceSIModel.TypeAcces.valueOf(sourceRessource.typeAcces()));
            }
        } else {
            for (ProfilSIDao.RessourceSimpleRow ressource : profilSIJdbcRepository
                    .findDefaultRessourcesByDirectionId(dto.getEmploi().getDirection())) {
                ressourcesMap.put(ressource.id(), RessourceSIModel.TypeAcces.valueOf(ressource.typeAcces()));
            }
        }

        if (dto.getProfilSI().getRessources() != null && !dto.getProfilSI().getRessources().isEmpty()) {
            Set<Integer> ressourceIds = new HashSet<>();
            for (var ressourceDTO : dto.getProfilSI().getRessources()) {
                if (!ressourceIds.add(ressourceDTO.getRessourceId())) {
                    throw new IllegalArgumentException(
                            "La ressource ID " + ressourceDTO.getRessourceId() + " est présente plusieurs fois");
                }
            }

            for (var ressourceDTO : dto.getProfilSI().getRessources()) {
                if (!jdbcReferenceRepository.existsRessourceSIById(ressourceDTO.getRessourceId())) {
                    throw new IllegalArgumentException(
                            "Ressource SI avec l'ID " + ressourceDTO.getRessourceId() + " non trouvee");
                }
                ressourcesMap.put(ressourceDTO.getRessourceId(), ressourceDTO.getTypeAcces());
            }
        }

        int profilSIId = profilSIJdbcRepository.insertProfilSI(
                dto.getProfilSI().getProfilSI(),
                dto.getEmploi().getDirection(),
                LocalDateTime.now());

        for (var entry : ressourcesMap.entrySet()) {
            profilSIJdbcRepository.insertProfilSIRessource(profilSIId, entry.getKey(), entry.getValue().name());
        }

        int emploiId = profilSIJdbcRepository.insertEmploi(
                dto.getEmploi().getEmploi(),
                dto.getEmploi().getDirection(),
                dto.getEmploi().getService(),
                dto.getEmploi().getDomaine(),
                dto.getEmploi().getStatus().name(),
                profilSIId,
                LocalDateTime.now());

        var profilDTO = getById(profilSIId);
        String serviceName = null;
        if (dto.getEmploi().getService() != null) {
            serviceName = jdbcReferenceRepository.findServiceNameById(dto.getEmploi().getService())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Service avec l'ID " + dto.getEmploi().getService() + " non trouve"));
        }
        String domaineName = null;
        if (dto.getEmploi().getDomaine() != null) {
            domaineName = jdbcReferenceRepository.findDomaineNameById(dto.getEmploi().getDomaine())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Domaine avec l'ID " + dto.getEmploi().getDomaine() + " non trouve"));
        }

        return ProfilSIDTOCreateResponse.builder()
                .profilSI(profilDTO)
                .idEmploi(emploiId)
                .emploi(dto.getEmploi().getEmploi())
                .direction(directionName)
                .service(serviceName)
                .domaine(domaineName)
                .status(dto.getEmploi().getStatus())
                .build();
    }

    @Transactional
    public ProfilSIDTOResponse update(int id, ProfilSIUpdateDTO dto) {
        ProfilSIDao.ProfilSIReadRow profilSI = profilSIJdbcRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Profil SI avec l'ID " + id + " non trouvé"));

        if (!profilSI.name().equals(dto.getProfilSI())) {
            if (profilSIJdbcRepository.existsByNameExcludingId(dto.getProfilSI(), id)) {
                throw new IllegalArgumentException("Un profil SI avec le nom '" + dto.getProfilSI() + "' existe déjà");
            }
        }

        profilSIJdbcRepository.updateProfilSI(id, dto.getProfilSI(), LocalDateTime.now());
        profilSIJdbcRepository.deleteProfilSIRessourcesByProfilSIId(id);

        if (dto.getRessources() != null && !dto.getRessources().isEmpty()) {
            Set<Integer> ressourceIds = new HashSet<>();
            for (var ressourceDTO : dto.getRessources()) {
                if (!ressourceIds.add(ressourceDTO.getRessourceId())) {
                    throw new IllegalArgumentException(
                            "La ressource ID " + ressourceDTO.getRessourceId() + " est présente plusieurs fois");
                }
            }

            for (var ressourceDTO : dto.getRessources()) {
                if (!jdbcReferenceRepository.existsRessourceSIById(ressourceDTO.getRessourceId())) {
                    throw new IllegalArgumentException(
                            "Ressource SI avec l'ID " + ressourceDTO.getRessourceId() + " non trouvee");
                }
                profilSIJdbcRepository.insertProfilSIRessource(id, ressourceDTO.getRessourceId(),
                        ressourceDTO.getTypeAcces().name());
            }
        }

        return getById(id);
    }

    @Transactional
    public ProfilSIDTODeleteResponse delete(int id) {
        ProfilSIDao.ProfilSIReadRow profilSI = profilSIJdbcRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Profil SI avec l'ID " + id + " non trouvé"));

        List<ProfilSIDTODeleteResponse.EmploiInfo> emploisDetaches = new ArrayList<>();

        List<ProfilSIDao.EmploiReadRow> emplois = profilSIJdbcRepository.findEmploisByProfilSIId(id);
        if (!emplois.isEmpty()) {
            for (ProfilSIDao.EmploiReadRow emploi : emplois) {
                emploisDetaches.add(ProfilSIDTODeleteResponse.EmploiInfo.builder()
                        .id(emploi.id())
                        .name(emploi.emploiName())
                        .build());
            }
        }

        profilSIJdbcRepository.detachEmploisByProfilSIId(id);
        profilSIJdbcRepository.deleteProfilAppLinksByProfilSIId(id);
        profilSIJdbcRepository.deleteProfilSIRessourcesByProfilSIId(id);
        profilSIJdbcRepository.deleteProfilSIById(id);

        String message = emploisDetaches.isEmpty()
                ? "Profil SI '" + profilSI.name() + "' supprimé avec succès. Aucun emploi n'était lié."
                : "Profil SI '" + profilSI.name() + "' supprimé avec succès. " + emploisDetaches.size()
                        + " emploi ont été détaché.";

        return ProfilSIDTODeleteResponse.builder()
                .message(message)
                .emploisDetaches(emploisDetaches)
                .build();
    }

    private ProfilSIDTOResponse toResponseDTOFromJdbcRow(ProfilSIDao.ProfilSIReadRow row) {
        List<RessourceSIDTO> ressourcesDTO = profilSIJdbcRepository.findRessourcesByProfilSIId(row.id()).stream()
                .map(r -> RessourceSIDTO.builder()
                        .id(r.id())
                        .categorie(r.categorie())
                        .name(r.name())
                        .typeAcces(fr.cmp.kyrios.model.Si.RessourceSIModel.TypeAcces.valueOf(r.typeAcces()))
                        .build())
                .collect(Collectors.toList());

        List<ProfilAppDTOSimple> profilAppsDTO = profilSIJdbcRepository.findProfilAppsByProfilSIId(row.id()).stream()
                .map(p -> ProfilAppDTOSimple.builder()
                        .id(p.id())
                        .name(p.name())
                        .application(p.application())
                        .build())
                .collect(Collectors.toList());

        List<ProfilSIDTOResponse.EmploiInfo> emploisDTO = profilSIJdbcRepository.findEmploisByProfilSIId(row.id())
                .stream()
                .map(e -> ProfilSIDTOResponse.EmploiInfo.builder()
                        .id(e.id())
                        .emploiName(e.emploiName())
                        .build())
                .collect(Collectors.toList());

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
