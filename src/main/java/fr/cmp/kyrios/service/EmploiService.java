package fr.cmp.kyrios.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.cmp.kyrios.exception.EmploiNotFoundException;
import fr.cmp.kyrios.model.dto.EmploiDTO;
import fr.cmp.kyrios.model.EmploiModel;
import fr.cmp.kyrios.model.ProfilSIModel;
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

    public EmploiModel get(int id) {
        return emploiRepository.findById(id)
                .orElseThrow(() -> new EmploiNotFoundException("Emploi avec l'ID " + id + " non trouvé"));
    }

    @Transactional
    public EmploiModel create(EmploiDTO dto) {
        EmploiModel emploi = new EmploiModel();

        emploi.setEmploiName(dto.getEmploi());

        emploi.setDirection(directionRepository.findByName(dto.getDirection())
                .orElseThrow(() -> new IllegalArgumentException("Direction introuvable")));

        if (dto.getService() != null) {
            emploi.setService(serviceRepository.findByName(dto.getService())
                    .orElseThrow(() -> new IllegalArgumentException("Service introuvable")));
        }

        if (dto.getDomaine() != null) {
            emploi.setDomaine(domaineService.findByName(dto.getDomaine())
                    .orElseThrow(() -> new IllegalArgumentException("Domaine introuvable")));
        }

        emploi.setStatus(dto.getStatus());
        emploi.setDateCreated(LocalDateTime.now());

        EmploiModel saved = emploiRepository.save(emploi);

        ProfilSIModel profil = profilSIRepository.findById(dto.getProfilSIid())
                .orElseThrow(() -> new IllegalArgumentException("ProfilSI introuvable"));
        profil.setEmploi(saved);
        profilSIRepository.save(profil);
        return saved;
    }

    @Transactional
    public EmploiModel update(int id, EmploiDTO dto) {
        EmploiModel emploi = emploiRepository.findById(id)
                .orElseThrow(() -> new EmploiNotFoundException("Emploi avec l'ID " + id + " non trouvé"));

        emploi.setEmploiName(dto.getEmploi());

        emploi.setDirection(directionRepository.findByName(dto.getDirection())
                .orElseThrow(() -> new IllegalArgumentException("Direction introuvable")));

        if (dto.getService() != null) {
            emploi.setService(serviceRepository.findByName(dto.getService())
                    .orElseThrow(() -> new IllegalArgumentException("Service introuvable")));
        } else {
            emploi.setService(null);
        }

        if (dto.getDomaine() != null) {
            emploi.setDomaine(domaineService.findByName(dto.getDomaine())
                    .orElseThrow(() -> new IllegalArgumentException("Domaine introuvable")));
        } else {
            emploi.setDomaine(null);
        }

        emploi.setStatus(dto.getStatus());
        emploi.setDateUpdated(LocalDateTime.now());

        EmploiModel updated = emploiRepository.save(emploi);

        ProfilSIModel profil = profilSIRepository.findById(dto.getProfilSIid())
                .orElseThrow(() -> new IllegalArgumentException("ProfilSI introuvable"));
        profil.setEmploi(updated);
        profilSIRepository.save(profil);
        return updated;
    }

    public void delete(int id) {
        EmploiModel emploi = emploiRepository.findById(id)
                .orElseThrow(() -> new EmploiNotFoundException("Emploi avec l'ID " + id + " non trouvé"));
        emploiRepository.delete(emploi);
    }

}
