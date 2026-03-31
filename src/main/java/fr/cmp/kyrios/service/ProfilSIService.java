package fr.cmp.kyrios.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.cmp.kyrios.dao.ReferenceDao;
import fr.cmp.kyrios.dao.ProfilSIDao;
import fr.cmp.kyrios.mapper.ProfilSIMapper;
import fr.cmp.kyrios.model.Si.RessourceSIModel;
import fr.cmp.kyrios.model.Si.dto.profilSI.ProfilSIDTO;
import fr.cmp.kyrios.model.Si.dto.profilSI.ProfilSIDTOCreate;
import fr.cmp.kyrios.model.Si.dto.profilSI.ProfilSIDTOCreateResponse;
import fr.cmp.kyrios.model.Si.dto.profilSI.ProfilSIDTODeleteResponse;
import fr.cmp.kyrios.model.Si.dto.profilSI.ProfilSIDTOResponse;
import fr.cmp.kyrios.model.Si.dto.profilSI.ProfilSIUpdateDTO;

@Service
public class ProfilSIService {
    @Autowired
    private ProfilSIDao profilSIDao;

    @Autowired
    private ReferenceDao referenceDao;

    public List<ProfilSIDTOResponse> listAll() {
        return profilSIDao.findAll().stream()
                .map(row -> ProfilSIMapper.toDto(row, profilSIDao))
                .toList();
    }

    public List<ProfilSIDTOResponse> getByDirection(int directionId) {
        if (!referenceDao.existsDirectionById(directionId)) {
            throw new IllegalArgumentException("Direction avec l'ID " + directionId + " non trouvee");
        }
        return profilSIDao.findByDirectionId(directionId).stream()
                .map(row -> ProfilSIMapper.toDto(row, profilSIDao))
                .toList();
    }

    public ProfilSIDTOResponse getById(int id) {
        ProfilSIDao.ProfilSIReadRow row = profilSIDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Profil SI avec l'ID " + id + " non trouvé"));
        return ProfilSIMapper.toDto(row, profilSIDao);
    }

    @Transactional
    public ProfilSIDTOCreateResponse create(ProfilSIDTOCreate dto) {
        String directionName = referenceDao.findDirectionNameById(dto.getEmploi().getDirection())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Direction avec l'ID " + dto.getEmploi().getDirection() + " non trouvee"));

        if (profilSIDao.existsByName(dto.getProfilSI().getProfilSI())) {
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
            ProfilSIDao.ProfilSIReadRow profilSource = profilSIDao
                    .findById(dto.getProfilSI().getProfilSISourceId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Profil SI source avec l'ID " + dto.getProfilSI().getProfilSISourceId() + " non trouvé"));

            if (profilSource.directionId() == null || profilSource.directionId() != dto.getEmploi().getDirection()) {
                throw new IllegalArgumentException("Le profil source doit être dans la même direction");
            }

            for (ProfilSIDao.RessourceReadRow sourceRessource : profilSIDao
                    .findRessourcesByProfilSIId(profilSource.id())) {
                ressourcesMap.put(sourceRessource.id(),
                        RessourceSIModel.TypeAcces.valueOf(sourceRessource.typeAcces()));
            }
        } else {
            for (ProfilSIDao.RessourceSimpleRow ressource : profilSIDao
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
                if (!referenceDao.existsRessourceSIById(ressourceDTO.getRessourceId())) {
                    throw new IllegalArgumentException(
                            "Ressource SI avec l'ID " + ressourceDTO.getRessourceId() + " non trouvee");
                }
                ressourcesMap.put(ressourceDTO.getRessourceId(), ressourceDTO.getTypeAcces());
            }
        }

        int profilSIId = profilSIDao.insertProfilSI(
                dto.getProfilSI().getProfilSI(),
                dto.getEmploi().getDirection(),
                LocalDateTime.now());

        for (var entry : ressourcesMap.entrySet()) {
            profilSIDao.insertProfilSIRessource(profilSIId, entry.getKey(), entry.getValue().name());
        }

        int emploiId = profilSIDao.insertEmploi(
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
            serviceName = referenceDao.findServiceNameById(dto.getEmploi().getService())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Service avec l'ID " + dto.getEmploi().getService() + " non trouve"));
        }
        String domaineName = null;
        if (dto.getEmploi().getDomaine() != null) {
            domaineName = referenceDao.findDomaineNameById(dto.getEmploi().getDomaine())
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
        ProfilSIDao.ProfilSIReadRow profilSI = profilSIDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Profil SI avec l'ID " + id + " non trouvé"));

        if (!profilSI.name().equals(dto.getProfilSI())) {
            if (profilSIDao.existsByNameExcludingId(dto.getProfilSI(), id)) {
                throw new IllegalArgumentException("Un profil SI avec le nom '" + dto.getProfilSI() + "' existe déjà");
            }
        }

        profilSIDao.updateProfilSI(id, dto.getProfilSI(), LocalDateTime.now());
        profilSIDao.deleteProfilSIRessourcesByProfilSIId(id);

        if (dto.getRessources() != null && !dto.getRessources().isEmpty()) {
            Set<Integer> ressourceIds = new HashSet<>();
            for (var ressourceDTO : dto.getRessources()) {
                if (!ressourceIds.add(ressourceDTO.getRessourceId())) {
                    throw new IllegalArgumentException(
                            "La ressource ID " + ressourceDTO.getRessourceId() + " est présente plusieurs fois");
                }
            }

            for (var ressourceDTO : dto.getRessources()) {
                if (!referenceDao.existsRessourceSIById(ressourceDTO.getRessourceId())) {
                    throw new IllegalArgumentException(
                            "Ressource SI avec l'ID " + ressourceDTO.getRessourceId() + " non trouvee");
                }
                profilSIDao.insertProfilSIRessource(id, ressourceDTO.getRessourceId(),
                        ressourceDTO.getTypeAcces().name());
            }
        }

        return getById(id);
    }

    @Transactional
    public ProfilSIDTODeleteResponse delete(int id) {
        ProfilSIDao.ProfilSIReadRow profilSI = profilSIDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Profil SI avec l'ID " + id + " non trouvé"));

        List<ProfilSIDTODeleteResponse.EmploiInfo> emploisDetaches = new ArrayList<>();

        List<ProfilSIDao.EmploiReadRow> emplois = profilSIDao.findEmploisByProfilSIId(id);
        if (!emplois.isEmpty()) {
            for (ProfilSIDao.EmploiReadRow emploi : emplois) {
                emploisDetaches.add(ProfilSIDTODeleteResponse.EmploiInfo.builder()
                        .id(emploi.id())
                        .name(emploi.emploiName())
                        .build());
            }
        }

        profilSIDao.detachEmploisByProfilSIId(id);
        profilSIDao.deleteProfilAppLinksByProfilSIId(id);
        profilSIDao.deleteProfilSIRessourcesByProfilSIId(id);
        profilSIDao.deleteProfilSIById(id);

        String message = emploisDetaches.isEmpty()
                ? "Profil SI '" + profilSI.name() + "' supprimé avec succès. Aucun emploi n'était lié."
                : "Profil SI '" + profilSI.name() + "' supprimé avec succès. " + emploisDetaches.size()
                        + " emploi ont été détaché.";

        return ProfilSIDTODeleteResponse.builder()
                .message(message)
                .emploisDetaches(emploisDetaches)
                .build();
    }

}
