package fr.cmp.kyrios.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.cmp.kyrios.exception.CategorieNotFoundException;
import fr.cmp.kyrios.exception.EmploiNotFoundException;
import fr.cmp.kyrios.exception.RessourceSINotFoundException;
import fr.cmp.kyrios.model.Emploi.DirectionModel;
import fr.cmp.kyrios.model.Si.ProfilSIModel;
import fr.cmp.kyrios.model.Si.RessourceSIModel;
import fr.cmp.kyrios.model.Si.dto.RessourceSIDTO;
import fr.cmp.kyrios.model.Si.dto.RessourceSIDTOCreate;
import fr.cmp.kyrios.repository.CategorieSIRepository;
import fr.cmp.kyrios.repository.DirectionRepository;
import fr.cmp.kyrios.repository.ProfilSIRepository;
import fr.cmp.kyrios.repository.RessourceSIRepository;
import jakarta.transaction.Transactional;

@Service
public class RessourceSIService {
    @Autowired
    private RessourceSIRepository ressourceSIRepository;

    @Autowired
    private CategorieSIRepository categorieSIRepository;

    @Autowired
    private DirectionRepository directionRepository;

    @Autowired
    private ProfilSIRepository profilSIRepository;

    public List<RessourceSIModel> listAll() {
        return ressourceSIRepository.findAll();
    }

    public RessourceSIModel getById(int id) {
        return ressourceSIRepository.findById(id)
                .orElseThrow(() -> new RessourceSINotFoundException("Ressource SI avec l'ID " + id + " non trouvée"));
    }

    public List<RessourceSIModel> getRessourcesParDefautByDirection(int directionId) {
        DirectionModel direction = directionRepository.findById(directionId)
                .orElseThrow(() -> new EmploiNotFoundException("Direction introuvable"));
        return direction.getRessourcesDefault();
    }

    public List<RessourceSIModel> getRessourcesByCategorie(int id) {
        categorieSIRepository.findById(id)
                .orElseThrow(() -> new CategorieNotFoundException("Catégorie avec l'ID " + id + " non trouvée"));

        return ressourceSIRepository.findByCategorieId(id);
    }

    @Transactional
    public RessourceSIModel create(RessourceSIDTOCreate dto) {
        if (categorieSIRepository.findById(dto.getCategorie()).isEmpty()) {
            throw new CategorieNotFoundException("Catégorie avec l'ID " + dto.getCategorie() + " non trouvée");
        }

        if (ressourceSIRepository.findByName(dto.getName()).isPresent()) {
            throw new IllegalArgumentException("Une ressource avec le nom '" + dto.getName() + "' existe déjà");
        }

        RessourceSIModel ressource = new RessourceSIModel();
        ressource.setCategorie(categorieSIRepository.findById(dto.getCategorie()).get());
        ressource.setName(dto.getName());
        ressource.setTypeAcces(dto.getTypeAcces());
        return ressourceSIRepository.save(ressource);
    }

    @Transactional
    public void delete(int id) {
        RessourceSIModel ressource = getById(id);

        List<DirectionModel> directions = directionRepository.findAll();
        for (DirectionModel direction : directions) {
            if (direction.getRessourcesDefault().remove(ressource)) {
                directionRepository.save(direction);
            }
        }

        List<ProfilSIModel> profils = profilSIRepository.findAll();
        for (ProfilSIModel profil : profils) {
            if (profil.getRessources().remove(ressource)) {
                profilSIRepository.save(profil);
            }
        }
        ressourceSIRepository.delete(ressource);
    }

    public RessourceSIDTO toDTO(RessourceSIModel ressource) {
        return RessourceSIDTO.builder()
                .id(ressource.getId())
                .categorie(ressource.getCategorie().getName())
                .name(ressource.getName())
                .typeAcces(ressource.getTypeAcces())
                .build();
    }

    public List<RessourceSIDTO> toDTOList(List<RessourceSIModel> ressources) {
        return ressources.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
