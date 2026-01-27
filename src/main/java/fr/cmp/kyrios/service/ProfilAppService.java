package fr.cmp.kyrios.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import fr.cmp.kyrios.model.Si.ProfilSIModel;
import fr.cmp.kyrios.util.EntityFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.cmp.kyrios.model.App.ProfilAppModel;
import fr.cmp.kyrios.model.App.ProfilAppProfilSI;
import fr.cmp.kyrios.model.App.dto.ProfilAppDTOCreate;
import fr.cmp.kyrios.model.App.dto.ProfilAppDTODeleteResponse;
import fr.cmp.kyrios.model.App.dto.ProfilAppDTOResponse;
import fr.cmp.kyrios.repository.ProfilAppProfilSIRepository;
import fr.cmp.kyrios.repository.ProfilAppRepository;

@Service
public class ProfilAppService {

    @Autowired
    private ProfilAppRepository profilAppRepository;

    @Autowired
    private ProfilAppProfilSIRepository profilAppProfilSIRepository;

    @Autowired
    private EntityFinder entityFinder;

    public List<ProfilAppModel> listAll() {
        return profilAppRepository.findAll();
    }

    public ProfilAppModel getById(int id) {
        return entityFinder.findProfilAppOrThrow(id);
    }

    public List<ProfilAppModel> getByApplication(int applicationId) {
        entityFinder.findApplicationOrThrow(applicationId);
        return profilAppRepository.findByApplicationId(applicationId);
    }

    @Transactional
    public ProfilAppModel create(ProfilAppDTOCreate dto) {
        if (profilAppRepository.findByNameAndApplicationId(dto.getName(), dto.getApplicationId()).isPresent()) {
            throw new IllegalArgumentException(
                    "Un profil d'application avec le nom '" + dto.getName() + "' existe déjà dans cette application");
        }

        ProfilAppModel profilApp = new ProfilAppModel();
        profilApp.setName(dto.getName());
        profilApp.setApplication(entityFinder.findApplicationOrThrow(dto.getApplicationId()));
        profilApp.setDateCreated(LocalDateTime.now());

        Set<Integer> uniqueIds = new HashSet<>();
        for (Integer profilSIId : dto.getProfilSIIds()) {
            if (!uniqueIds.add(profilSIId)) {
                throw new IllegalArgumentException(
                        "Le profil SI ID " + profilSIId + " est présent plusieurs fois dans la liste");
            }
        }

        for (Integer profilSIId : dto.getProfilSIIds()) {
            if (profilAppProfilSIRepository.existsByApplicationIdAndProfilSIId(profilApp.getApplication().getId(),
                    profilSIId)) {
                ProfilSIModel profilSI = entityFinder.findProfilSIOrThrow(profilSIId);
                throw new IllegalArgumentException(
                        "Le profil SI '" + profilSI.getName()
                                + "' est déjà associé à un profil applicatif dans l'application '"
                                + profilApp.getApplication().getName() + "'");
            }
        }

        createProfilSILiaisons(profilApp, dto.getProfilSIIds());

        return profilAppRepository.save(profilApp);
    }

    @Transactional
    public ProfilAppModel update(int id, ProfilAppDTOCreate dto) {
        ProfilAppModel updateProfilApp = getById(id);

        if (updateProfilApp.getApplication().getId() != dto.getApplicationId()) {
            throw new IllegalArgumentException(
                    "Impossible de changer l'application d'un profil applicatif");
        }

        if (!updateProfilApp.getName().equals(dto.getName())) {
            if (profilAppRepository.findByNameAndApplicationId(dto.getName(), dto.getApplicationId()).isPresent()) {
                throw new IllegalArgumentException(
                        "Un profil d'application avec le nom '" + dto.getName()
                                + "' existe déjà dans cette application");
            }
        }

        updateProfilApp.setName(dto.getName());
        updateProfilApp.setDateUpdated(LocalDateTime.now());

        updateProfilApp.getProfilSI().removeIf(r -> true);
        profilAppRepository.flush();

        createProfilSILiaisons(updateProfilApp, dto.getProfilSIIds());

        return profilAppRepository.save(updateProfilApp);
    }

    @Transactional
    public ProfilAppDTODeleteResponse delete(int id) {
        ProfilAppModel profilApp = getById(id);

        List<ProfilAppDTODeleteResponse.ProfilSIInfo> profilSIDetaches = new ArrayList<>();

        if (profilApp.getProfilSI() != null && !profilApp.getProfilSI().isEmpty()) {
            for (ProfilAppProfilSI liaison : profilApp.getProfilSI()) {
                profilSIDetaches.add(ProfilAppDTODeleteResponse.ProfilSIInfo.builder()
                        .id(liaison.getProfilSI().getId())
                        .name(liaison.getProfilSI().getName())
                        .build());
            }
        }

        profilAppRepository.delete(profilApp);

        String message = profilSIDetaches.isEmpty()
                ? "Profil d'application " + profilApp.getName() + " supprimé avec succès. Aucun profil SI n'était lié."
                : "Profil d'application " + profilApp.getName() + " supprimé avec succès. "
                        + profilSIDetaches.size() + " profil SI détaché.";
        return ProfilAppDTODeleteResponse.builder()
                .message(message)
                .profilsSIDetaches(profilSIDetaches)
                .build();
    }

    public ProfilAppDTOResponse toDTO(ProfilAppModel profilApp) {
        return ProfilAppDTOResponse.builder()
                .id(profilApp.getId())
                .name(profilApp.getName())
                .application(profilApp.getApplication().getName())
                .profilSI(profilApp.getProfilSI().stream()
                        .map(liaison -> liaison.getProfilSI().getName())
                        .collect(Collectors.toList()))
                .dateCreated(profilApp.getDateCreated())
                .dateUpdated(profilApp.getDateUpdated())
                .build();
    }

    private void createProfilSILiaisons(ProfilAppModel profilApp, List<Integer> profilSIIds) {
        for (Integer profilSIId : profilSIIds) {
            ProfilSIModel profilSI = entityFinder.findProfilSIOrThrow(profilSIId);
            ProfilAppProfilSI liaison = new ProfilAppProfilSI();
            liaison.setProfilApp(profilApp);
            liaison.setProfilSI(profilSI);
            liaison.setApplication(profilApp.getApplication());
            profilApp.getProfilSI().add(liaison);
        }
    }

}
