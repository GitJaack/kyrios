package fr.cmp.kyrios.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.cmp.kyrios.exception.EmploiNotFoundException;
import fr.cmp.kyrios.model.Emploi.DirectionModel;
import fr.cmp.kyrios.model.Emploi.DomaineModel;
import fr.cmp.kyrios.model.Emploi.EmploiModel;
import fr.cmp.kyrios.model.Emploi.ServiceModel;
import fr.cmp.kyrios.model.Si.ProfilSIModel;
import fr.cmp.kyrios.model.Si.RessourceSIModel;
import fr.cmp.kyrios.model.Si.dto.ProfilSIDTOCreate;
import fr.cmp.kyrios.model.Si.dto.ProfilSIDTO;
import fr.cmp.kyrios.model.Si.dto.ProfilSIUpdateDTO;
import fr.cmp.kyrios.model.Si.dto.ProfilSIDTOResponse;
import fr.cmp.kyrios.model.Si.dto.RessourceSIDTO;
import fr.cmp.kyrios.repository.DirectionRepository;
import fr.cmp.kyrios.repository.DomaineRepository;
import fr.cmp.kyrios.repository.EmploiRepository;
import fr.cmp.kyrios.repository.ProfilSIRepository;
import fr.cmp.kyrios.repository.RessourceSIRepository;
import fr.cmp.kyrios.repository.ServiceRepository;
import jakarta.transaction.Transactional;

@Service
public class ProfilSIService {
    @Autowired
    private ProfilSIRepository profilSIRepository;

    @Autowired
    private DirectionRepository directionRepository;

    @Autowired
    private RessourceSIRepository ressourceSIRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private DomaineRepository domaineRepository;

    @Autowired
    private EmploiRepository emploiRepository;

    public List<ProfilSIModel> listAll() {
        return profilSIRepository.findAll();
    }

    public ProfilSIModel getById(int id) {
        return profilSIRepository.findById(id)
                .orElseThrow(() -> new EmploiNotFoundException("Profil SI avec l'ID " + id + " non trouvé"));
    }

    public List<ProfilSIModel> getByDirection(int directionId) {
        DirectionModel direction = directionRepository.findById(directionId)
                .orElseThrow(() -> new IllegalArgumentException("Direction introuvable"));

        return profilSIRepository.findByDirection(direction);
    }

    public List<RessourceSIModel> getRessourcesParDefautByDirection(int directionId) {
        DirectionModel direction = directionRepository.findById(directionId)
                .orElseThrow(() -> new EmploiNotFoundException("Direction introuvable"));
        return direction.getRessourcesDefault();
    }

    @Transactional
    public ProfilSIDTOResponse create(ProfilSIDTOCreate dto) {
        DirectionModel direction = directionRepository.findById(dto.getEmploi().getDirection())
                .orElseThrow(() -> new EmploiNotFoundException("Direction introuvable"));

        if (profilSIRepository.findByName(dto.getProfilSI().getProfilSI()).isPresent()) {
            throw new IllegalArgumentException(
                    "Un profil SI avec le nom '" + dto.getProfilSI().getProfilSI() + "' existe déjà");
        }

        ProfilSIModel profilSI = new ProfilSIModel();
        profilSI.setName(dto.getProfilSI().getProfilSI());
        profilSI.setDirection(direction);
        profilSI.setDateCreated(LocalDateTime.now());

        List<RessourceSIModel> ressourcesFinales;

        if (dto.getProfilSI().getRessourceIds() != null && !dto.getProfilSI().getRessourceIds().isEmpty()) {
            // L'utilisateur a sélectionné des ressources spécifiques
            ressourcesFinales = ressourceSIRepository.findAllById(dto.getProfilSI().getRessourceIds());
        } else {
            // Pas de sélection : utiliser les ressources par défaut selon le mode
            if (dto.getProfilSI().getModeCreation() == ProfilSIDTO.ModeCreation.COPIER) {
                // Mode COPIER : récupérer les ressources du profil source
                if (dto.getProfilSI().getProfilSISourceId() == null) {
                    throw new IllegalArgumentException("L'ID du profil source est requis en mode COPIER");
                }

                ProfilSIModel profilSource = getById(dto.getProfilSI().getProfilSISourceId());
                // Vérifier que le profil source est bien dans la même direction
                if (profilSource.getDirection() != null &&
                        !profilSource.getDirection().equals(direction)) {
                    throw new IllegalArgumentException("Le profil source doit être dans la même direction");
                }

                // Copier les ressources du profil source
                ressourcesFinales = new ArrayList<>(profilSource.getRessources());
            } else {
                // Mode NOUVEAU : utiliser les ressources par défaut de la direction
                ressourcesFinales = new ArrayList<>(getRessourcesParDefautByDirection(dto.getEmploi().getDirection()));
            }
        }

        // 4. Assigner les ressources au profil SI
        profilSI.setRessources(ressourcesFinales);

        // 5. Sauvegarder le Profil SI
        profilSI = profilSIRepository.save(profilSI);

        // 6. Créer l'emploi et le lier au profil SI
        EmploiModel emploi = new EmploiModel();
        emploi.setEmploiName(dto.getEmploi().getEmploi());
        emploi.setDirection(direction);

        if (dto.getEmploi().getService() != null) {
            ServiceModel service = serviceRepository.findById(dto.getEmploi().getService())
                    .orElseThrow(() -> new IllegalArgumentException("Service introuvable"));
            emploi.setService(service);
        }

        if (dto.getEmploi().getDomaine() != null) {
            DomaineModel domaine = domaineRepository.findById(dto.getEmploi().getDomaine())
                    .orElseThrow(() -> new IllegalArgumentException("Domaine introuvable"));
            emploi.setDomaine(domaine);
        }

        emploi.setStatus(dto.getEmploi().getStatus());
        emploi.setProfilSI(profilSI);
        emploi.setDateCreated(LocalDateTime.now());

        emploi = emploiRepository.save(emploi);

        // 7. Retourner le DTO de réponse
        return toResponseDTO(profilSI, emploi);
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

        List<RessourceSIModel> ressources = ressourceSIRepository.findAllById(dto.getRessourceIds());
        profilSI.setRessources(ressources);

        return profilSIRepository.save(profilSI);

    }

    @Transactional
    public void delete(int id) {
        ProfilSIModel profilSI = getById(id);
        profilSIRepository.delete(profilSI);
    }

    public ProfilSIDTOResponse toResponseDTO(ProfilSIModel profilSI, EmploiModel emploi) {
        List<RessourceSIDTO> ressourcesDTO = profilSI.getRessources().stream()
                .map(r -> RessourceSIDTO.builder()
                        .id(r.getId())
                        .categorie(r.getCategorie().getName())
                        .libelleAcces(r.getLibelleAcces())
                        .typeAcces(r.getTypeAcces())
                        .isDefault(r.isDefault())
                        // .selected(true)
                        .build())
                .collect(Collectors.toList());

        return ProfilSIDTOResponse.builder()
                .idProfilSI(profilSI.getId())
                .name(profilSI.getName())
                .ressources(ressourcesDTO)
                .dateCreated(profilSI.getDateCreated())
                .dateUpdated(profilSI.getDateUpdated())
                .idEmploi(emploi != null ? emploi.getId() : 0)
                .emploi(emploi != null ? emploi.getEmploiName() : null)
                .direction(emploi != null && emploi.getDirection() != null ? emploi.getDirection().getName() : null)
                .service(emploi != null && emploi.getService() != null ? emploi.getService().getName() : null)
                .domaine(emploi != null && emploi.getDomaine() != null ? emploi.getDomaine().getName() : null)
                .status(emploi != null ? emploi.getStatus() : null)
                .build();
    }
}
