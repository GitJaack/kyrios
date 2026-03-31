package fr.cmp.kyrios.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.cmp.kyrios.dao.ReferenceDao;
import fr.cmp.kyrios.dao.ProfilAppDao;
import fr.cmp.kyrios.exception.ProfilAppNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.cmp.kyrios.model.App.dto.ProfilAppDTOCreate;
import fr.cmp.kyrios.model.App.dto.ProfilAppDTODeleteResponse;
import fr.cmp.kyrios.model.App.dto.ProfilAppDTOResponse;
import fr.cmp.kyrios.mapper.ProfilAppMapper;

@Service
public class ProfilAppService {
    private static final String PERMISSION_RESOURCE_NAME = "Niveau de permission";

    @Autowired
    private ProfilAppDao profilAppDao;

    @Autowired
    private ReferenceDao referenceDao;

    public List<ProfilAppDTOResponse> listAll() {
        return profilAppDao.findAll().stream()
                .map(row -> ProfilAppMapper.toDto(row, profilAppDao))
                .toList();
    }

    public List<ProfilAppDTOResponse> getByApplication(int applicationId) {
        if (!referenceDao.existsApplicationById(applicationId)) {
            throw new IllegalArgumentException("Application avec l'ID " + applicationId + " non trouvee");
        }
        return profilAppDao.findByApplicationId(applicationId).stream()
                .map(row -> ProfilAppMapper.toDto(row, profilAppDao))
                .toList();
    }

    public ProfilAppDTOResponse getById(int id) {
        ProfilAppDao.ProfilAppReadRow row = profilAppDao.findById(id)
                .orElseThrow(() -> new ProfilAppNotFoundException("Profil App avec l'ID " + id + " non trouvé"));
        return ProfilAppMapper.toDto(row, profilAppDao);
    }

    @Transactional
    public ProfilAppDTOResponse create(ProfilAppDTOCreate dto) {
        if (profilAppDao.existsByNameAndApplicationId(dto.getName(), dto.getApplicationId())) {
            throw new IllegalArgumentException(
                    "Un profil d'application avec le nom '" + dto.getName() + "' existe déjà dans cette application");
        }

        String applicationName = referenceDao.findApplicationNameById(dto.getApplicationId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Application avec l'ID " + dto.getApplicationId() + " non trouvee"));

        Set<Integer> uniqueIds = new HashSet<>();
        for (Integer profilSIId : dto.getProfilSIIds()) {
            if (!uniqueIds.add(profilSIId)) {
                throw new IllegalArgumentException(
                        "Le profil SI " + profilSIId + " est présent plusieurs fois dans la liste");
            }
        }

        for (Integer profilSIId : dto.getProfilSIIds()) {
            String profilSIName = referenceDao.findProfilSINameById(profilSIId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Profil SI avec l'ID " + profilSIId + " non trouve"));

            if (profilAppDao.existsProfilSIInApplication(dto.getApplicationId(), profilSIId)) {
                throw new IllegalArgumentException(
                        "Le profil SI '" + profilSIName
                                + "' est déjà associé à un profil applicatif dans l'application '"
                                + applicationName + "'");
            }
        }

        if (dto.getRessourceAppIds() != null && !dto.getRessourceAppIds().isEmpty()) {
            boolean permissionResourceSelected = false;
            for (Integer ressourceAppId : dto.getRessourceAppIds()) {
                ReferenceDao.RessourceAppRef ressourceApp = referenceDao
                        .findRessourceAppById(ressourceAppId)
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Ressource App avec l'ID " + ressourceAppId + " non trouvee"));
                if (ressourceApp.applicationId() != dto.getApplicationId()) {
                    throw new IllegalArgumentException(
                            "La ressource '" + ressourceApp.name() + "' n'appartient pas à l'application '"
                                    + applicationName + "'");
                }
                if (PERMISSION_RESOURCE_NAME.equals(ressourceApp.name())) {
                    permissionResourceSelected = true;
                }
            }

            if (permissionResourceSelected) {
                if (dto.getPermissionLevel() == null || dto.getPermissionLevel() < 0 || dto.getPermissionLevel() > 6) {
                    throw new IllegalArgumentException("Le niveau de permission doit etre compris entre 0 et 6");
                }
            }
        }

        int newProfilAppId = profilAppDao.insertProfilApp(dto.getName(), dto.getApplicationId(),
                LocalDateTime.now());

        for (Integer profilSIId : dto.getProfilSIIds()) {
            profilAppDao.insertProfilSILink(newProfilAppId, profilSIId, dto.getApplicationId());
        }

        if (dto.getRessourceAppIds() != null && !dto.getRessourceAppIds().isEmpty()) {
            for (Integer ressourceAppId : dto.getRessourceAppIds()) {
                ReferenceDao.RessourceAppRef ressourceApp = referenceDao
                        .findRessourceAppById(ressourceAppId)
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Ressource App avec l'ID " + ressourceAppId + " non trouvee"));

                Integer permissionLevel = PERMISSION_RESOURCE_NAME.equals(ressourceApp.name())
                        ? dto.getPermissionLevel()
                        : null;

                profilAppDao.insertRessourceLink(newProfilAppId, ressourceAppId, permissionLevel);
            }
        }

        return getById(newProfilAppId);
    }

    @Transactional
    public ProfilAppDTOResponse update(int id, ProfilAppDTOCreate dto) {
        ProfilAppDao.ProfilAppReadRow currentProfilApp = profilAppDao.findById(id)
                .orElseThrow(() -> new ProfilAppNotFoundException("Profil App avec l'ID " + id + " non trouvé"));

        if (currentProfilApp.applicationId() != dto.getApplicationId()) {
            throw new IllegalArgumentException(
                    "Impossible de changer l'application d'un profil applicatif");
        }

        String applicationName = referenceDao.findApplicationNameById(dto.getApplicationId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Application avec l'ID " + dto.getApplicationId() + " non trouvee"));

        if (!currentProfilApp.name().equals(dto.getName())) {
            if (profilAppDao.existsByNameAndApplicationIdExcludingId(dto.getName(), dto.getApplicationId(),
                    id)) {
                throw new IllegalArgumentException(
                        "Un profil d'application avec le nom '" + dto.getName()
                                + "' existe déjà dans cette application");
            }
        }

        Set<Integer> targetProfilSIIds = new HashSet<>();
        for (Integer profilSIId : dto.getProfilSIIds()) {
            if (!targetProfilSIIds.add(profilSIId)) {
                throw new IllegalArgumentException(
                        "Le profil SI " + profilSIId + " est présent plusieurs fois dans la liste");
            }
        }

        for (Integer profilSIId : targetProfilSIIds) {
            String profilSIName = referenceDao.findProfilSINameById(profilSIId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Profil SI avec l'ID " + profilSIId + " non trouve"));

            boolean linkedToAnotherProfilApp = profilAppDao
                    .existsProfilSIInApplicationExcludingProfilApp(dto.getApplicationId(), profilSIId, id);

            if (linkedToAnotherProfilApp) {
                throw new IllegalArgumentException(
                        "Le profil SI '" + profilSIName
                                + "' est déjà associé à un profil applicatif dans l'application '"
                                + applicationName + "'");
            }
        }

        if (dto.getRessourceAppIds() != null && !dto.getRessourceAppIds().isEmpty()) {
            boolean permissionResourceSelected = false;
            for (Integer ressourceAppId : dto.getRessourceAppIds()) {
                ReferenceDao.RessourceAppRef ressourceApp = referenceDao
                        .findRessourceAppById(ressourceAppId)
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Ressource App avec l'ID " + ressourceAppId + " non trouvee"));
                if (ressourceApp.applicationId() != dto.getApplicationId()) {
                    throw new IllegalArgumentException(
                            "La ressource '" + ressourceApp.name() + "' n'appartient pas à l'application '"
                                    + applicationName + "'");
                }
                if (PERMISSION_RESOURCE_NAME.equals(ressourceApp.name())) {
                    permissionResourceSelected = true;
                }
            }

            if (permissionResourceSelected) {
                if (dto.getPermissionLevel() == null || dto.getPermissionLevel() < 0 || dto.getPermissionLevel() > 6) {
                    throw new IllegalArgumentException("Le niveau de permission doit etre compris entre 0 et 6");
                }
            }
        }

        profilAppDao.updateProfilApp(id, dto.getName(), LocalDateTime.now());

        profilAppDao.deleteProfilSIByProfilAppId(id);
        for (Integer profilSIId : dto.getProfilSIIds()) {
            profilAppDao.insertProfilSILink(id, profilSIId, dto.getApplicationId());
        }

        profilAppDao.deleteRessourcesByProfilAppId(id);

        if (dto.getRessourceAppIds() != null && !dto.getRessourceAppIds().isEmpty()) {
            for (Integer ressourceAppId : dto.getRessourceAppIds()) {
                ReferenceDao.RessourceAppRef ressourceApp = referenceDao
                        .findRessourceAppById(ressourceAppId)
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Ressource App avec l'ID " + ressourceAppId + " non trouvee"));

                Integer permissionLevel = PERMISSION_RESOURCE_NAME.equals(ressourceApp.name())
                        ? dto.getPermissionLevel()
                        : null;

                profilAppDao.insertRessourceLink(id, ressourceAppId, permissionLevel);
            }
        }

        return getById(id);
    }

    @Transactional
    public ProfilAppDTODeleteResponse delete(int id) {
        ProfilAppDao.ProfilAppReadRow profilApp = profilAppDao.findById(id)
                .orElseThrow(() -> new ProfilAppNotFoundException("Profil App avec l'ID " + id + " non trouvé"));

        List<ProfilAppDTODeleteResponse.ProfilSIInfo> profilSIDetaches = new ArrayList<>();

        List<ProfilAppDao.ProfilSIReadRow> linkedProfilsSI = profilAppDao
                .findProfilSIByProfilAppId(id);
        if (!linkedProfilsSI.isEmpty()) {
            for (ProfilAppDao.ProfilSIReadRow profilSI : linkedProfilsSI) {
                profilSIDetaches.add(ProfilAppDTODeleteResponse.ProfilSIInfo.builder()
                        .id(profilSI.id())
                        .name(profilSI.name())
                        .build());
            }
        }

        profilAppDao.deleteRessourcesByProfilAppId(id);
        profilAppDao.deleteProfilSIByProfilAppId(id);
        profilAppDao.deleteProfilAppById(id);

        String message = profilSIDetaches.isEmpty()
                ? "Profil d'application " + profilApp.name() + " supprimé avec succès. Aucun profil SI n'était lié."
                : "Profil d'application " + profilApp.name() + " supprimé avec succès. "
                        + profilSIDetaches.size() + " profil SI détaché.";
        return ProfilAppDTODeleteResponse.builder()
                .message(message)
                .profilsSIDetaches(profilSIDetaches)
                .build();
    }

}
