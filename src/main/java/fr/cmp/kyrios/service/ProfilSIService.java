package fr.cmp.kyrios.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import fr.cmp.kyrios.exception.EmploiNotFoundException;
import fr.cmp.kyrios.model.Si.ProfilSIModel;
import fr.cmp.kyrios.repository.ProfilSIRepository;

public class ProfilSIService {
    @Autowired
    private ProfilSIRepository profilSIRepository;

    public List<ProfilSIModel> listAll() {
        return profilSIRepository.findAll();
    }

    public ProfilSIModel getById(int id) {
        return profilSIRepository.findById(id)
                .orElseThrow(() -> new EmploiNotFoundException("Profil SI avec l'ID " + id + " non trouvé"));
    }
}
