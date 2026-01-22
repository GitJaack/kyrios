package fr.cmp.kyrios.util;

import org.springframework.stereotype.Component;

import fr.cmp.kyrios.exception.CategorieNotFoundException;
import fr.cmp.kyrios.exception.EmploiNotFoundException;
import fr.cmp.kyrios.exception.ProfilSINotFoundException;
import fr.cmp.kyrios.exception.RessourceSINotFoundException;
import fr.cmp.kyrios.model.Emploi.DirectionModel;
import fr.cmp.kyrios.model.Emploi.DomaineModel;
import fr.cmp.kyrios.model.Emploi.EmploiModel;
import fr.cmp.kyrios.model.Emploi.ServiceModel;
import fr.cmp.kyrios.model.Si.CategorieSIModel;
import fr.cmp.kyrios.model.Si.ProfilSIModel;
import fr.cmp.kyrios.model.Si.RessourceSIModel;
import fr.cmp.kyrios.repository.CategorieSIRepository;
import fr.cmp.kyrios.repository.DirectionRepository;
import fr.cmp.kyrios.repository.DomaineRepository;
import fr.cmp.kyrios.repository.EmploiRepository;
import fr.cmp.kyrios.repository.ProfilSIRepository;
import fr.cmp.kyrios.repository.RessourceSIRepository;
import fr.cmp.kyrios.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;

/**
 * Utilitaire centralisé pour récupérer les entités avec gestion d'erreur
 * unifiée
 */
@Component
@RequiredArgsConstructor
public class EntityFinder {
    private final EmploiRepository emploiRepository;

    private final ProfilSIRepository profilSIRepository;

    private final DirectionRepository directionRepository;

    private final ServiceRepository serviceRepository;

    private final DomaineRepository domaineRepository;

    private final CategorieSIRepository categorieSIRepository;

    private final RessourceSIRepository ressourceSIRepository;

    public EmploiModel findEmploiOrThrow(int id) {
        return emploiRepository.findById(id)
                .orElseThrow(() -> new EmploiNotFoundException("Emploi avec l'ID " + id + " non trouvé"));
    }

    public ProfilSIModel findProfilSIOrThrow(int id) {
        return profilSIRepository.findById(id)
                .orElseThrow(() -> new ProfilSINotFoundException("Profil SI avec l'ID " + id + " non trouvé"));
    }

    public DirectionModel findDirectionOrThrow(int id) {
        return directionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Direction avec l'ID " + id + " non trouvé"));
    }

    public ServiceModel findServiceOrNull(Integer id) {
        if (id == null) {
            return null;
        }
        return serviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Service avec l'ID " + id + " non trouvé"));
    }

    public DomaineModel findDomaineOrNull(Integer id) {
        if (id == null) {
            return null;
        }
        return domaineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Domaine avec l'ID " + id + " non trouvé"));
    }

    public CategorieSIModel findCategorieOrThrow(int id) {
        return categorieSIRepository.findById(id)
                .orElseThrow(() -> new CategorieNotFoundException("Catégorie avec l'ID " + id + " non trouvée"));
    }

    public CategorieSIModel findCategorieByNameOrThrow(String name) {
        return categorieSIRepository.findByName(name)
                .orElseThrow(() -> new CategorieNotFoundException("Catégorie avec le nom '" + name + "' non trouvée"));
    }

    public RessourceSIModel findRessourceOrThrow(int id) {
        return ressourceSIRepository.findById(id)
                .orElseThrow(() -> new RessourceSINotFoundException("Ressource SI avec l'ID " + id + " non trouvée"));
    }
}
