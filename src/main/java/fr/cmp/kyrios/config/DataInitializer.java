package fr.cmp.kyrios.config;

import java.sql.PreparedStatement;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

        private final JdbcTemplate jdbcTemplate;

        @Value("${app.init-data:true}")
        private boolean initDataEnabled;

        public DataInitializer(JdbcTemplate jdbcTemplate) {
                this.jdbcTemplate = jdbcTemplate;
        }

        @Override
        public void run(String... args) {
                System.out.println("[DATA_INIT] Demarrage de l'initialisation des donnees de test...");

                LocalDateTime now = LocalDateTime.now();

                int dirDsi = insertAndGetId("INSERT INTO directions (name) VALUES (?)",
                                "Direction des Systemes d'Information");
                int dirAgence = insertAndGetId("INSERT INTO directions (name) VALUES (?)", "Agence Comptable");

                int svcRestaurant = insertAndGetId("INSERT INTO services (name) VALUES (?)", "Restaurant");
                insertAndGetId("INSERT INTO services (name) VALUES (?)", "Maintenance");

                int domRegie = insertAndGetId("INSERT INTO domaines (name) VALUES (?)", "Regie Caisse");
                insertAndGetId("INSERT INTO domaines (name) VALUES (?)", "PSG");

                int profil1 = insertAndGetId(
                                "INSERT INTO profils_si (name, date_created) VALUES (?, ?)",
                                "Developpeur fullstack", now);
                int profil2 = insertAndGetId(
                                "INSERT INTO profils_si (name, date_created) VALUES (?, ?)",
                                "Developpeur windev", now);

                insertAndGetId(
                                "INSERT INTO emplois (emploi_name, direction_id, service_id, domaine_id, status, profil_si_id, date_created) VALUES (?, ?, ?, ?, ?, ?, ?)",
                                "Developpeur Fullstack", dirDsi, svcRestaurant, domRegie, "PERMANENT", profil1, now);
                insertAndGetId(
                                "INSERT INTO emplois (emploi_name, direction_id, service_id, domaine_id, status, profil_si_id, date_created) VALUES (?, ?, ?, ?, ?, ?, ?)",
                                "Developpeur Windev", dirAgence, null, null, "PERMANENT", profil2, now);

                int catService = insertAndGetId("INSERT INTO categories (name) VALUES (?)", "Repertoires de service");
                int catFonctionnels = insertAndGetId("INSERT INTO categories (name) VALUES (?)",
                                "Repertoires fonctionnels / transverses");

                int resAgenceComptable = insertAndGetId(
                                "INSERT INTO ressource_si (categorie_id, name, type_acces) VALUES (?, ?, ?)",
                                catService, "SVC_Agence Comptable", "LECTURE_ECRITURE");
                int resAssesseurs = insertAndGetId(
                                "INSERT INTO ressource_si (categorie_id, name, type_acces) VALUES (?, ?, ?)",
                                catService, "SVC_Assesseurs", "LECTURE_ECRITURE");
                int resBureau = insertAndGetId(
                                "INSERT INTO ressource_si (categorie_id, name, type_acces) VALUES (?, ?, ?)",
                                catService, "SVC_Bureau", "LECTURE_ECRITURE");
                int resComptabilite = insertAndGetId(
                                "INSERT INTO ressource_si (categorie_id, name, type_acces) VALUES (?, ?, ?)",
                                catService, "SVC_Comptabilite", "LECTURE_ECRITURE");

                int resComitesCos = insertAndGetId(
                                "INSERT INTO ressource_si (categorie_id, name, type_acces) VALUES (?, ?, ?)",
                                catFonctionnels, "FON_COMITES\\COS", "LECTURE_ECRITURE");
                int resComitesAlm = insertAndGetId(
                                "INSERT INTO ressource_si (categorie_id, name, type_acces) VALUES (?, ?, ?)",
                                catFonctionnels, "FON_COMITES\\COMITE ALM", "LECTURE_ECRITURE");
                int resComitesCodir = insertAndGetId(
                                "INSERT INTO ressource_si (categorie_id, name, type_acces) VALUES (?, ?, ?)",
                                catFonctionnels, "FON_COMITES\\CODIR", "LECTURE_ECRITURE");
                int resComitesCopil = insertAndGetId(
                                "INSERT INTO ressource_si (categorie_id, name, type_acces) VALUES (?, ?, ?)",
                                catFonctionnels, "FON_COMITES\\COPIL", "LECTURE_ECRITURE");

                jdbcTemplate.update(
                                "INSERT INTO direction_ressources_default (direction_id, ressource_id) VALUES (?, ?)",
                                dirDsi,
                                resAgenceComptable);
                jdbcTemplate.update(
                                "INSERT INTO direction_ressources_default (direction_id, ressource_id) VALUES (?, ?)",
                                dirDsi,
                                resBureau);
                jdbcTemplate.update(
                                "INSERT INTO direction_ressources_default (direction_id, ressource_id) VALUES (?, ?)",
                                dirDsi,
                                resComitesCos);
                jdbcTemplate.update(
                                "INSERT INTO direction_ressources_default (direction_id, ressource_id) VALUES (?, ?)",
                                dirDsi,
                                resComitesCodir);

                jdbcTemplate.update(
                                "INSERT INTO direction_ressources_default (direction_id, ressource_id) VALUES (?, ?)",
                                dirAgence, resComptabilite);
                jdbcTemplate.update(
                                "INSERT INTO direction_ressources_default (direction_id, ressource_id) VALUES (?, ?)",
                                dirAgence, resAssesseurs);
                jdbcTemplate.update(
                                "INSERT INTO direction_ressources_default (direction_id, ressource_id) VALUES (?, ?)",
                                dirAgence, resComitesAlm);
                jdbcTemplate.update(
                                "INSERT INTO direction_ressources_default (direction_id, ressource_id) VALUES (?, ?)",
                                dirAgence, resComitesCopil);

                jdbcTemplate.update("UPDATE profils_si SET direction_id = ? WHERE id = ?", dirDsi, profil1);
                jdbcTemplate.update("UPDATE profils_si SET direction_id = ? WHERE id = ?", dirAgence, profil2);

                jdbcTemplate.update(
                                "INSERT INTO profil_si_ressources (profil_si_id, ressource_id, type_acces) VALUES (?, ?, ?)",
                                profil1, resAgenceComptable, "LECTURE_ECRITURE");
                jdbcTemplate.update(
                                "INSERT INTO profil_si_ressources (profil_si_id, ressource_id, type_acces) VALUES (?, ?, ?)",
                                profil1, resBureau, "LECTURE_ECRITURE");
                jdbcTemplate.update(
                                "INSERT INTO profil_si_ressources (profil_si_id, ressource_id, type_acces) VALUES (?, ?, ?)",
                                profil1, resComitesCos, "LECTURE_ECRITURE");
                jdbcTemplate.update(
                                "INSERT INTO profil_si_ressources (profil_si_id, ressource_id, type_acces) VALUES (?, ?, ?)",
                                profil1, resComitesCodir, "LECTURE_ECRITURE");

                jdbcTemplate.update(
                                "INSERT INTO profil_si_ressources (profil_si_id, ressource_id, type_acces) VALUES (?, ?, ?)",
                                profil2, resComptabilite, "LECTURE_ECRITURE");
                jdbcTemplate.update(
                                "INSERT INTO profil_si_ressources (profil_si_id, ressource_id, type_acces) VALUES (?, ?, ?)",
                                profil2, resAssesseurs, "LECTURE_ECRITURE");
                jdbcTemplate.update(
                                "INSERT INTO profil_si_ressources (profil_si_id, ressource_id, type_acces) VALUES (?, ?, ?)",
                                profil2, resComitesAlm, "LECTURE_ECRITURE");
                jdbcTemplate.update(
                                "INSERT INTO profil_si_ressources (profil_si_id, ressource_id, type_acces) VALUES (?, ?, ?)",
                                profil2, resComitesCopil, "LECTURE_ECRITURE");

                int appThemis = insertAndGetId(
                                "INSERT INTO applications (name, direction_id, description, date_created) VALUES (?, ?, ?, ?)",
                                "THEMIS", dirDsi, "Application de gestion THEMIS", now);
                int appFluxa = insertAndGetId(
                                "INSERT INTO applications (name, direction_id, description, date_created) VALUES (?, ?, ?, ?)",
                                "FLUXA", dirAgence, "Application de gestion FLUXA", now);

                int profilApp1 = insertAndGetId(
                                "INSERT INTO profil_app (name, application_id, date_created) VALUES (?, ?, ?)",
                                "Developpeur", appThemis, now);
                int profilApp2 = insertAndGetId(
                                "INSERT INTO profil_app (name, application_id, date_created) VALUES (?, ?, ?)",
                                "Developpeur", appFluxa, now);
                int profilApp3 = insertAndGetId(
                                "INSERT INTO profil_app (name, application_id, date_created) VALUES (?, ?, ?)",
                                "Comptable", appFluxa, now);

                jdbcTemplate.update(
                                "INSERT INTO profil_app_profil_si (profil_app_id, profil_si_id, application_id) VALUES (?, ?, ?)",
                                profilApp1, profil1, appThemis);
                jdbcTemplate.update(
                                "INSERT INTO profil_app_profil_si (profil_app_id, profil_si_id, application_id) VALUES (?, ?, ?)",
                                profilApp1, profil2, appThemis);
                jdbcTemplate.update(
                                "INSERT INTO profil_app_profil_si (profil_app_id, profil_si_id, application_id) VALUES (?, ?, ?)",
                                profilApp2, profil2, appFluxa);
                jdbcTemplate.update(
                                "INSERT INTO profil_app_profil_si (profil_app_id, profil_si_id, application_id) VALUES (?, ?, ?)",
                                profilApp3, profil1, appFluxa);

                insertAndGetId(
                                "INSERT INTO ressource_app (application_id, name, description) VALUES (?, ?, ?)",
                                appThemis, "BTSY", "Acces Synthese Client");
                insertAndGetId(
                                "INSERT INTO ressource_app (application_id, name, description) VALUES (?, ?, ?)",
                                appThemis, "BTAC", "Accueil");
                insertAndGetId(
                                "INSERT INTO ressource_app (application_id, name, description) VALUES (?, ?, ?)",
                                appThemis, "BTAD", "Administrateur");
                insertAndGetId(
                                "INSERT INTO ressource_app (application_id, name, description) VALUES (?, ?, ?)",
                                appFluxa, "Connexion/Accueil", null);
                insertAndGetId(
                                "INSERT INTO ressource_app (application_id, name, description) VALUES (?, ?, ?)",
                                appFluxa, "Televerer un document", null);

                System.out.println("[DATA_INIT] Donnees de test initialisees avec succes.");
        }

        private int insertAndGetId(String sql, Object... params) {
                KeyHolder keyHolder = new GeneratedKeyHolder();

                jdbcTemplate.update(connection -> {
                        PreparedStatement statement = connection.prepareStatement(sql, new String[] { "id" });
                        for (int i = 0; i < params.length; i++) {
                                Object value = params[i];
                                if (value == null) {
                                        statement.setNull(i + 1, java.sql.Types.NULL);
                                } else {
                                        statement.setObject(i + 1, value);
                                }
                        }
                        return statement;
                }, keyHolder);

                Number key = keyHolder.getKey();
                if (key == null) {
                        throw new IllegalStateException("Impossible de recuperer l'ID genere");
                }
                return key.intValue();
        }
}
