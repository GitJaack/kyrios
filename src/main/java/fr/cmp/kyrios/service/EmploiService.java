package fr.cmp.kyrios.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.cmp.kyrios.exception.EmploiNotFoundException;
import fr.cmp.kyrios.model.Emploi.EmploiModel;
import fr.cmp.kyrios.model.Emploi.dto.EmploiDTO;
import fr.cmp.kyrios.model.Emploi.dto.EmploiDTOResponse;
import fr.cmp.kyrios.model.Emploi.dto.ProfilSISimpleDTO;
import fr.cmp.kyrios.model.Si.ProfilSIModel;
import fr.cmp.kyrios.repository.EmploiRepository;
import fr.cmp.kyrios.repository.DirectionRepository;
import fr.cmp.kyrios.repository.DomaineRepository;
import fr.cmp.kyrios.repository.ProfilSIRepository;
import fr.cmp.kyrios.repository.ServiceRepository;

@Service
public class EmploiService {
    @Autowired
    private EmploiRepository emploiRepository;

    @Autowired
    private ProfilSIRepository profilSIRepository;

    @Autowired
    private DirectionRepository directionRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private DomaineRepository domaineService;

    public List<EmploiModel> listAll() {
        return emploiRepository.findAll();
    }

    public EmploiModel getById(int id) {
        return emploiRepository.findById(id)
                .orElseThrow(() -> new EmploiNotFoundException("Emploi avec l'ID " + id + " non trouvé"));
    }

    @Transactional
    public EmploiModel create(EmploiDTO dto) {
        ProfilSIModel profil = profilSIRepository.findById(dto.getProfilSI())
                .orElseThrow(() -> new IllegalArgumentException("ProfilSI introuvable"));

        EmploiModel emploi = new EmploiModel();
        emploi.setEmploiName(dto.getEmploi());

        emploi.setDirection(directionRepository.findById(dto.getDirection())
                .orElseThrow(() -> new IllegalArgumentException("Direction introuvable")));

        if (dto.getService() != null) {
            emploi.setService(serviceRepository.findById(dto.getService())
                    .orElseThrow(() -> new IllegalArgumentException("Service introuvable")));
        }

        if (dto.getDomaine() != null) {
            emploi.setDomaine(domaineService.findById(dto.getDomaine())
                    .orElseThrow(() -> new IllegalArgumentException("Domaine introuvable")));
        }

        emploi.setStatus(dto.getStatus());
        emploi.setProfilSI(profil);
        emploi.setDateCreated(LocalDateTime.now());

        return emploiRepository.save(emploi);
    }

    @Transactional
    public EmploiModel update(int id, EmploiDTO dto) {
        EmploiModel emploi = emploiRepository.findById(id)
                .orElseThrow(() -> new EmploiNotFoundException("Emploi avec l'ID " + id + " non trouvé"));

        emploi.setEmploiName(dto.getEmploi());

        emploi.setDirection(directionRepository.findById(dto.getDirection())
                .orElseThrow(() -> new IllegalArgumentException("Direction introuvable")));

        if (dto.getService() != null) {
            emploi.setService(serviceRepository.findById(dto.getService())
                    .orElseThrow(() -> new IllegalArgumentException("Service introuvable")));
        } else {
            emploi.setService(null);
        }

        if (dto.getDomaine() != null) {
            emploi.setDomaine(domaineService.findById(dto.getDomaine())
                    .orElseThrow(() -> new IllegalArgumentException("Domaine introuvable")));
        } else {
            emploi.setDomaine(null);
        }

        emploi.setStatus(dto.getStatus());
        emploi.setDateUpdated(LocalDateTime.now());

        ProfilSIModel profil = profilSIRepository.findById(dto.getProfilSI())
                .orElseThrow(() -> new IllegalArgumentException("ProfilSI introuvable"));
        emploi.setProfilSI(profil);

        return emploiRepository.save(emploi);
    }

    public void delete(int id) {
        EmploiModel emploi = emploiRepository.findById(id)
                .orElseThrow(() -> new EmploiNotFoundException("Emploi avec l'ID " + id + " non trouvé"));
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
