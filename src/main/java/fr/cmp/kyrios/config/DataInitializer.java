package fr.cmp.kyrios.config;

import java.sql.PreparedStatement;

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
                if (!initDataEnabled) {
                        System.out.println("[DATA_INIT] Initialisation des donnees desactivee (app.init-data=false).");
                        return;
                }

                Integer directionCount = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM directions", Integer.class);
                if (directionCount != null && directionCount > 0) {
                        System.out.println("[DATA_INIT] Donnees deja presentes, initialisation ignoree.");
                        return;
                }

                System.out.println("[DATA_INIT] Demarrage de l'initialisation des donnees de test...");

                String[] directions = {
                                "Agence comptable",
                                "Direction de la Communication et des Partenariats",
                                "Direction de la Securite",
                                "Direction de l'Inclusion et de la Culture Financieres",
                                "Direction des Ressources Humaines et de la Modernisation",
                                "Direction des services techniques",
                                "Direction des Systemes d'Information",
                                "Direction des ventes, expertises et conservation",
                                "Direction du Pret sur gage",
                                "Direction Financiere",
                                "Direction Generale",
                                "Direction générale adjointe"
                };

                for (String direction : directions) {
                        insertAndGetId("INSERT INTO directions (name) VALUES (?)", direction);
                }

                String[] services = {
                                "Budget",
                                "CCART",
                                "Controle de gestion",
                                "Corps de controle",
                                "Epargne",
                                "Expertise",
                                "Travaux",
                                "Guichets payeurs",
                                "Hotel des Ventes",
                                "Juridique et Marchés publics",
                                "Logistique et moyens généraux",
                                "Magasins",
                                "Maintenance",
                                "Middle Office",
                                "Pole relation à distance",
                                "Restaurant",
                                "Sécurité",
                                "Service des Prêts sur gage",
                                "Trésorerie"
                };

                for (String service : services) {
                        insertAndGetId("INSERT INTO services (name) VALUES (?)", service);
                }

                String[] domaines = {
                                "Clic&Clou",
                                "CP",
                                "DPO",
                                "GRC",
                                "IG",
                                "LCB-FT",
                                "Paie",
                                "Parcours budget",
                                "Partenariat",
                                "PRD",
                                "PSG",
                                "PSG Clic&Clou",
                                "Régie caisse",
                                "SSI",
                                "Validation renou."
                };

                for (String domaine : domaines) {
                        insertAndGetId("INSERT INTO domaines (name) VALUES (?)", domaine);
                }

                int appThemis = 0;

                String[] applications = {
                                "Thémis",
                                "IRIS",
                                "Enchères",
                                "SAB",
                                "Lexis Nexis",
                                "Fluxa",
                                "PEP"
                };

                for (String application : applications) {
                        int appId = insertAndGetId("INSERT INTO applications (name) VALUES (?)", application);
                        if ("Thémis".equals(application)) {
                                appThemis = appId;
                        }
                }

                int catRepertoireServices = insertAndGetId("INSERT INTO categories (name) VALUES (?)",
                                "Repertoires de services");

                String[] repertoireServiceRessources = {
                                "SVC_Budget",
                                "SVC_CI",
                                "SVC_CI_Controle permanent",
                                "SVC_CI_Gestion des risques et conformite",
                                "SVC_CI_Inspection generale",
                                "SVC_CI_LCB-FT",
                                "SVC_COMM",
                                "SVC_DF",
                                "SVC_DF_Controle de gestion",
                                "SVC_DF_Controle de gestion_Tableaux de bord",
                                "SVC_DF_Epargne solidaire",
                                "SVC_DF_Epargne solidaire_Numerisation",
                                "SVC_DF_Tresorerie-ALM",
                                "SVC_DICF",
                                "SVC_DICF_CFC",
                                "SVC_DICF_Parcours Budget Permanents",
                                "SVC_DICF_Dossier de Direction DICF",
                                "SVC_Direction de la Securite",
                                "SVC_Direction de la Sécurité_Plannings",
                                "SVC_Direction Generale",
                                "SVC_DPO",
                                "SVC_DRH",
                                "SVC_DSI",
                                "SVC_DST",
                                "SVC_PSG",
                                "SVC_PSG_Controles PSG",
                                "SVC_PSG_Dossiers de Direction PSG",
                                "SVC_PSG_Formatuteur",
                                "SVC_PSG_Management PSG",
                                "SVC_PSG_Pole relation à Distance ",
                                "SVC_PSG_Formation",
                                "SVC_PSG_RH & Plannings",
                                "SVC_SJUR-MP",
                                "SVC_SSI",
                                "SVC_DVEC",
                                "SVC_DVEC_CCART",
                                "SVC_DVEC_Hotel des ventes"
                };

                for (String ressource : repertoireServiceRessources) {
                        insertServiceResource(catRepertoireServices, ressource);
                }

                int catRepertoiresFonctionnelsTransverses = insertAndGetId("INSERT INTO categories (name) VALUES (?)",
                                "Repertoires fonctionnels / transverses");

                String[] repertoiresFonctionnelsTransversesRessources = {
                                "FON_COMITES\\COS",
                                "FON_COMITES\\COMITE ALM",
                                "FON_COMITES\\COMITE D'AUDIT",
                                "FON_COMITES\\COMITE DES PRESTATIONS EXTERNALISEES",
                                "FON_COMITES\\COMITE DES RISQUES",
                                "FON_COMITES\\COMITE DES RISQUES DE CREDITS",
                                "FON_COMITES\\COMITE LCB-FT",
                                "FON_CONTROLE",
                                "FON_CONTROLE\\ALM",
                                "FON_CONTROLE\\Extracts video",
                                "FON_CONTROLE\\Reportings reglementaires",
                                "FON_CONTROLE\\Reportings reglementaires\\Remises",
                                "FON_CONTROLE\\Requisitions",
                                "FON_DIRECTIONS",
                                "FON_DIRECTIONS\\Agence comptable\\Boni PSG",
                                "FON_DIRECTIONS\\Agence comptable\\Caisse PSG",
                                "FON_DIRECTIONS\\Agence comptable\\Caisse PSG\\Virements en attente",
                                "FON_DIRECTIONS\\Agence comptable\\Debiteurs divers",
                                "FON_DIRECTIONS\\Agence comptable\\DSI",
                                "FON_DIRECTIONS\\Agence comptable\\Epargne",
                                "FON_DIRECTIONS\\Agence comptable\\Inventaires",
                                "FON_DIRECTIONS\\Agence comptable\\locataires",
                                "FON_DIRECTIONS\\Agence comptable\\Régie CCART",
                                "FON_DIRECTIONS\\Arretes comptables",
                                "FON_DIRECTIONS\\BUDGET",
                                "FON_DIRECTIONS\\CONTROLE\\SJMP",
                                "FON_DIRECTIONS\\CONTROLE\\Agence comptable",
                                "FON_DIRECTIONS\\DCDM",
                                "FON_DIRECTIONS\\DCDM\\DICF",
                                "FON_DIRECTIONS\\DCDM\\DG",
                                "FON_DIRECTIONS\\DCDM\\DRHM",
                                "FON_DIRECTIONS\\DCDM\\DSI",
                                "FON_DIRECTIONS\\DCDM\\HDV",
                                "FON_DIRECTIONS\\DRHM\\PSG",
                                "FON_DIRECTIONS\\DRHM\\CENTRALE-ACHAT-RIDF",
                                "FON_DIRECTIONS\\DRHM-EAE",
                                "FON_DIRECTIONS\\Magasins",
                                "FON_DIRECTIONS\\Magasins\\Magasins PSG&HDV",
                                "FON_DIRECTIONS\\Marchés Publics",
                                "FON_DIRECTIONS\\PARAPHEUR BUDGET-DG",
                                "FON_DIRECTIONS\\PARAPHEUR RH-DG",
                                "FON_DIRECTIONS\\PROCEDURES",
                                "FON_DIRECTIONS\\Projet Epargne Personnes Morales",
                                "FON_Interfaces",
                                "FON_Paie",
                                "FON_REP-DIR",
                                "GRH2000 et Arrêtés-PROD"
                };

                for (String ressource : repertoiresFonctionnelsTransversesRessources) {
                        insertServiceResource(catRepertoiresFonctionnelsTransverses, ressource);
                }

                int catBoitesMessageriePartagees = insertAndGetId("INSERT INTO categories (name) VALUES (?)",
                                "Boites de messagerie partagées");

                String[] BoitesMessageriePartageesRessources = {
                                "1pourcentmarchedelart@creditmunicipal.fr",
                                "agencecomptable-cmp@creditmunicipal.fr",
                                "boni@creditmunicipal.fr",
                                "BUDGET-CMP@creditmunicipal.fr",
                                "ccart@creditmunicipal.fr",
                                "cgt@creditmunicipal.fr",
                                "communication@creditmunicipal.fr",
                                "conference@creditmunicipal.fr",
                                "conformite@creditmunicipal.fr",
                                "contact@ccart.paris",
                                "compteurimprimantes@creditmunicipal.fr",
                                "deontologue@creditmunicipal.fr",
                                "direction-psg@creditmunicipal.fr",
                                "dpo@creditmunicipal.fr",
                                "droitdalerte-cmp@creditmunicipal.fr",
                                "epargne-solidarité@creditmunicipal.fr",
                                "expertise@creditmunicipal.fr",
                                "exploitation@creditmunicipal.fr",
                                "immobilier@creditmunicipal.fr",
                                "innovation@creditmunicipal.fr",
                                "labft-cmpbq@creditmunicipal.fr",
                                "location@creditmunicipal.fr",
                                "maintenance@creditmunicipal.fr",
                                "microcredit-et-accompagnement@creditmunicipal.fr",
                                "monobjet@creditmunicipal.fr",
                                "moyensgeneraux@creditmunicipal.fr",
                                "ne-pas-repondre@creditmunicipal.fr",
                                "pcb@creditmunicipal.fr",
                                "pointbudget@creditmunicipal.fr",
                                "pret@ccart.paris",
                                "pretsurgage@creditmunicipal.fr",
                                "pretsurgage_archives@creditmunicipal.fr",
                                "rapports-controles@creditmunicipal.fr",
                                "rec.epargne-solidarite@creditmunicipal.fr",
                                "reclamation@creditmunicipal.fr",
                                "reclamationvente@creditmunicipal.fr",
                                "recrutement-cmp@creditmunicipal.fr",
                                "Restaurant-entreprise@creditmunicipal.fr",
                                "rdvenligne@creditmunicipal.fr",
                                "securite-cmp@creditmunicipal.fr",
                                "securite.info@creditmunicipal.fr",
                                "service-client.psg@creditmunicipal.fr",
                                "service-it@creditmunicipal.fr",
                                "service-marchespublics@creditmunicipal.fr",
                                "sfact-cmp@creditmunicipal.fr",
                                "stopviolences@creditmunicipal.fr",
                                "test-kertios@creditmunicipal.fr",
                                "voixduclient@creditmunicipal.fr",
                                "zabbix@creditmunicipal.fr",
                                "zextras@creditmunicipal.fr",
                                "zextras_pretsurgage@creditmunicipal.fr"
                };

                for (String ressource : BoitesMessageriePartageesRessources) {
                        insertServiceResource(catBoitesMessageriePartagees, ressource);
                }

                int catBureautiqueSpe = insertAndGetId("INSERT INTO categories (name) VALUES (?)",
                                "Bureautique spe.");

                String[] bureautiqueSpeRessources = {
                                "3CX",
                                "AutoCAD",
                                "AutoCAD (viewer)",
                                "DRIVE CMP",
                                "MS Project",
                                "Passbolt",
                                "Visio 2016"
                };

                for (String ressource : bureautiqueSpeRessources) {
                        insertServiceResource(catBureautiqueSpe, ressource);
                }

                int catApplicationsMetiers = insertAndGetId("INSERT INTO categories (name) VALUES (?)",
                                "Applications métiers");

                String[] applicationsMetiersRessources = {
                                "ABIS Access V3",
                                "AXWAY (secure transport et EBICS-TS)",
                                "Back office metier site epargne",
                                "CCSupervision",
                                "CIVILNet RH",
                                "DVP",
                                "Encheres SVV (Innexa)",
                                "Epsitronic (main courante)",
                                "Espace de consultation de comptes clients (presta. Standard telephonique)",
                                "E-temptation (Admin)",
                                "FIS (tresorerie)",
                                "FLUXA (gestion des virements)",
                                "GLPI (Admin)",
                                "Immos.net (production)",
                                "Intranet (admin)",
                                "IRIS",
                                "Matomo",
                                "METABASE (LCB-FT / DICF / BI)",
                                "Milestone (videosurveillance IP)",
                                "NOTITIA (Standard telephonique)",
                                "Network (videosurveillance analogique)",
                                "PEP (ex WINM9 de GFI)",
                                "Themis",
                                "Regie.net (prod)",
                                "SAB (CMP Banque)",
                                "SAB (Epargne)",
                                "Site ccart.paris (admin)",
                                "Site creditmunicipal.fr (admin)",
                                "Site Epargne (Admin)",
                                "Studio Protecsys 2 + P2 Web (badge d'acces)",
                                "Support Espace Personnel",
                                "Synapse (portail POBI de la BDF pour MAJ FICP)",
                                "Testo Saveris",
                                "Vbank"
                };

                for (String ressource : applicationsMetiersRessources) {
                        insertServiceResource(catApplicationsMetiers, ressource);
                }

                int catApplicationsExterne = insertAndGetId("INSERT INTO categories (name) VALUES (?)",
                                "Applications hebergees en externe");

                String[] applicationsExterneRessources = {
                                "CIVILNet RH (support My.cirilgroup)",
                                "Horoquartz support (Etemptation et Protecsys)",
                                "ADAPTEL",
                                "ADOBE suite (Admin)",
                                "APEC",
                                "Arkea-Banque (Saas)",
                                "Banque populaire Mediterranee (Saas)",
                                "Banque populaire Rhone Alpes (Saas)",
                                "BIP",
                                "BOVP",
                                "BOAMP",
                                "CANVA",
                                "Caisse Epargne IDF",
                                "CDC banques des territoires / cdc.net (saas)",
                                "CDC.net (Saas)",
                                "CEGID (Saas)",
                                "CIC Ouest (Saas)",
                                "CIRCL (Saas)",
                                "CJN (+ PROCONNECT?)",
                                "CNAS (saas)",
                                "CNRACL (Saas)",
                                "COFFREO",
                                "CPF",
                                "crea-sol.credit (plateforme microcrédit CREASOL)",
                                "Credit Agricole Paris (Saas)",
                                "Credit Agricole Touraine Poitou (Saas)",
                                "Dalloz",
                                "Demarches simplifiees",
                                "Digitouch (acces biometrique)",
                                "EBALO",
                                "Ebanks (Saas)",
                                "EDENRED",
                                "Emerit (montres connectees PTI)",
                                "Ensemble des reseaux sociaux",
                                "Etafi",
                                "France Travail",
                                "Google Ads",
                                "Google Analytics",
                                "Impot.gouv (Saas)",
                                "Info greffe ",
                                "Interencheres Atlas ",
                                "Interencheres Pro",
                                "LexisNexis (Saas)",
                                "LexisNexis 360 Juridique",
                                "Mailchimp (Saas)",
                                "Mapping controle (geolocalisation)",
                                "Maximilien (Marches publics)",
                                "Maximilen (Pastell actes)",
                                "Maximilien (Parapheur + clef RGS** Certeurope)",
                                "Myatlas",
                                "MySimplyAgenda",
                                "Net.Entreprise (Saas)",
                                "OneGate (Saas)",
                                "ORIAS (Saas)",
                                "Pasos.ecb.europa.ue (Saas Facturation BCE)",
                                "Portail de la gestion publique",
                                "Portail DGFIP",
                                "Portail Web Epistronic (acces distance main courante)",
                                "R Studio",
                                "Securescharing.bnpparibas (Plateforme BNPP pour ABIS)",
                                "Sendinblue.com ",
                                "Societe Generale (compta)",
                                "SOFAXIS",
                                "SPC Connect (gestion des centrales d'alarme)",
                                "SystemPay",
                                "Target (Saas)",
                                "Teambrain",
                                "TITAN",
                                "TRELLO",
                                "URSSAF",
                                "WILLIS TIXERS WATSON (Gras Savoye)"
                };

                for (String ressource : applicationsExterneRessources) {
                        insertServiceResource(catApplicationsExterne, ressource);
                }

                String[][] themisRessources = {
                                { "BTSY", "Acces Synthese Client" },
                                { "BTAC", "Accueil" },
                                { "BTAD", "Administrateur" },
                                { "BTAR", "Annulation reglements" },
                                { "BTAP", "Art et Patrimoine" },
                                { "BTCC", "Chargé de Clientele" },
                                { "BTCT", "Comptabilite" },
                                { "BT1C", "Compteur - Creation" },
                                { "BT2C", "Compteur - Modification" },
                                { "BTVC", "Compteur - Visualisation" },
                                { "BTCF", "Conformite" },
                                { "BTCO", "Correspondance" },
                                { "BTTC", "Courriers textes" },
                                { "BTVV", "CPVille - Visualisation" },
                                { "BTEC", "Decroissance encours" },
                                { "BTVP", "Degagement VP" },
                                { "BTLI", "Devalidation liquidation" },
                                { "BT1D", "Dico - Creation" },
                                { "BT2D", "Dico - Modification" },
                                { "BTVD", "Dico - Visualisation" },
                                { "BTDI", "Divers" },
                                { "BTSII", "DSI ou Agent comptable pour CAC" },
                                { "BT1E", "Employe - Creation" },
                                { "BT2E", "Employe - Modification des droits" },
                                { "BTVE", "Employe - Visualisation" },
                                { "BTOS", "Expertise assesseur" },
                                { "BTE1", "Exploitation" },
                                { "BTFO", "Fiche octroi engagements >10000 euros" },
                                { "DRCB", "Forcage carte bleue" },
                                { "DOBJ", "Forcage nombre objets lots" },
                                { "DCRA", "Forcer la creation de ticket" },
                                { "DCMP", "Forcer la modification du patronyme" },
                                { "DCFO", "Forcer modification famille d'objet" },
                                { "BTPR", "Gestion des produits" },
                                { "BT2G", "Guichet - Modification" },
                                { "BTVG", "Gestion ecrans-Guichets-Roulomat" },
                                { "BTGP", "Guichet Payeur" },
                                { "BT1H", "Habilitation" },
                                { "BT2H", "Habilitation - Modification" },
                                { "BTVH", "Habilitation - Visualisation" },
                                { "BTHP", "Habilitation PSG" },
                                { "BTHV", "HDV - Gestion des Ventes" },
                                { "BTMD", "HDV - Magasiniers" },
                                { "BTHS", "HDV - Secretariat" },
                                { "BTLD", "Liste Dega. Prepaye" },
                                { "BTLR", "Liste des rendez-vous" },
                                { "BTLQ", "Liste Qualification" },
                                { "BTMP", "Magasinier PSG" },
                                { "BTMA", "Majorations differenciees" },
                                { "DVAL", "Modification date valeur lot vente" },
                                { "BTMF", "Modification fiche connaissance client" },
                                { "BTJR", "Modification SP DOC Justificatif de revenu" },
                                { "BTEP", "Pilotage Ecran de" },
                                { "DCBP", "Prescription d'un boni Lever la" },
                                { "DQMB", "QMATIC Autorise fermeture ecran en auto" },
                                { "DQMA", "QMATIC Passe appel en mode manuel" },
                                { "DCVT", "RAZ ou modification frais preparation" },
                                { "BTBE", "Reedition des bulletin engagement" },
                                { "BMBE", "Reexpertise, modif. Bulletin expertise" },
                                { "BT1P", "Reexpertise, brise(perte) COS" },
                                { "DRPR", "Remises sur penalites de retard" },
                                { "DREN", "Renouvelable ou non Modif" },
                                { "BTER", "Renouvellements eligibles administrateur" },
                                { "BTEA", "Renouvellements eligibles assesseurs" },
                                { "BT2R", "Repertoire - Modification" },
                                { "BTVR", "Repertoire - Visualisation" },
                                { "BTRH", "Ressources Humaines" },
                                { "DANL", "Restauration ligne engagement caisse" },
                                { "BTRE", "Retour expertise" },
                                { "BTRV", "Revenus" },
                                { "DRGP", "RGPD suppression du tiers" },
                                { "BTSW", "SAFE_WATCH Liste gels d'avoirs ou de" },
                                { "BTVI", "Saisie virement manuel" },
                                { "DSCA", "Scan entree-sortie en mag 2,3, ou 4" },
                                { "BT1S", "Serveur vocal : double reglement" },
                                { "BT2S", "Serveur vocal : erreur de donnees" },
                                { "BTSI", "Service informatique seulement" },
                                { "BT2T", "Taux - Modification" },
                                { "BTVT", "Taux - Visualisation" },
                                { "BTEL", "Televersement" },
                                { "DTIE", "Transfert contrat tiers A a tiers B" },
                                { "BTTB", "Travaux planifies" },
                                { "BTA1", "Variables Autorise a ajouter des" }
                };

                for (String[] themisRessource : themisRessources) {
                        insertAppResource(appThemis, themisRessource[0], themisRessource[1]);
                }

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

        private int insertServiceResource(int categorieId, String name) {
                return insertAndGetId(
                                "INSERT INTO ressource_si (categorie_id, name, type_acces) VALUES (?, ?, ?)",
                                categorieId,
                                name,
                                "LECTURE_ECRITURE");
        }

        private int insertAppResource(int applicationId, String name, String description) {
                return insertAndGetId(
                                "INSERT INTO ressource_app (application_id, name, description) VALUES (?, ?, ?)",
                                applicationId,
                                name,
                                description);
        }
}
