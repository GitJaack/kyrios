package fr.cmp.kyrios.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.cmp.kyrios.exception.CategorieNotFoundException;
import fr.cmp.kyrios.model.Si.CategorieSIModel;
import fr.cmp.kyrios.model.Si.dto.CategorieSIDTOCreate;
import fr.cmp.kyrios.model.Si.dto.CategorieSIDTOResponse;
import fr.cmp.kyrios.repository.CategorieSIRepository;
import jakarta.transaction.Transactional;

@Service
public class CategorieSIService {
    @Autowired
    private CategorieSIRepository categorieSIRepository;

    public List<CategorieSIModel> listAll() {
        return categorieSIRepository.findAll();
    }

    public CategorieSIModel getById(int id) {
        return categorieSIRepository.findById(id)
                .orElseThrow(() -> new CategorieNotFoundException("Catégorie avec l'ID " + id + " non trouvée"));
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

    public CategorieSIDTOResponse toDTO(CategorieSIModel categorie) {
        return CategorieSIDTOResponse.builder()
                .id(categorie.getId())
                .name(categorie.getName())
                .build();
    }
}
