package fr.cmp.kyrios.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.cmp.kyrios.model.Emploi.DirectionModel;
import fr.cmp.kyrios.model.Si.ProfilSIModel;
import fr.cmp.kyrios.model.Si.RessourceSIModel;
import fr.cmp.kyrios.model.Si.dto.ressourceSI.RessourceSIDTO;
import fr.cmp.kyrios.model.Si.dto.ressourceSI.RessourceSIDTOCreate;
import fr.cmp.kyrios.repository.Emploi.DirectionRepository;
import fr.cmp.kyrios.repository.Si.ProfilSIRepository;
import fr.cmp.kyrios.repository.Si.RessourceSIRepository;
import fr.cmp.kyrios.util.EntityFinder;
import jakarta.transaction.Transactional;

@Service
public class RessourceSIService {
    @Autowired
    private RessourceSIRepository ressourceSIRepository;

    @Autowired
    private DirectionRepository directionRepository;

    @Autowired
    private ProfilSIRepository profilSIRepository;

    @Autowired
    private EntityFinder entityFinder;

    public List<RessourceSIModel> listAll() {
        return ressourceSIRepository.findAll();
    }

    public RessourceSIModel getById(int id) {
        return entityFinder.findRessourceOrThrow(id);
    }

    public List<RessourceSIModel> getRessourcesParDefautByDirection(int directionId) {
        DirectionModel direction = entityFinder.findDirectionOrThrow(directionId);
        return direction.getRessourcesDefault();
    }

    public List<RessourceSIModel> getRessourcesByCategorie(int id) {
        entityFinder.findCategorieOrThrow(id);
        return ressourceSIRepository.findByCategorieId(id);
    }

    @Transactional
    public RessourceSIModel create(RessourceSIDTOCreate dto) {
        if (ressourceSIRepository.findByName(dto.getName()).isPresent()) {
            throw new IllegalArgumentException("Une ressource avec le nom '" + dto.getName() + "' existe déjà");
        }

        RessourceSIModel ressource = new RessourceSIModel();
        ressource.setCategorie(entityFinder.findCategorieOrThrow(dto.getCategorie()));
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
            boolean hasRessource = profil.getProfilSIRessources().stream()
                    .anyMatch(psr -> psr.getRessource().getId() == id);
            if (hasRessource) {
                profil.getProfilSIRessources().removeIf(psr -> psr.getRessource().getId() == id);
            }
        }

        profilSIRepository.flush();

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
