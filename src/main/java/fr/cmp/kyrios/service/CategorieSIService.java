package fr.cmp.kyrios.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.cmp.kyrios.model.Emploi.DirectionModel;
import fr.cmp.kyrios.model.Si.CategorieSIModel;
import fr.cmp.kyrios.model.Si.ProfilSIModel;
import fr.cmp.kyrios.model.Si.RessourceSIModel;
import fr.cmp.kyrios.model.Si.dto.categorieSI.CategorieSIDTOCreate;
import fr.cmp.kyrios.model.Si.dto.categorieSI.CategorieSIDTOResponse;
import fr.cmp.kyrios.repository.Emploi.DirectionRepository;
import fr.cmp.kyrios.repository.Si.CategorieSIRepository;
import fr.cmp.kyrios.repository.Si.ProfilSIRepository;
import fr.cmp.kyrios.repository.Si.RessourceSIRepository;
import fr.cmp.kyrios.util.EntityFinder;
import jakarta.transaction.Transactional;

@Service
public class CategorieSIService {
    @Autowired
    private CategorieSIRepository categorieSIRepository;

    @Autowired
    private DirectionRepository directionRepository;

    @Autowired
    private RessourceSIRepository ressourceSIRepository;

    @Autowired
    private ProfilSIRepository profilSIRepository;

    @Autowired
    private EntityFinder entityFinder;

    public List<CategorieSIModel> listAll() {
        return categorieSIRepository.findAll();
    }

    public CategorieSIModel getById(int id) {
        return entityFinder.findCategorieOrThrow(id);
    }

    @Transactional
    public CategorieSIModel create(CategorieSIDTOCreate dto) {
        if (categorieSIRepository.findByName(dto.getName()).isPresent()) {
            throw new IllegalArgumentException("Une catégorie avec le nom '" + dto.getName() + "' existe déjà");
        }

        CategorieSIModel categorie = new CategorieSIModel();
        categorie.setName(dto.getName());
        return categorieSIRepository.save(categorie);
    }

    @Transactional
    public CategorieSIModel update(int id, String name) {
        CategorieSIModel categorie = getById(id);

        if (!categorie.getName().equals(name)) {
            categorieSIRepository.findByName(name).ifPresent(existing -> {
                throw new IllegalArgumentException("Une catégorie avec le nom '" + name + "' existe déjà");
            });
        }

        categorie.setName(name);
        return categorieSIRepository.save(categorie);
    }

    @Transactional
    public void delete(int id) {
        CategorieSIModel categorie = getById(id);

        List<RessourceSIModel> ressources = categorie.getRessources();

        if (ressources != null && !ressources.isEmpty()) {
            List<DirectionModel> directions = directionRepository.findAll();
            for (DirectionModel direction : directions) {
                boolean modified = false;
                for (RessourceSIModel ressource : ressources) {
                    if (direction.getRessourcesDefault().remove(ressource)) {
                        modified = true;
                    }
                }
                if (modified) {
                    directionRepository.save(direction);
                }
            }

            List<ProfilSIModel> profils = profilSIRepository.findAll();
            for (ProfilSIModel profil : profils) {
                for (RessourceSIModel ressource : ressources) {
                    profil.getProfilSIRessources().removeIf(psr -> psr.getRessource().equals(ressource));
                }
            }

            profilSIRepository.flush();

            ressourceSIRepository.deleteAll(ressources);
        }

        categorieSIRepository.delete(categorie);
    }

    public CategorieSIDTOResponse toDTO(CategorieSIModel categorie) {
        return CategorieSIDTOResponse.builder()
                .id(categorie.getId())
                .name(categorie.getName())
                .build();
    }
}
