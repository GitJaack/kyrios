package fr.cmp.kyrios.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import fr.cmp.kyrios.repository.EmploiRepository;
import fr.cmp.kyrios.repository.ProfilSIRepository;
import fr.cmp.kyrios.util.EntityFinder;
import jakarta.transaction.Transactional;

@Service
public class ProfilSIService {
    @Autowired
    private ProfilSIRepository profilSIRepository;

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
        DirectionModel direction = entityFinder.findDirectionOrThrow(directionId);
        return direction.getRessourcesDefault();
    }

    @Transactional
    public ProfilSIDTOCreateResponse create(ProfilSIDTOCreate dto) {
        DirectionModel direction = entityFinder.findDirectionOrThrow(dto.getEmploi().getDirection());

        if (profilSIRepository.findByName(dto.getProfilSI().getProfilSI()).isPresent()) {
            throw new IllegalArgumentException(
                    "Un profil SI avec le nom '" + dto.getProfilSI().getProfilSI() + "' existe déjà");
        }

        if (dto.getProfilSI().getModeCreation() == ProfilSIDTO.ModeCreation.COPIER) {
            if (dto.getProfilSI().getProfilSISourceId() == null) {
                throw new IllegalArgumentException("L'ID du profil source est requis en mode COPIER");
            }
        }

        ProfilSIModel profilSI = new ProfilSIModel();
        profilSI.setName(dto.getProfilSI().getProfilSI());
        profilSI.setDirection(direction);
        profilSI.setDateCreated(LocalDateTime.now());

        List<ProfilSIRessource> profilSIRessources = new ArrayList<>();

        if (dto.getProfilSI().getModeCreation() == ProfilSIDTO.ModeCreation.COPIER) {
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
            List<RessourceSIModel> ressourcesDefaut = getRessourcesParDefautByDirection(dto.getEmploi().getDirection());
            for (RessourceSIModel ressource : ressourcesDefaut) {
                ProfilSIRessource profilSIRessource = new ProfilSIRessource();
                profilSIRessource.setProfilSI(profilSI);
                profilSIRessource.setRessource(ressource);
                profilSIRessource.setTypeAcces(ressource.getTypeAcces());
                profilSIRessources.add(profilSIRessource);
            }
        }

        if (dto.getProfilSI().getRessources() != null && !dto.getProfilSI().getRessources().isEmpty()) {
            Set<Integer> ressourceIds = new HashSet<>();
            for (var ressourceDTO : dto.getProfilSI().getRessources()) {
                if (!ressourceIds.add(ressourceDTO.getRessourceId())) {
                    throw new IllegalArgumentException(
                            "La ressource ID " + ressourceDTO.getRessourceId() + " est présente plusieurs fois");
                }
            }

            for (var ressourceDTO : dto.getProfilSI().getRessources()) {
                RessourceSIModel ressource = entityFinder.findRessourceOrThrow(ressourceDTO.getRessourceId());

                ProfilSIRessource existante = profilSIRessources.stream()
                        .filter(psr -> psr.getRessource().getId() == ressource.getId())
                        .findFirst()
                        .orElse(null);

                if (existante != null) {
                    existante.setTypeAcces(ressourceDTO.getTypeAcces());
                } else {
                    ProfilSIRessource profilSIRessource = new ProfilSIRessource();
                    profilSIRessource.setProfilSI(profilSI);
                    profilSIRessource.setRessource(ressource);
                    profilSIRessource.setTypeAcces(ressourceDTO.getTypeAcces());
                    profilSIRessources.add(profilSIRessource);
                }
            }
        }

        profilSI = profilSIRepository.save(profilSI);

        profilSI.getProfilSIRessources().addAll(profilSIRessources);
        profilSI = profilSIRepository.save(profilSI);

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

        profilSI.getProfilSIRessources().removeIf(r -> true);

        if (dto.getRessources() != null && !dto.getRessources().isEmpty()) {
            Set<Integer> ressourceIds = new HashSet<>();
            for (var ressourceDTO : dto.getRessources()) {
                if (!ressourceIds.add(ressourceDTO.getRessourceId())) {
                    throw new IllegalArgumentException(
                            "La ressource ID " + ressourceDTO.getRessourceId() + " est présente plusieurs fois");
                }
            }

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
                .direction(profilSI.getDirection().getName())
                .ressources(ressourcesDTO)
                .dateCreated(profilSI.getDateCreated())
                .dateUpdated(profilSI.getDateUpdated())
                .build();
    }
}
