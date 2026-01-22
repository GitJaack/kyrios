package fr.cmp.kyrios.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.cmp.kyrios.exception.EmploiNotFoundException;
import fr.cmp.kyrios.model.Emploi.DirectionModel;
import fr.cmp.kyrios.model.Emploi.EmploiModel;
import fr.cmp.kyrios.model.Si.ProfilSIModel;
import fr.cmp.kyrios.model.Si.ProfilSIRessource;
import fr.cmp.kyrios.model.Si.RessourceSIModel;
import fr.cmp.kyrios.model.Si.dto.profilSI.ProfilSIDTO;
import fr.cmp.kyrios.model.Si.dto.profilSI.ProfilSIDTOCreate;
import fr.cmp.kyrios.model.Si.dto.profilSI.ProfilSIDTOCreateResponse;
import fr.cmp.kyrios.model.Si.dto.profilSI.ProfilSIDTODeleteResponse;
import fr.cmp.kyrios.model.Si.dto.profilSI.ProfilSIDTOResponse;
import fr.cmp.kyrios.model.Si.dto.profilSI.ProfilSIUpdateDTO;
import fr.cmp.kyrios.model.Si.dto.ressourceSI.RessourceSIDTO;
import fr.cmp.kyrios.repository.DirectionRepository;
import fr.cmp.kyrios.repository.EmploiRepository;
import fr.cmp.kyrios.repository.ProfilSIRepository;
import fr.cmp.kyrios.util.EntityFinder;
import jakarta.transaction.Transactional;

@Service
public class ProfilSIService {
    @Autowired
    private ProfilSIRepository profilSIRepository;

    @Autowired
    private DirectionRepository directionRepository;

    @Autowired
    private EmploiRepository emploiRepository;

    @Autowired
    private EntityFinder entityFinder;

    public List<ProfilSIModel> listAll() {
        return profilSIRepository.findAll();
    }

    public ProfilSIModel getById(int id) {
        return entityFinder.findProfilSIOrThrow(id);
    }

    public List<ProfilSIModel> getByDirection(int directionId) {
        DirectionModel direction = entityFinder.findDirectionOrThrow(directionId);

        return profilSIRepository.findByDirection(direction);
    }

    public List<RessourceSIModel> getRessourcesParDefautByDirection(int directionId) {
        DirectionModel direction = directionRepository.findById(directionId)
                .orElseThrow(() -> new EmploiNotFoundException("Direction introuvable"));
        return direction.getRessourcesDefault();
    }

    @Transactional
    public ProfilSIDTOCreateResponse create(ProfilSIDTOCreate dto) {
        DirectionModel direction = entityFinder.findDirectionOrThrow(dto.getEmploi().getDirection());

        if (profilSIRepository.findByName(dto.getProfilSI().getProfilSI()).isPresent()) {
            throw new IllegalArgumentException(
                    "Un profil SI avec le nom '" + dto.getProfilSI().getProfilSI() + "' existe déjà");
        }

        // Validation du mode COPIER
        if (dto.getProfilSI().getModeCreation() == ProfilSIDTO.ModeCreation.COPIER) {
            if (dto.getProfilSI().getProfilSISourceId() == null) {
                throw new IllegalArgumentException("L'ID du profil source est requis en mode COPIER");
            }
        }

        ProfilSIModel profilSI = new ProfilSIModel();
        profilSI.setName(dto.getProfilSI().getProfilSI());
        profilSI.setDirection(direction);
        profilSI.setDateCreated(LocalDateTime.now());

        // Sauvegarde d'abord le Profil SI pour avoir un ID
        profilSI = profilSIRepository.save(profilSI);

        // Étape 1 : Charger les ressources de base selon le mode
        List<ProfilSIRessource> profilSIRessources = new ArrayList<>();

        if (dto.getProfilSI().getModeCreation() == ProfilSIDTO.ModeCreation.COPIER) {
            // Mode COPIER : copier les ressources avec leurs types d'accès du profil source
            ProfilSIModel profilSource = getById(dto.getProfilSI().getProfilSISourceId());
            if (profilSource.getDirection() != null &&
                    !profilSource.getDirection().equals(direction)) {
                throw new IllegalArgumentException("Le profil source doit être dans la même direction");
            }

            for (ProfilSIRessource sourceRessource : profilSource.getProfilSIRessources()) {
                ProfilSIRessource newRessource = new ProfilSIRessource();
                newRessource.setProfilSI(profilSI);
                newRessource.setRessource(sourceRessource.getRessource());
                newRessource.setTypeAcces(sourceRessource.getTypeAcces());
                profilSIRessources.add(newRessource);
            }
        } else {
            // Mode NOUVEAU : partir des ressources par défaut avec leur type par défaut
            List<RessourceSIModel> ressourcesDefaut = getRessourcesParDefautByDirection(dto.getEmploi().getDirection());
            for (RessourceSIModel ressource : ressourcesDefaut) {
                ProfilSIRessource profilSIRessource = new ProfilSIRessource();
                profilSIRessource.setProfilSI(profilSI);
                profilSIRessource.setRessource(ressource);
                profilSIRessource.setTypeAcces(ressource.getTypeAcces());
                profilSIRessources.add(profilSIRessource);
            }
        }

        // Étape 2 : Ajouter/Modifier les ressources avec leurs types d'accès spécifiés
        if (dto.getProfilSI().getRessources() != null && !dto.getProfilSI().getRessources().isEmpty()) {
            for (var ressourceDTO : dto.getProfilSI().getRessources()) {
                RessourceSIModel ressource = entityFinder.findRessourceOrThrow(ressourceDTO.getRessourceId());

                // Chercher si cette ressource existe déjà dans la liste
                ProfilSIRessource existante = profilSIRessources.stream()
                        .filter(psr -> psr.getRessource().getId() == ressource.getId())
                        .findFirst()
                        .orElse(null);

                if (existante != null) {
                    // Mettre à jour le type d'accès
                    existante.setTypeAcces(ressourceDTO.getTypeAcces());
                } else {
                    // Ajouter la nouvelle ressource
                    ProfilSIRessource profilSIRessource = new ProfilSIRessource();
                    profilSIRessource.setProfilSI(profilSI);
                    profilSIRessource.setRessource(ressource);
                    profilSIRessource.setTypeAcces(ressourceDTO.getTypeAcces());
                    profilSIRessources.add(profilSIRessource);
                }
            }
        }

        // Sauvegarder toutes les associations
        profilSI.getProfilSIRessources().addAll(profilSIRessources);
        profilSI = profilSIRepository.save(profilSI);

        // Créer l'emploi et le lie au profil SI
        EmploiModel emploi = new EmploiModel();
        emploi.setEmploiName(dto.getEmploi().getEmploi());
        emploi.setDirection(direction);
        emploi.setService(entityFinder.findServiceOrNull(dto.getEmploi().getService()));
        emploi.setDomaine(entityFinder.findDomaineOrNull(dto.getEmploi().getDomaine()));
        emploi.setStatus(dto.getEmploi().getStatus());
        emploi.setProfilSI(profilSI);
        emploi.setDateCreated(LocalDateTime.now());

        emploi = emploiRepository.save(emploi);

        return toResponseDTOCreate(profilSI, emploi);
    }

    @Transactional
    public ProfilSIModel update(int id, ProfilSIUpdateDTO dto) {
        ProfilSIModel profilSI = getById(id);
        if (!profilSI.getName().equals(dto.getProfilSI())) {
            profilSIRepository.findByName(dto.getProfilSI()).ifPresent(existing -> {
                throw new IllegalArgumentException("Un profil SI avec le nom '" + dto.getProfilSI() + "' existe déjà");
            });
        }
        profilSI.setName(dto.getProfilSI());
        profilSI.setDateUpdated(LocalDateTime.now());

        // Supprimer les anciennes associations
        profilSI.getProfilSIRessources().clear();

        // Créer les nouvelles associations avec les types d'accès spécifiés
        if (dto.getRessources() != null && !dto.getRessources().isEmpty()) {
            for (var ressourceDTO : dto.getRessources()) {
                RessourceSIModel ressource = entityFinder.findRessourceOrThrow(ressourceDTO.getRessourceId());

                ProfilSIRessource profilSIRessource = new ProfilSIRessource();
                profilSIRessource.setProfilSI(profilSI);
                profilSIRessource.setRessource(ressource);
                profilSIRessource.setTypeAcces(ressourceDTO.getTypeAcces());
                profilSI.getProfilSIRessources().add(profilSIRessource);
            }
        }

        return profilSIRepository.save(profilSI);
    }

    @Transactional
    public ProfilSIDTODeleteResponse delete(int id) {
        ProfilSIModel profilSI = getById(id);

        List<ProfilSIDTODeleteResponse.EmploiInfo> emploisDetaches = new ArrayList<>();

        if (profilSI.getEmplois() != null && !profilSI.getEmplois().isEmpty()) {
            for (EmploiModel emploi : profilSI.getEmplois()) {
                emploisDetaches.add(ProfilSIDTODeleteResponse.EmploiInfo.builder()
                        .id(emploi.getId())
                        .nom(emploi.getEmploiName())
                        .build());

                emploi.setProfilSI(null);
                emploiRepository.save(emploi);
            }
        }

        profilSIRepository.delete(profilSI);

        String message = emploisDetaches.isEmpty()
                ? "Profil SI '" + profilSI.getName() + "' supprimé avec succès. Aucun emploi n'était lié."
                : "Profil SI '" + profilSI.getName() + "' supprimé avec succès. " + emploisDetaches.size()
                        + " emploi(s) ont été détaché(s).";

        return ProfilSIDTODeleteResponse.builder()
                .message(message)
                .emploisDetaches(emploisDetaches)
                .build();
    }

    public ProfilSIDTOCreateResponse toResponseDTOCreate(ProfilSIModel profilSI, EmploiModel emploi) {
        return ProfilSIDTOCreateResponse.builder()
                .profilSI(toResponseDTO(profilSI))
                .idEmploi(emploi != null ? emploi.getId() : 0)
                .emploi(emploi != null ? emploi.getEmploiName() : null)
                .direction(emploi != null && emploi.getDirection() != null ? emploi.getDirection().getName() : null)
                .service(emploi != null && emploi.getService() != null ? emploi.getService().getName() : null)
                .domaine(emploi != null && emploi.getDomaine() != null ? emploi.getDomaine().getName() : null)
                .status(emploi != null ? emploi.getStatus() : null)
                .build();
    }

    public ProfilSIDTOResponse toResponseDTO(ProfilSIModel profilSI) {
        List<RessourceSIDTO> ressourcesDTO = profilSI.getProfilSIRessources().stream()
                .map(psr -> RessourceSIDTO.builder()
                        .id(psr.getRessource().getId())
                        .categorie(psr.getRessource().getCategorie().getName())
                        .name(psr.getRessource().getName())
                        .typeAcces(psr.getTypeAcces())
                        .build())
                .collect(Collectors.toList());

        return ProfilSIDTOResponse.builder()
                .idProfilSI(profilSI.getId())
                .name(profilSI.getName())
                .directionId(profilSI.getDirection().getId())
                .ressources(ressourcesDTO)
                .dateCreated(profilSI.getDateCreated())
                .dateUpdated(profilSI.getDateUpdated())
                .build();
    }
}
