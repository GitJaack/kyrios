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
    @Autowired
    private ProfilAppDao profilAppJdbcRepository;

    @Autowired
    private ReferenceDao jdbcReferenceRepository;

    public List<ProfilAppDTOResponse> listAll() {
        return profilAppJdbcRepository.findAll().stream()
                .map(row -> ProfilAppMapper.toDto(row, profilAppJdbcRepository))
                .toList();
    }

    public List<ProfilAppDTOResponse> getByApplicationReadOnlyJdbc(int applicationId) {
        if (!jdbcReferenceRepository.existsApplicationById(applicationId)) {
            throw new IllegalArgumentException("Application avec l'ID " + applicationId + " non trouvee");
        }
        return profilAppJdbcRepository.findByApplicationId(applicationId).stream()
                .map(row -> ProfilAppMapper.toDto(row, profilAppJdbcRepository))
                .toList();
    }

    public ProfilAppDTOResponse getById(int id) {
        ProfilAppDao.ProfilAppReadRow row = profilAppJdbcRepository.findById(id)
                .orElseThrow(() -> new ProfilAppNotFoundException("Profil App avec l'ID " + id + " non trouvé"));
        return ProfilAppMapper.toDto(row, profilAppJdbcRepository);
    }

    @Transactional
    public ProfilAppDTOResponse create(ProfilAppDTOCreate dto) {
        if (profilAppJdbcRepository.existsByNameAndApplicationId(dto.getName(), dto.getApplicationId())) {
            throw new IllegalArgumentException(
                    "Un profil d'application avec le nom '" + dto.getName() + "' existe déjà dans cette application");
        }

        String applicationName = jdbcReferenceRepository.findApplicationNameById(dto.getApplicationId())
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
            String profilSIName = jdbcReferenceRepository.findProfilSINameById(profilSIId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Profil SI avec l'ID " + profilSIId + " non trouve"));

            if (profilAppJdbcRepository.existsProfilSIInApplication(dto.getApplicationId(), profilSIId)) {
                throw new IllegalArgumentException(
                        "Le profil SI '" + profilSIName
                                + "' est déjà associé à un profil applicatif dans l'application '"
                                + applicationName + "'");
            }
        }

        if (dto.getRessourceAppIds() != null && !dto.getRessourceAppIds().isEmpty()) {
            for (Integer ressourceAppId : dto.getRessourceAppIds()) {
                ReferenceDao.RessourceAppRef ressourceApp = jdbcReferenceRepository
                        .findRessourceAppById(ressourceAppId)
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Ressource App avec l'ID " + ressourceAppId + " non trouvee"));
                if (ressourceApp.applicationId() != dto.getApplicationId()) {
                    throw new IllegalArgumentException(
                            "La ressource '" + ressourceApp.name() + "' n'appartient pas à l'application '"
                                    + applicationName + "'");
                }
            }
        }

        int newProfilAppId = profilAppJdbcRepository.insertProfilApp(dto.getName(), dto.getApplicationId(),
                LocalDateTime.now());

        for (Integer profilSIId : dto.getProfilSIIds()) {
            profilAppJdbcRepository.insertProfilSILink(newProfilAppId, profilSIId, dto.getApplicationId());
        }

        if (dto.getRessourceAppIds() != null && !dto.getRessourceAppIds().isEmpty()) {
            for (Integer ressourceAppId : dto.getRessourceAppIds()) {
                profilAppJdbcRepository.insertRessourceLink(newProfilAppId, ressourceAppId);
            }
        }

        return getById(newProfilAppId);
    }

    @Transactional
    public ProfilAppDTOResponse update(int id, ProfilAppDTOCreate dto) {
        ProfilAppDao.ProfilAppReadRow currentProfilApp = profilAppJdbcRepository.findById(id)
                .orElseThrow(() -> new ProfilAppNotFoundException("Profil App avec l'ID " + id + " non trouvé"));

        if (currentProfilApp.applicationId() != dto.getApplicationId()) {
            throw new IllegalArgumentException(
                    "Impossible de changer l'application d'un profil applicatif");
        }

        String applicationName = jdbcReferenceRepository.findApplicationNameById(dto.getApplicationId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Application avec l'ID " + dto.getApplicationId() + " non trouvee"));

        if (!currentProfilApp.name().equals(dto.getName())) {
            if (profilAppJdbcRepository.existsByNameAndApplicationIdExcludingId(dto.getName(), dto.getApplicationId(),
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
            String profilSIName = jdbcReferenceRepository.findProfilSINameById(profilSIId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Profil SI avec l'ID " + profilSIId + " non trouve"));

            boolean linkedToAnotherProfilApp = profilAppJdbcRepository
                    .existsProfilSIInApplicationExcludingProfilApp(dto.getApplicationId(), profilSIId, id);

            if (linkedToAnotherProfilApp) {
                throw new IllegalArgumentException(
                        "Le profil SI '" + profilSIName
                                + "' est déjà associé à un profil applicatif dans l'application '"
                                + applicationName + "'");
            }
        }

        if (dto.getRessourceAppIds() != null && !dto.getRessourceAppIds().isEmpty()) {
            for (Integer ressourceAppId : dto.getRessourceAppIds()) {
                ReferenceDao.RessourceAppRef ressourceApp = jdbcReferenceRepository
                        .findRessourceAppById(ressourceAppId)
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Ressource App avec l'ID " + ressourceAppId + " non trouvee"));
                if (ressourceApp.applicationId() != dto.getApplicationId()) {
                    throw new IllegalArgumentException(
                            "La ressource '" + ressourceApp.name() + "' n'appartient pas à l'application '"
                                    + applicationName + "'");
                }
            }
        }

        profilAppJdbcRepository.updateProfilApp(id, dto.getName(), LocalDateTime.now());

        profilAppJdbcRepository.deleteProfilSIByProfilAppId(id);
        for (Integer profilSIId : dto.getProfilSIIds()) {
            profilAppJdbcRepository.insertProfilSILink(id, profilSIId, dto.getApplicationId());
        }

        profilAppJdbcRepository.deleteRessourcesByProfilAppId(id);

        if (dto.getRessourceAppIds() != null && !dto.getRessourceAppIds().isEmpty()) {
            for (Integer ressourceAppId : dto.getRessourceAppIds()) {
                profilAppJdbcRepository.insertRessourceLink(id, ressourceAppId);
            }
        }

        return getById(id);
    }

    @Transactional
    public ProfilAppDTODeleteResponse delete(int id) {
        ProfilAppDao.ProfilAppReadRow profilApp = profilAppJdbcRepository.findById(id)
                .orElseThrow(() -> new ProfilAppNotFoundException("Profil App avec l'ID " + id + " non trouvé"));

        List<ProfilAppDTODeleteResponse.ProfilSIInfo> profilSIDetaches = new ArrayList<>();

        List<ProfilAppDao.ProfilSIReadRow> linkedProfilsSI = profilAppJdbcRepository
                .findProfilSIByProfilAppId(id);
        if (!linkedProfilsSI.isEmpty()) {
            for (ProfilAppDao.ProfilSIReadRow profilSI : linkedProfilsSI) {
                profilSIDetaches.add(ProfilAppDTODeleteResponse.ProfilSIInfo.builder()
                        .id(profilSI.id())
                        .name(profilSI.name())
                        .build());
            }
        }

        profilAppJdbcRepository.deleteRessourcesByProfilAppId(id);
        profilAppJdbcRepository.deleteProfilSIByProfilAppId(id);
        profilAppJdbcRepository.deleteProfilAppById(id);

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
