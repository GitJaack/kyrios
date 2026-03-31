package fr.cmp.kyrios.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.cmp.kyrios.dao.EmploiDao;
import fr.cmp.kyrios.dao.ReferenceDao;
import fr.cmp.kyrios.model.Emploi.dto.EmploiDTOCreate;
import fr.cmp.kyrios.model.Emploi.dto.EmploiDTOResponse;
import fr.cmp.kyrios.mapper.EmploiMapper;

@Service
public class EmploiService {
    @Autowired
    private ReferenceDao referenceDao;

    @Autowired
    private EmploiDao emploiDao;

    public List<EmploiDTOResponse> listAll() {
        return emploiDao.findAll().stream()
                .map(EmploiMapper::toDto)
                .toList();
    }

    public EmploiDTOResponse getById(int id) {
        var row = emploiDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Emploi avec l'ID " + id + " non trouvé"));
        return EmploiMapper.toDto(row);
    }

    @Transactional
    public EmploiDTOResponse create(EmploiDTOCreate dto) {
        if (!referenceDao.existsProfilSIById(dto.getProfilSI())) {
            throw new IllegalArgumentException("Profil SI avec l'ID " + dto.getProfilSI() + " non trouve");
        }

        if (!referenceDao.existsDirectionById(dto.getEmploi().getDirection())) {
            throw new IllegalArgumentException(
                    "Direction avec l'ID " + dto.getEmploi().getDirection() + " non trouvee");
        }

        if (dto.getEmploi().getService() != null
                && !referenceDao.existsServiceById(dto.getEmploi().getService())) {
            throw new IllegalArgumentException("Service avec l'ID " + dto.getEmploi().getService() + " non trouve");
        }

        if (dto.getEmploi().getDomaine() != null
                && !referenceDao.existsDomaineById(dto.getEmploi().getDomaine())) {
            throw new IllegalArgumentException("Domaine avec l'ID " + dto.getEmploi().getDomaine() + " non trouve");
        }

        int id = emploiDao.insert(
                dto.getEmploi().getEmploi(),
                dto.getEmploi().getDirection(),
                dto.getEmploi().getService(),
                dto.getEmploi().getDomaine(),
                dto.getEmploi().getStatus().name(),
                dto.getProfilSI(),
                LocalDateTime.now());

        return getById(id);
    }

    @Transactional
    public EmploiDTOResponse update(int id, EmploiDTOCreate dto) {
        if (!referenceDao.existsEmploiById(id)) {
            throw new IllegalArgumentException("Emploi avec l'ID " + id + " non trouve");
        }

        if (!referenceDao.existsDirectionById(dto.getEmploi().getDirection())) {
            throw new IllegalArgumentException(
                    "Direction avec l'ID " + dto.getEmploi().getDirection() + " non trouvee");
        }

        if (dto.getEmploi().getService() != null
                && !referenceDao.existsServiceById(dto.getEmploi().getService())) {
            throw new IllegalArgumentException("Service avec l'ID " + dto.getEmploi().getService() + " non trouve");
        }

        if (dto.getEmploi().getDomaine() != null
                && !referenceDao.existsDomaineById(dto.getEmploi().getDomaine())) {
            throw new IllegalArgumentException("Domaine avec l'ID " + dto.getEmploi().getDomaine() + " non trouve");
        }

        if (!referenceDao.existsProfilSIById(dto.getProfilSI())) {
            throw new IllegalArgumentException("Profil SI avec l'ID " + dto.getProfilSI() + " non trouve");
        }

        emploiDao.update(
                id,
                dto.getEmploi().getEmploi(),
                dto.getEmploi().getDirection(),
                dto.getEmploi().getService(),
                dto.getEmploi().getDomaine(),
                dto.getEmploi().getStatus().name(),
                dto.getProfilSI(),
                LocalDateTime.now());

        return getById(id);
    }

    public void delete(int id) {
        if (!referenceDao.existsEmploiById(id)) {
            throw new IllegalArgumentException("Emploi avec l'ID " + id + " non trouve");
        }
        emploiDao.deleteById(id);
    }

}
