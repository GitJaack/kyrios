package fr.cmp.kyrios.config;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import fr.cmp.kyrios.model.Emploi.DirectionModel;
import fr.cmp.kyrios.model.Emploi.DomaineModel;
import fr.cmp.kyrios.model.Emploi.EmploiModel;
import fr.cmp.kyrios.model.Emploi.ServiceModel;
import fr.cmp.kyrios.model.Si.ProfilSIModel;
import fr.cmp.kyrios.repository.DirectionRepository;
import fr.cmp.kyrios.repository.DomaineRepository;
import fr.cmp.kyrios.repository.EmploiRepository;
import fr.cmp.kyrios.repository.ProfilSIRepository;
import fr.cmp.kyrios.repository.ServiceRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private DirectionRepository directionRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private DomaineRepository domaineRepository;

    @Autowired
    private ProfilSIRepository profilSIRepository;

    @Autowired
    private EmploiRepository emploiRepository;

    @Override
    public void run(String... args) throws Exception {
        // Vérifier si les données existent déjà
        if (directionRepository.count() > 0) {
            System.out.println("Données de test déjà présentes, initialisation ignorée.");
            return;
        }

        System.out.println("Initialisation des données de test...");

        // ===== DIRECTIONS =====
        DirectionModel dir = new DirectionModel();
        dir.setName("Direction des Systemes d'Information");
        dir = directionRepository.save(dir);

        // ===== SERVICES =====
        ServiceModel svcInfra = new ServiceModel();
        svcInfra.setName("Infrastructure");
        svcInfra = serviceRepository.save(svcInfra);

        // ===== DOMAINES =====
        DomaineModel domJava = new DomaineModel();
        domJava.setName("Java");
        domJava = domaineRepository.save(domJava);

        // ===== PROFILS SI =====
        ProfilSIModel profil1 = new ProfilSIModel();
        profil1.setName("Developpeur fullstack");
        profil1.setDateCreated(LocalDateTime.now());
        profil1 = profilSIRepository.save(profil1);

        ProfilSIModel profil2 = new ProfilSIModel();
        profil2.setName("Developpeur windev");
        profil2.setDateCreated(LocalDateTime.now());
        profil2 = profilSIRepository.save(profil2);

        // ===== EMPLOIS =====
        EmploiModel emp = new EmploiModel();
        emp.setEmploiName("Developpeur informatique Fullstack (Java)");
        emp.setDirection(dir);
        emp.setService(svcInfra);
        emp.setDomaine(domJava);
        emp.setStatus(EmploiModel.Status.PERMANENT);
        emp.setProfilSI(profil1);
        emp.setDateCreated(LocalDateTime.now());
        emp = emploiRepository.save(emp);
        ;
        EmploiModel emp2 = new EmploiModel();
        emp2.setEmploiName("Developpeur informatique windev");
        emp2.setDirection(dir);
        emp2.setService(null);
        emp2.setDomaine(null);
        emp2.setStatus(EmploiModel.Status.PERMANENT);
        emp2.setProfilSI(profil2);
        emp2.setDateCreated(LocalDateTime.now());
        emp2 = emploiRepository.save(emp2);
        ;

        profil1.getEmplois().add(emp);
        profil2.getEmplois().add(emp2);

    }
}
