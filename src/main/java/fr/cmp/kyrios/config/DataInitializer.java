package fr.cmp.kyrios.config;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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

    @Autowired
    private CategorieSIRepository categorieSIRepository;

    @Autowired
    private RessourceSIRepository ressourceSIRepository;

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

        DirectionModel dir2 = new DirectionModel();
        dir2.setName("Comptable");
        dir2 = directionRepository.save(dir2);

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
        emp2.setDirection(dir2);
        emp2.setService(null);
        emp2.setDomaine(null);
        emp2.setStatus(EmploiModel.Status.PERMANENT);
        emp2.setProfilSI(profil2);
        emp2.setDateCreated(LocalDateTime.now());
        emp2 = emploiRepository.save(emp2);
        ;

        profil1.getEmplois().add(emp);
        profil2.getEmplois().add(emp2);

        // ===== CATEGORIES SI =====
        CategorieSIModel catRepertoiresService = new CategorieSIModel();
        catRepertoiresService.setName("Répertoires de service");
        catRepertoiresService = categorieSIRepository.save(catRepertoiresService);

        CategorieSIModel catRepertoiresFonctionnels = new CategorieSIModel();
        catRepertoiresFonctionnels.setName("Répertoires fonctionnels / transverses");
        catRepertoiresFonctionnels = categorieSIRepository.save(catRepertoiresFonctionnels);

        // ===== RESSOURCES SI - Répertoires de service =====
        RessourceSIModel resAgenceComptable = new RessourceSIModel();
        resAgenceComptable.setCategorie(catRepertoiresService);
        resAgenceComptable.setName("SVC_Agence Comptable");
        resAgenceComptable.setTypeAcces(RessourceSIModel.TypeAcces.LECTURE_ECRITURE);
        ressourceSIRepository.save(resAgenceComptable);

        RessourceSIModel resAssesseurs = new RessourceSIModel();
        resAssesseurs.setCategorie(catRepertoiresService);
        resAssesseurs.setName("SVC_Assesseurs");
        resAssesseurs.setTypeAcces(RessourceSIModel.TypeAcces.LECTURE_ECRITURE);
        ressourceSIRepository.save(resAssesseurs);

        RessourceSIModel resBureau = new RessourceSIModel();
        resBureau.setCategorie(catRepertoiresService);
        resBureau.setName("SVC_Bureau");
        resBureau.setTypeAcces(RessourceSIModel.TypeAcces.LECTURE_ECRITURE);
        ressourceSIRepository.save(resBureau);

        RessourceSIModel resComptabilite = new RessourceSIModel();
        resComptabilite.setCategorie(catRepertoiresService);
        resComptabilite.setName("SVC_Comptabilité");
        resComptabilite.setTypeAcces(RessourceSIModel.TypeAcces.LECTURE_ECRITURE);
        ressourceSIRepository.save(resComptabilite);

        // ===== RESSOURCES SI - Répertoires fonctionnels / transverses =====
        RessourceSIModel resComitesCos = new RessourceSIModel();
        resComitesCos.setCategorie(catRepertoiresFonctionnels);
        resComitesCos.setName("FON_COMITES\\COS");
        resComitesCos.setTypeAcces(RessourceSIModel.TypeAcces.LECTURE_ECRITURE);
        ressourceSIRepository.save(resComitesCos);

        RessourceSIModel resComitesAlm = new RessourceSIModel();
        resComitesAlm.setCategorie(catRepertoiresFonctionnels);
        resComitesAlm.setName("FON_COMITES\\COMITE ALM");
        resComitesAlm.setTypeAcces(RessourceSIModel.TypeAcces.LECTURE_ECRITURE);
        ressourceSIRepository.save(resComitesAlm);

        RessourceSIModel resComitesCodir = new RessourceSIModel();
        resComitesCodir.setCategorie(catRepertoiresFonctionnels);
        resComitesCodir.setName("FON_COMITES\\CODIR");
        resComitesCodir.setTypeAcces(RessourceSIModel.TypeAcces.LECTURE_ECRITURE);
        ressourceSIRepository.save(resComitesCodir);

        RessourceSIModel resComitesCopil = new RessourceSIModel();
        resComitesCopil.setCategorie(catRepertoiresFonctionnels);
        resComitesCopil.setName("FON_COMITES\\COPIL");
        resComitesCopil.setTypeAcces(RessourceSIModel.TypeAcces.LECTURE_ECRITURE);
        ressourceSIRepository.save(resComitesCopil);

        // ===== RESSOURCES PAR DÉFAUT POUR LES DIRECTIONS =====
        // Assigner des ressources par défaut pour la Direction des Systèmes
        // d'Information
        dir.getRessourcesDefault().add(resAgenceComptable);
        dir.getRessourcesDefault().add(resBureau);
        dir.getRessourcesDefault().add(resComitesCos);
        dir.getRessourcesDefault().add(resComitesCodir);
        dir = directionRepository.save(dir);

        // Assigner des ressources par défaut pour la direction Comptable
        dir2.getRessourcesDefault().add(resComptabilite);
        dir2.getRessourcesDefault().add(resAssesseurs);
        dir2.getRessourcesDefault().add(resComitesAlm);
        dir2 = directionRepository.save(dir2);

        // ===== ASSIGNER LES RESSOURCES AUX PROFILS SI =====
        // Profil 1 : Assigner la direction et ses ressources par défaut
        profil1.setDirection(dir);
        profil1.getRessources().addAll(dir.getRessourcesDefault());
        profil1 = profilSIRepository.save(profil1);

        // Profil 2 : Assigner la direction et ses ressources par défaut
        profil2.setDirection(dir2);
        profil2.getRessources().addAll(dir2.getRessourcesDefault());
        profil2 = profilSIRepository.save(profil2);

        System.out.println("Données de test initialisées avec succès!");

    }
}
