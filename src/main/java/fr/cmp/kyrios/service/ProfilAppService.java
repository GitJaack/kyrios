package fr.cmp.kyrios.service;

import java.time.LocalDateTime;
import java.util.List;
import fr.cmp.kyrios.util.EntityFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.cmp.kyrios.model.App.ProfilAppModel;
import fr.cmp.kyrios.model.App.dto.ProfilAppDTOCreate;
import fr.cmp.kyrios.model.App.dto.ProfilAppDTOResponse;
import fr.cmp.kyrios.repository.ProfilAppRepository;

@Service
public class ProfilAppService {

    @Autowired
    private ProfilAppRepository profilAppRepository;

    @Autowired
    private EntityFinder entityFinder;

    public List<ProfilAppModel> listAll() {
        return profilAppRepository.findAll();
    }

    public ProfilAppModel getById(int id) {
        return entityFinder.findProfilAppOrThrow(id);
    }

    @Transactional
    public ProfilAppModel create(ProfilAppDTOCreate dto) {
        ProfilAppModel profilApp = new ProfilAppModel();
        profilApp.setName(dto.getName());
        profilApp.setApplication(entityFinder.findApplicationOrThrow(dto.getApplicationId()));
        profilApp.setProfilSI(entityFinder.findProfilSIOrThrow(dto.getProfilSIId()));
        profilApp.setDateCreated(LocalDateTime.now());
        return profilAppRepository.save(profilApp);
    }

    public ProfilAppDTOResponse toDTO(ProfilAppModel profilApp) {
        return ProfilAppDTOResponse.builder()
                .id(profilApp.getId())
                .name(profilApp.getName())
                .application(profilApp.getApplication().getName())
                .profilSI(profilApp.getProfilSI().getName())
                .dateCreated(profilApp.getDateCreated())
                .dateUpdated(profilApp.getDateUpdated())
                .build();
    }
}
