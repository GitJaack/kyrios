package fr.cmp.kyrios.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.cmp.kyrios.model.Emploi.EmploiModel;
import fr.cmp.kyrios.model.Emploi.dto.EmploiDTOCreate;
import fr.cmp.kyrios.model.Emploi.dto.EmploiDTOResponse;
import fr.cmp.kyrios.model.Emploi.dto.ProfilSISimpleDTO;
import fr.cmp.kyrios.model.Si.ProfilSIModel;
import fr.cmp.kyrios.repository.EmploiRepository;
import fr.cmp.kyrios.util.EntityFinder;

@Service
public class EmploiService {
    @Autowired
    private EmploiRepository emploiRepository;

    @Autowired
    private EntityFinder entityFinder;

    public List<EmploiModel> listAll() {
        return emploiRepository.findAll();
    }

    public EmploiModel getById(int id) {
        return entityFinder.findEmploiOrThrow(id);
    }

    @Transactional
    public EmploiModel create(EmploiDTOCreate dto) {
        ProfilSIModel profil = entityFinder.findProfilSIOrThrow(dto.getProfilSI());

        EmploiModel emploi = new EmploiModel();
        emploi.setEmploiName(dto.getEmploi().getEmploi());
        emploi.setDirection(entityFinder.findDirectionOrThrow(dto.getEmploi().getDirection()));
        emploi.setService(entityFinder.findServiceOrNull(dto.getEmploi().getService()));
        emploi.setDomaine(entityFinder.findDomaineOrNull(dto.getEmploi().getDomaine()));
        emploi.setStatus(dto.getEmploi().getStatus());
        emploi.setProfilSI(profil);
        emploi.setDateCreated(LocalDateTime.now());

        return emploiRepository.save(emploi);
    }

    @Transactional
    public EmploiModel update(int id, EmploiDTOCreate dto) {
        EmploiModel emploi = getById(id);

        emploi.setEmploiName(dto.getEmploi().getEmploi());
        emploi.setDirection(entityFinder.findDirectionOrThrow(dto.getEmploi().getDirection()));
        emploi.setService(entityFinder.findServiceOrNull(dto.getEmploi().getService()));
        emploi.setDomaine(entityFinder.findDomaineOrNull(dto.getEmploi().getDomaine()));
        emploi.setStatus(dto.getEmploi().getStatus());
        emploi.setDateUpdated(LocalDateTime.now());

        ProfilSIModel profil = entityFinder.findProfilSIOrThrow(dto.getProfilSI());
        emploi.setProfilSI(profil);

        return emploiRepository.save(emploi);
    }

    public void delete(int id) {
        EmploiModel emploi = getById(id);
        emploiRepository.delete(emploi);
    }

    public EmploiDTOResponse toDTO(EmploiModel emploi) {
        ProfilSISimpleDTO profilDTO = null;
        if (emploi.getProfilSI() != null) {
            profilDTO = new ProfilSISimpleDTO(emploi.getProfilSI().getId(), emploi.getProfilSI().getName());
        }

        return EmploiDTOResponse.builder()
                .id(emploi.getId())
                .emploi(emploi.getEmploiName())
                .direction(emploi.getDirection() != null ? emploi.getDirection().getName() : null)
                .service(emploi.getService() != null ? emploi.getService().getName() : null)
                .domaine(emploi.getDomaine() != null ? emploi.getDomaine().getName() : null)
                .status(emploi.getStatus())
                .profilSI(profilDTO)
                .dateCreated(emploi.getDateCreated())
                .dateUpdated(emploi.getDateUpdated())
                .build();
    }
}
