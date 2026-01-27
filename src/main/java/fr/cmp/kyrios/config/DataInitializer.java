package fr.cmp.kyrios.config;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import fr.cmp.kyrios.model.App.AppModel;
import fr.cmp.kyrios.model.App.ProfilAppModel;
import fr.cmp.kyrios.model.App.ProfilAppProfilSI;
import fr.cmp.kyrios.model.Emploi.DirectionModel;
import fr.cmp.kyrios.model.Emploi.DomaineModel;
import fr.cmp.kyrios.model.Emploi.EmploiModel;
import fr.cmp.kyrios.model.Emploi.ServiceModel;
import fr.cmp.kyrios.model.Si.CategorieSIModel;
import fr.cmp.kyrios.model.Si.ProfilSIModel;
import fr.cmp.kyrios.model.Si.ProfilSIRessource;
import fr.cmp.kyrios.model.Si.RessourceSIModel;
import fr.cmp.kyrios.repository.AppRepository;
import fr.cmp.kyrios.repository.CategorieSIRepository;
import fr.cmp.kyrios.repository.DirectionRepository;
import fr.cmp.kyrios.repository.DomaineRepository;
import fr.cmp.kyrios.repository.EmploiRepository;
import fr.cmp.kyrios.repository.ProfilAppProfilSIRepository;
import fr.cmp.kyrios.repository.ProfilAppRepository;
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

        @Autowired
        private AppRepository appRepository;

        @Autowired
        private ProfilAppRepository profilAppRepository;

        @Autowired
        private ProfilAppProfilSIRepository profilAppProfilSIRepository;

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
                emp.setEmploiName("Developpeur Fullstack");
                emp.setDirection(dir);
                emp.setService(svcInfra);
                emp.setDomaine(domJava);
                emp.setStatus(EmploiModel.Status.PERMANENT);
                emp.setProfilSI(profil1);
                emp.setDateCreated(LocalDateTime.now());
                emp = emploiRepository.save(emp);
                ;
                EmploiModel emp2 = new EmploiModel();
                emp2.setEmploiName("Developpeur Windev");
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
                RessourceSIModel resAgenceComptable = createRessource(catRepertoiresService, "SVC_Agence Comptable",
                                RessourceSIModel.TypeAcces.LECTURE_ECRITURE);
                RessourceSIModel resAssesseurs = createRessource(catRepertoiresService, "SVC_Assesseurs",
                                RessourceSIModel.TypeAcces.LECTURE_ECRITURE);
                RessourceSIModel resBureau = createRessource(catRepertoiresService, "SVC_Bureau",
                                RessourceSIModel.TypeAcces.LECTURE_ECRITURE);
                RessourceSIModel resComptabilite = createRessource(catRepertoiresService, "SVC_Comptabilité",
                                RessourceSIModel.TypeAcces.LECTURE_ECRITURE);

                // ===== RESSOURCES SI - Répertoires fonctionnels / transverses =====
                RessourceSIModel resComitesCos = createRessource(catRepertoiresFonctionnels, "FON_COMITES\\COS",
                                RessourceSIModel.TypeAcces.LECTURE_ECRITURE);
                RessourceSIModel resComitesAlm = createRessource(catRepertoiresFonctionnels, "FON_COMITES\\COMITE ALM",
                                RessourceSIModel.TypeAcces.LECTURE_ECRITURE);
                RessourceSIModel resComitesCodir = createRessource(catRepertoiresFonctionnels, "FON_COMITES\\CODIR",
                                RessourceSIModel.TypeAcces.LECTURE_ECRITURE);
                RessourceSIModel resComitesCopil = createRessource(catRepertoiresFonctionnels, "FON_COMITES\\COPIL",
                                RessourceSIModel.TypeAcces.LECTURE_ECRITURE);

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
                dir2.getRessourcesDefault().add(resComitesCopil);
                dir2 = directionRepository.save(dir2);

                // ===== ASSIGNER LES RESSOURCES AUX PROFILS SI =====
                // Profil 1 : Assigner la direction et ses ressources par défaut
                profil1.setDirection(dir);
                profil1 = profilSIRepository.save(profil1); // Sauvegarder d'abord pour avoir un ID

                // Créer les associations avec les ressources
                for (RessourceSIModel ressource : dir.getRessourcesDefault()) {
                        associateRessourceToProfile(profil1, ressource, ressource.getTypeAcces());
                }
                profil1 = profilSIRepository.save(profil1);

                // Profil 2 : Assigner la direction et ses ressources par défaut
                profil2.setDirection(dir2);
                profil2 = profilSIRepository.save(profil2); // Sauvegarder d'abord pour avoir un ID

                // Créer les associations avec les ressources
                for (RessourceSIModel ressource : dir2.getRessourcesDefault()) {
                        associateRessourceToProfile(profil2, ressource, ressource.getTypeAcces());
                }
                profil2 = profilSIRepository.save(profil2);

                AppModel app = new AppModel();
                app.setName("THEMIS");
                app.setDirection(dir);
                app.setDescription("Application de gestion THEMIS");
                app.setDateCreated(LocalDateTime.now());
                app = appRepository.save(app);

                AppModel app2 = new AppModel();
                app2.setName("FLUXA");
                app2.setDirection(dir2);
                app2.setDescription("Application de gestion FLUXA");
                app2.setDateCreated(LocalDateTime.now());
                app2 = appRepository.save(app2);

                ProfilAppModel profilApp1 = new ProfilAppModel();
                profilApp1.setName("Developpeur");
                profilApp1.setApplication(app);
                profilApp1.setDateCreated(LocalDateTime.now());
                profilApp1 = profilAppRepository.save(profilApp1);

                ProfilAppProfilSI liaison1 = new ProfilAppProfilSI();
                liaison1.setProfilApp(profilApp1);
                liaison1.setProfilSI(profil1);
                liaison1.setApplication(app);
                liaison1 = profilAppProfilSIRepository.save(liaison1);

                ProfilAppProfilSI liaison2 = new ProfilAppProfilSI();
                liaison2.setProfilApp(profilApp1);
                liaison2.setProfilSI(profil2);
                liaison2.setApplication(app);
                liaison2 = profilAppProfilSIRepository.save(liaison2);

                ProfilAppModel profilApp2 = new ProfilAppModel();
                profilApp2.setName("Developpeur");
                profilApp2.setApplication(app2);
                profilApp2.setDateCreated(LocalDateTime.now());
                profilApp2 = profilAppRepository.save(profilApp2);

                ProfilAppProfilSI liaison3 = new ProfilAppProfilSI();
                liaison3.setProfilApp(profilApp2);
                liaison3.setProfilSI(profil2);
                liaison3.setApplication(app2);
                liaison3 = profilAppProfilSIRepository.save(liaison3);

                ProfilAppModel profilApp3 = new ProfilAppModel();
                profilApp3.setName("Comptable");
                profilApp3.setApplication(app2);
                profilApp3.setDateCreated(LocalDateTime.now());
                profilApp3 = profilAppRepository.save(profilApp3);

                ProfilAppProfilSI liaison4 = new ProfilAppProfilSI();
                liaison4.setProfilApp(profilApp3);
                liaison4.setProfilSI(profil1);
                liaison4.setApplication(app2);
                liaison4 = profilAppProfilSIRepository.save(liaison4);

                System.out.println("Données de test initialisées avec succès!");

        }

        /**
         * Crée une ressource SI
         */
        private RessourceSIModel createRessource(CategorieSIModel categorie, String name,
                        RessourceSIModel.TypeAcces typeAcces) {
                RessourceSIModel ressource = new RessourceSIModel();
                ressource.setCategorie(categorie);
                ressource.setName(name);
                ressource.setTypeAcces(typeAcces);
                return ressourceSIRepository.save(ressource);
        }

        /**
         * Associe une ressource à un profil SI avec un type d'accès spécifique
         */
        private void associateRessourceToProfile(ProfilSIModel profil, RessourceSIModel ressource,
                        RessourceSIModel.TypeAcces typeAcces) {
                ProfilSIRessource profilSIRessource = new ProfilSIRessource();
                profilSIRessource.setProfilSI(profil);
                profilSIRessource.setRessource(ressource);
                profilSIRessource.setTypeAcces(typeAcces);
                profil.getProfilSIRessources().add(profilSIRessource);
        }

}
