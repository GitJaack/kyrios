package fr.cmp.kyrios.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import fr.cmp.kyrios.exception.RessourceSINotFoundException;
import fr.cmp.kyrios.model.Si.RessourceSIModel;
import fr.cmp.kyrios.repository.RessourceSIRepository;

public class RessourceSIService {
    @Autowired
    private RessourceSIRepository ressourceSIRepository;

    public RessourceSIModel getById(int id) {
        return ressourceSIRepository.findById(id)
                .orElseThrow(() -> new RessourceSINotFoundException("Ressource SI avec l'ID " + id + " non trouvée"));
    }

    public List<RessourceSIModel> getRessourcesByCategorie(String categorieName) {
        return ressourceSIRepository.findByCategorieName(categorieName);
    }
}
