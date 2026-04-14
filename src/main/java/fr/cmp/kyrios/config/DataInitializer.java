package fr.cmp.kyrios.config;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.nio.charset.Charset;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Locale;
import java.util.List;

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

        @Value("${app.ressource-direction.csv.enabled}")
        private boolean ressourceDirectionCsvEnabled;

        @Value("${app.ressource-direction.csv.path}")
        private String ressourceDirectionCsvPath;

        @Value("${app.referentiel.csv.path}")
        private String referentielCsvPath;

        public DataInitializer(JdbcTemplate jdbcTemplate) {
                this.jdbcTemplate = jdbcTemplate;
        }

        @Override
        public void run(String... args) {
                if (!initDataEnabled) {
                        System.out.println("[DATA_INIT] Initialisation des donnees desactivee (app.init-data=false).");
                        return;
                }

                // Keep persistent databases compatible with permission level feature.
                jdbcTemplate.execute("ALTER TABLE profil_app_ressources ADD COLUMN IF NOT EXISTS permission_level INT");
                jdbcTemplate.execute(
                                "ALTER TABLE direction_ressources_default ADD COLUMN IF NOT EXISTS type_acces VARCHAR(32)");

                Integer directionCount = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM directions", Integer.class);
                if (directionCount != null && directionCount > 0) {
                        System.out.println(
                                        "[DATA_INIT] Donnees deja presentes.");
                        ensureThemisPermissionLevelResources();
                        ressourceSICsv();
                        ressourceDirectionDefautCsv();
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
                                "Direction Generale adjointe",
                                "Direction Epargne",
                                "Direction Juridique"
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

                ressourceSICsv();

                int catThemisCodeEcran = insertAppCategory(appThemis, "Code ecran");

                String[][] themisCodeEcranRessources = {
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

                for (String[] themisRessource : themisCodeEcranRessources) {
                        insertAppResource(appThemis, catThemisCodeEcran, themisRessource[0], themisRessource[1]);
                }

                int catThemisDroits = insertAppCategory(appThemis, "Droits");

                String[][] themisDroitsRessources = {
                                { "Niveau de permission", "" },
                                { "Pret a un employe", "" },
                                { "Delais exceptionnels", "" },
                                { "Edition duplicat", "" },
                };

                for (String[] themisDroit : themisDroitsRessources) {
                        insertAppResource(appThemis, catThemisDroits, themisDroit[0], themisDroit[1]);
                }

                int catThemisDroitsSituationsParticulieres = insertAppCategory(appThemis,
                                "Droits de mise et levée des situations particulières (SP)");

                String[][] themisDroitsSituationsParticulieresRessources = {
                                { "BV", "Blocage vente" },
                                { "CF", "Tentative depot contrefacon" },
                                { "CL", "Classification" },
                                { "CO", "Controler" },
                                { "DOC", "Document" },
                                { "IN", "Surveillance HDV" },
                                { "LI", "Litige" },
                                { "JU", "Juridique" },
                                { "LIJ", "Liquidations judicieres" },
                                { "MP", "Majeurs proteges" },
                                { "PO", "Police" },
                                { "RG", "Reglements en anomalie" },
                                { "RP", "Incidents conservation" },
                                { "RQ", "Requisition" },
                                { "RV", "Revendication" },
                                { "SU", "Successions" }

                };

                for (String[] themisDroitSP : themisDroitsSituationsParticulieresRessources) {
                        insertAppResource(appThemis, catThemisDroitsSituationsParticulieres, themisDroitSP[0],
                                        themisDroitSP[1]);
                }

                ressourceDirectionDefautCsv();

                System.out.println("[DATA_INIT] Donnees de test initialisees avec succes.");
        }

        private void ressourceSICsv() {
                Path csvPath = Paths.get(referentielCsvPath);
                if (!Files.exists(csvPath)) {
                        System.out.println("[DATA_INIT] Referential CSV introuvable: " + csvPath.toAbsolutePath());
                        return;
                }

                try (BufferedReader reader = Files.newBufferedReader(csvPath, Charset.forName("windows-1252"))) {
                        List<List<String>> records = readCsvRecords(reader);
                        if (records.size() < 4) {
                                System.out.println("[DATA_INIT] Referential CSV invalide: en-tetes manquants.");
                                return;
                        }

                        List<String> groupRow = records.get(1);
                        List<String> subGroupRow = records.get(2);
                        List<String> resourceRow = records.get(3);

                        Map<String, Integer> categoryIdsByName = loadIdsByNormalizedName("categories");
                        Map<String, Integer> resourceIdsByName = loadIdsByNormalizedName("ressource_si");

                        String activeCategory = null;
                        int createdCategories = 0;
                        int createdResources = 0;

                        for (int col = 0; col < resourceRow.size(); col++) {
                                String groupCell = getCell(groupRow, col);
                                String subGroupCell = getCell(subGroupRow, col);

                                String categoryFromSubGroup = labelCategorieSI(subGroupCell);
                                if (categoryFromSubGroup != null) {
                                        activeCategory = categoryFromSubGroup;
                                }

                                String categoryFromGroup = labelCategorieSI(groupCell);
                                if (categoryFromGroup != null) {
                                        activeCategory = categoryFromGroup;
                                }

                                String resourceName = getCell(resourceRow, col);
                                if (resourceName == null) {
                                        continue;
                                }

                                String canonicalCategory = activeCategory;
                                if (canonicalCategory == null) {
                                        continue;
                                }

                                String normalizedCategory = normalizeForMatching(canonicalCategory);
                                Integer categoryId = categoryIdsByName.get(normalizedCategory);
                                if (categoryId == null) {
                                        categoryId = insertAndGetId("INSERT INTO categories (name) VALUES (?)",
                                                        canonicalCategory);
                                        categoryIdsByName.put(normalizedCategory, categoryId);
                                        createdCategories++;
                                }

                                String normalizedResource = normalizeForMatching(resourceName);
                                if (resourceIdsByName.containsKey(normalizedResource)) {
                                        continue;
                                }

                                int resourceId = insertProfilSIRessource(categoryId, resourceName);
                                resourceIdsByName.put(normalizedResource, resourceId);
                                createdResources++;
                        }

                        System.out.println("[DATA_INIT] Referential CSV charge: categories creees=" + createdCategories
                                        + ", ressources creees=" + createdResources);
                } catch (Exception e) {
                        System.out.println("[DATA_INIT] Echec de lecture du referential CSV '" + referentielCsvPath
                                        + "': " + e.getMessage());
                }
        }

        private String labelCategorieSI(String label) {
                if (label == null) {
                        return null;
                }

                String normalized = normalizeForMatching(label);
                if (normalized.contains("repertoires de services")) {
                        return "Repertoires de services";
                }
                if (normalized.contains("repertoires fonctionnels / transverses")) {
                        return "Repertoires fonctionnels / transverses";
                }
                if (normalized.contains("boites de messagerie partagees")) {
                        return "Boites de messagerie partagées";
                }
                if (normalized.contains("bureautique spe")) {
                        return "Bureautique spe.";
                }
                if (normalized.contains("applications metiers")
                                || normalized.contains("applications hebergees en interne")) {
                        return "Applications métiers";
                }
                if (normalized.contains("applications hebergees en externe")) {
                        return "Applications hebergees en externe";
                }
                return null;
        }

        private void ressourceDirectionDefautCsv() {
                if (!ressourceDirectionCsvEnabled) {
                        return;
                }

                Path csvPath = Paths.get(ressourceDirectionCsvPath);
                if (!Files.exists(csvPath)) {
                        System.out.println("[DATA_INIT] Fichier CSV introuvable: " + csvPath.toAbsolutePath());
                        return;
                }

                try (BufferedReader reader = Files.newBufferedReader(csvPath, Charset.forName("windows-1252"))) {
                        List<List<String>> records = readCsvRecords(reader);
                        int imports = importCsv(records);
                        auditRessourceDirectionDefautCsv(records);
                        System.out.println("[DATA_INIT] Import CSV des ressources par defaut: " + imports
                                        + " liaison(s) direction/ressource appliquee(s).");
                } catch (Exception e) {
                        System.out.println("[DATA_INIT] Echec de lecture du fichier CSV '"
                                        + ressourceDirectionCsvPath
                                        + "': " + e.getMessage());
                }
        }

        private int importCsv(List<List<String>> records) {
                if (records.isEmpty()) {
                        return 0;
                }

                List<String> header = records.get(0);
                boolean tabular = isTabularHeader(header);

                Map<String, Integer> directionIdsByName = loadIdsByNormalizedName("directions");
                Map<String, Integer> ressourceIdsByName = loadIdsByNormalizedName("ressource_si");

                if (tabular) {
                        return importCsvTabular(records, directionIdsByName, ressourceIdsByName);
                }
                return importCsvMatrix(records, directionIdsByName, ressourceIdsByName);
        }

        private void auditRessourceDirectionDefautCsv(List<List<String>> records) {
                if (records.isEmpty()) {
                        return;
                }

                List<String> header = records.get(0);
                boolean tabular = isTabularHeader(header);

                Map<String, Integer> directionIdsByName = loadIdsByNormalizedName("directions");
                Map<String, Integer> ressourceIdsByName = loadIdsByNormalizedName("ressource_si");

                List<String> reportRows = new ArrayList<>();
                reportRows.add("status;direction;ressource;expected_type;actual_type");

                Map<LinkKey, String> expected = tabular
                                ? collectExpectedFromCsvTabular(records, directionIdsByName, ressourceIdsByName,
                                                reportRows)
                                : collectExpectedFromCsvMatrix(records, directionIdsByName, ressourceIdsByName,
                                                reportRows);

                Map<LinkKey, String> actual = chargeRessourceDirectionDefaut();
                Map<Integer, String> directionNamesById = loadNameById("directions");
                Map<Integer, String> ressourceNamesById = loadNameById("ressource_si");

                int missing = 0;
                int mismatch = 0;
                int extra = 0;
                int unresolved = 0;

                for (Map.Entry<LinkKey, String> entry : expected.entrySet()) {
                        LinkKey key = entry.getKey();
                        String expectedType = entry.getValue();
                        String actualType = actual.get(key);

                        if (actualType == null) {
                                missing++;
                                reportRows.add(String.join(";",
                                                "MISSING",
                                                csvSafe(directionNamesById.getOrDefault(key.directionId(),
                                                                "<direction#" + key.directionId() + ">")),
                                                csvSafe(ressourceNamesById.getOrDefault(key.ressourceId(),
                                                                "<ressource#" + key.ressourceId() + ">")),
                                                csvSafe(expectedType),
                                                ""));
                                continue;
                        }

                        if (!expectedType.equals(actualType)) {
                                mismatch++;
                                reportRows.add(String.join(";",
                                                "TYPE_MISMATCH",
                                                csvSafe(directionNamesById.getOrDefault(key.directionId(),
                                                                "<direction#" + key.directionId() + ">")),
                                                csvSafe(ressourceNamesById.getOrDefault(key.ressourceId(),
                                                                "<ressource#" + key.ressourceId() + ">")),
                                                csvSafe(expectedType),
                                                csvSafe(actualType)));
                        }
                }

                for (Map.Entry<LinkKey, String> entry : actual.entrySet()) {
                        if (expected.containsKey(entry.getKey())) {
                                continue;
                        }
                        LinkKey key = entry.getKey();
                        extra++;
                        reportRows.add(String.join(";",
                                        "EXTRA_IN_DB",
                                        csvSafe(directionNamesById.getOrDefault(key.directionId(),
                                                        "<direction#" + key.directionId() + ">")),
                                        csvSafe(ressourceNamesById.getOrDefault(key.ressourceId(),
                                                        "<ressource#" + key.ressourceId() + ">")),
                                        "",
                                        csvSafe(entry.getValue())));
                }

                for (String row : reportRows) {
                        if (row.startsWith("CSV_UNKNOWN_")) {
                                unresolved++;
                        }
                }

                System.out.println("[DATA_INIT][AUDIT] CSV attendu=" + expected.size()
                                + " | DB actuel=" + actual.size()
                                + " | manquants=" + missing
                                + " | type_mismatch=" + mismatch
                                + " | extra_db=" + extra
                                + " | csv_non_resolus=" + unresolved);

                if (missing == 0 && mismatch == 0 && extra == 0 && unresolved == 0) {
                        System.out.println("[DATA_INIT][AUDIT] OK: aucun ecart detecte entre CSV et base.");
                        return;
                }

                Path reportPath = Paths.get("data", "direction-defaults-audit.csv");
                try {
                        Files.write(reportPath, reportRows, Charset.forName("UTF-8"));
                        System.out.println("[DATA_INIT][AUDIT] Rapport genere: " + reportPath.toAbsolutePath());
                } catch (Exception e) {
                        System.out.println("[DATA_INIT][AUDIT] Impossible d'ecrire le rapport: " + e.getMessage());
                }
        }

        private Map<LinkKey, String> collectExpectedFromCsvTabular(List<List<String>> records,
                        Map<String, Integer> directionIdsByName,
                        Map<String, Integer> ressourceIdsByName,
                        List<String> reportRows) {
                Map<LinkKey, String> expected = new LinkedHashMap<>();
                List<String> header = records.get(0);

                int directionCol = -1;
                int ressourceCol = -1;
                int typeAccesCol = -1;

                for (int col = 0; col < header.size(); col++) {
                        String h = normalizeHeader(header.get(col));
                        if ("direction".equals(h)) {
                                directionCol = col;
                        } else if ("ressource".equals(h) || "resource".equals(h)) {
                                ressourceCol = col;
                        } else if ("typeacces".equals(h) || "acces".equals(h) || "access".equals(h)
                                        || "type".equals(h)) {
                                typeAccesCol = col;
                        }
                }

                if (directionCol < 0 || ressourceCol < 0) {
                        return expected;
                }

                for (int rowIdx = 1; rowIdx < records.size(); rowIdx++) {
                        List<String> row = records.get(rowIdx);
                        String directionName = getCell(row, directionCol);
                        String ressourceName = getCell(row, ressourceCol);
                        String typeAccesRaw = typeAccesCol >= 0 ? getCell(row, typeAccesCol) : null;
                        if (directionName == null || ressourceName == null) {
                                continue;
                        }

                        Integer directionId = resolveIdByName(directionIdsByName, directionName);
                        Integer ressourceId = resolveIdByName(ressourceIdsByName, ressourceName);
                        if (directionId == null) {
                                reportRows.add(String.join(";",
                                                "CSV_UNKNOWN_DIRECTION",
                                                csvSafe(directionName),
                                                csvSafe(ressourceName),
                                                "",
                                                ""));
                                continue;
                        }
                        if (ressourceId == null) {
                                reportRows.add(String.join(";",
                                                "CSV_UNKNOWN_RESOURCE",
                                                csvSafe(directionName),
                                                csvSafe(ressourceName),
                                                "",
                                                ""));
                                continue;
                        }

                        String normalizedType = normalizeTypeAcces(typeAccesRaw);
                        if (normalizedType == null) {
                                normalizedType = "LECTURE_ECRITURE";
                        }

                        expected.put(new LinkKey(directionId, ressourceId), normalizedType);
                }

                return expected;
        }

        private Map<LinkKey, String> collectExpectedFromCsvMatrix(List<List<String>> records,
                        Map<String, Integer> directionIdsByName,
                        Map<String, Integer> ressourceIdsByName,
                        List<String> reportRows) {
                Map<LinkKey, String> expected = new LinkedHashMap<>();
                if (records.size() < 2) {
                        return expected;
                }

                List<String> header = records.get(0);
                for (int rowIdx = 1; rowIdx < records.size(); rowIdx++) {
                        List<String> row = records.get(rowIdx);
                        String ressourceName = getCell(row, 0);
                        if (ressourceName == null) {
                                continue;
                        }

                        Integer ressourceId = resolveIdByName(ressourceIdsByName, ressourceName);
                        if (ressourceId == null) {
                                reportRows.add(String.join(";",
                                                "CSV_UNKNOWN_RESOURCE",
                                                "",
                                                csvSafe(ressourceName),
                                                "",
                                                ""));
                                continue;
                        }

                        for (int col = 1; col < header.size(); col++) {
                                String directionName = trimToNull(header.get(col));
                                String marker = getCell(row, col);
                                if (directionName == null || marker == null) {
                                        continue;
                                }

                                Integer directionId = resolveIdByName(directionIdsByName, directionName);
                                if (directionId == null) {
                                        reportRows.add(String.join(";",
                                                        "CSV_UNKNOWN_DIRECTION",
                                                        csvSafe(directionName),
                                                        csvSafe(ressourceName),
                                                        "",
                                                        ""));
                                        continue;
                                }

                                String normalizedType = normalizeTypeAcces(marker);
                                if (normalizedType == null) {
                                        normalizedType = "LECTURE_ECRITURE";
                                }

                                expected.put(new LinkKey(directionId, ressourceId), normalizedType);
                        }
                }

                return expected;
        }

        private Map<LinkKey, String> chargeRessourceDirectionDefaut() {
                Map<LinkKey, String> actual = new LinkedHashMap<>();
                jdbcTemplate.query(
                                "SELECT direction_id, ressource_id, COALESCE(type_acces, 'LECTURE_ECRITURE') AS type_acces FROM direction_ressources_default",
                                rs -> {
                                        LinkKey key = new LinkKey(rs.getInt("direction_id"), rs.getInt("ressource_id"));
                                        actual.put(key, rs.getString("type_acces"));
                                });
                return actual;
        }

        private Map<Integer, String> loadNameById(String table) {
                Map<Integer, String> names = new HashMap<>();
                jdbcTemplate.query("SELECT id, name FROM " + table,
                                rs -> {
                                        names.put(rs.getInt("id"), rs.getString("name"));
                                });
                return names;
        }

        private String csvSafe(String value) {
                if (value == null) {
                        return "";
                }
                if (value.contains(";") || value.contains("\"") || value.contains("\n")) {
                        return "\"" + value.replace("\"", "\"\"") + "\"";
                }
                return value;
        }

        private int importCsvTabular(List<List<String>> records, Map<String, Integer> directionIdsByName,
                        Map<String, Integer> ressourceIdsByName) {
                List<String> header = records.get(0);

                int directionCol = -1;
                int ressourceCol = -1;
                int typeAccesCol = -1;

                for (int col = 0; col < header.size(); col++) {
                        String h = normalizeHeader(header.get(col));
                        if ("direction".equals(h)) {
                                directionCol = col;
                        } else if ("ressource".equals(h) || "resource".equals(h)) {
                                ressourceCol = col;
                        } else if ("typeacces".equals(h) || "acces".equals(h) || "access".equals(h)
                                        || "type".equals(h)) {
                                typeAccesCol = col;
                        }
                }

                if (directionCol < 0 || ressourceCol < 0) {
                        return 0;
                }

                int applied = 0;
                for (int rowIdx = 1; rowIdx < records.size(); rowIdx++) {
                        List<String> row = records.get(rowIdx);
                        String directionName = getCell(row, directionCol);
                        String ressourceName = getCell(row, ressourceCol);
                        String typeAccesRaw = typeAccesCol >= 0 ? getCell(row, typeAccesCol) : null;

                        if (directionName == null || ressourceName == null) {
                                continue;
                        }

                        Integer directionId = directionIdsByName.get(normalizeForMatching(directionName));
                        Integer ressourceId = ressourceIdsByName.get(normalizeForMatching(ressourceName));
                        if (directionId == null || ressourceId == null) {
                                continue;
                        }

                        String normalizedType = normalizeTypeAcces(typeAccesRaw);
                        if (normalizedType == null) {
                                normalizedType = "LECTURE_ECRITURE";
                        }

                        if (upsertDirectionDefaultResource(directionId, ressourceId, normalizedType)) {
                                applied++;
                        }
                }

                return applied;
        }

        private int importCsvMatrix(List<List<String>> records, Map<String, Integer> directionIdsByName,
                        Map<String, Integer> ressourceIdsByName) {
                if (records.size() < 2) {
                        return 0;
                }

                List<String> header = records.get(0);
                if (header.size() <= 1) {
                        return 0;
                }

                int applied = 0;
                for (int rowIdx = 1; rowIdx < records.size(); rowIdx++) {
                        List<String> row = records.get(rowIdx);
                        String ressourceName = getCell(row, 0);
                        if (ressourceName == null) {
                                continue;
                        }

                        Integer ressourceId = ressourceIdsByName.get(normalizeForMatching(ressourceName));
                        if (ressourceId == null) {
                                continue;
                        }

                        for (int col = 1; col < header.size(); col++) {
                                String directionName = trimToNull(header.get(col));
                                if (directionName == null) {
                                        continue;
                                }

                                String marker = getCell(row, col);
                                if (marker == null) {
                                        continue;
                                }

                                Integer directionId = directionIdsByName.get(normalizeForMatching(directionName));
                                if (directionId == null) {
                                        continue;
                                }

                                String normalizedType = normalizeTypeAcces(marker);
                                if (normalizedType == null) {
                                        normalizedType = "LECTURE_ECRITURE";
                                }

                                if (upsertDirectionDefaultResource(directionId, ressourceId, normalizedType)) {
                                        applied++;
                                }
                        }
                }

                return applied;
        }

        private List<List<String>> readCsvRecords(BufferedReader reader) throws Exception {
                List<List<String>> records = new java.util.ArrayList<>();
                StringBuilder buffer = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                        if (buffer.length() > 0) {
                                buffer.append("\n");
                        }
                        buffer.append(line);

                        if (!hasBalancedQuotes(buffer.toString())) {
                                continue;
                        }

                        records.add(parseCsvLine(buffer.toString()));
                        buffer.setLength(0);
                }

                if (buffer.length() > 0) {
                        records.add(parseCsvLine(buffer.toString()));
                }

                return records;
        }

        private boolean hasBalancedQuotes(String text) {
                int quoteCount = 0;
                for (int i = 0; i < text.length(); i++) {
                        if (text.charAt(i) == '"') {
                                quoteCount++;
                        }
                }
                return (quoteCount % 2) == 0;
        }

        private List<String> parseCsvLine(String line) {
                List<String> fields = new java.util.ArrayList<>();
                StringBuilder current = new StringBuilder();
                boolean inQuotes = false;

                for (int i = 0; i < line.length(); i++) {
                        char ch = line.charAt(i);
                        if (ch == '"') {
                                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                                        current.append('"');
                                        i++;
                                } else {
                                        inQuotes = !inQuotes;
                                }
                        } else if (ch == ';' && !inQuotes) {
                                fields.add(trimToNull(current.toString()));
                                current.setLength(0);
                        } else {
                                current.append(ch);
                        }
                }

                fields.add(trimToNull(current.toString()));
                return fields;
        }

        private boolean isTabularHeader(List<String> header) {
                boolean hasDirection = false;
                boolean hasRessource = false;
                for (String cell : header) {
                        String h = normalizeHeader(cell);
                        if ("direction".equals(h)) {
                                hasDirection = true;
                        }
                        if ("ressource".equals(h) || "resource".equals(h)) {
                                hasRessource = true;
                        }
                }
                return hasDirection && hasRessource;
        }

        private String getCell(List<String> row, int index) {
                if (index < 0 || index >= row.size()) {
                        return null;
                }
                return trimToNull(row.get(index));
        }

        private String normalizeHeader(String header) {
                if (header == null) {
                        return "";
                }
                return header.replace(" ", "").replace("_", "").toLowerCase(Locale.ROOT);
        }

        private String normalizeForMatching(String value) {
                if (value == null) {
                        return "";
                }

                String cleaned = value
                                .replace("\uFFFD", "")
                                .replace("\uFEFF", "")
                                .replace('\u00A0', ' ')
                                .trim()
                                .replaceAll("^[\"']+|[\"']+$", "")
                                .replaceAll("[;]+$", "");

                String stripped = Normalizer.normalize(cleaned, Normalizer.Form.NFD)
                                .replaceAll("\\p{M}+", "");
                return stripped.trim().replaceAll("\\s+", " ").toLowerCase(Locale.ROOT);
        }

        private Integer resolveIdByName(Map<String, Integer> idsByName, String rawName) {
                if (rawName == null) {
                        return null;
                }
                String normalized = normalizeForMatching(rawName);
                Integer id = idsByName.get(normalized);
                if (id != null) {
                        return id;
                }

                // Secondary normalization for common CSV corruption patterns.
                String relaxed = normalizeForMatching(rawName.replace(",", "."));
                return idsByName.get(relaxed);
        }

        private Map<String, Integer> loadIdsByNormalizedName(String table) {
                String sql = "SELECT id, name FROM " + table;
                Map<String, Integer> idsByName = new HashMap<>();
                jdbcTemplate.query(sql, rs -> {
                        int id = rs.getInt("id");
                        String name = rs.getString("name");
                        idsByName.putIfAbsent(normalizeForMatching(name), id);
                });
                return idsByName;
        }

        private String trimToNull(String value) {
                if (value == null) {
                        return null;
                }
                String trimmed = value.trim();
                return trimmed.isEmpty() ? null : trimmed;
        }

        private String normalizeTypeAcces(String raw) {
                if (raw == null) {
                        return null;
                }
                String value = raw.trim().toUpperCase(Locale.ROOT);
                return switch (value) {
                        case "L" -> "LECTURE";
                        case "E", "X" ->
                                "LECTURE_ECRITURE";
                        default -> null;
                };
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

        private int insertProfilSIRessource(int categorieId, String name) {
                return insertProfilSIRessource(categorieId, name, "LECTURE_ECRITURE");
        }

        private int insertProfilSIRessource(int categorieId, String name, String typeAcces) {
                return insertAndGetId(
                                "INSERT INTO ressource_si (categorie_id, name, type_acces) VALUES (?, ?, ?)",
                                categorieId,
                                name,
                                typeAcces);
        }

        private boolean upsertDirectionDefaultResource(int directionId, int ressourceId, String typeAcces) {
                int updated = jdbcTemplate.update(
                                """
                                                MERGE INTO direction_ressources_default (direction_id, ressource_id, type_acces)
                                                KEY (direction_id, ressource_id)
                                                VALUES (?, ?, ?)
                                                """,
                                directionId,
                                ressourceId,
                                typeAcces);
                return updated > 0;
        }

        private record LinkKey(int directionId, int ressourceId) {
        }

        private int insertAppCategory(int applicationId, String name) {
                return insertAndGetId(
                                "INSERT INTO categories_app (application_id, name) VALUES (?, ?)",
                                applicationId,
                                name);
        }

        private int insertAppResource(int applicationId, Integer categoryId, String name, String description) {
                return insertAndGetId(
                                "INSERT INTO ressource_app (application_id, category_id, name, description) VALUES (?, ?, ?, ?)",
                                applicationId,
                                categoryId,
                                name,
                                description);
        }

        private void ensureThemisPermissionLevelResources() {
                List<Integer> appIds = jdbcTemplate.query(
                                "SELECT id FROM applications WHERE LOWER(name) IN ('thémis', 'themis')",
                                (rs, rowNum) -> rs.getInt("id"));
                if (appIds.isEmpty()) {
                        return;
                }

                int themisAppId = appIds.get(0);
                List<Integer> categoryIds = jdbcTemplate.query(
                                "SELECT id FROM categories_app WHERE application_id = ? AND name = ?",
                                (rs, rowNum) -> rs.getInt("id"),
                                themisAppId,
                                "Droits");
                if (categoryIds.isEmpty()) {
                        return;
                }

                int droitsCategoryId = categoryIds.get(0);

                Integer count = jdbcTemplate.queryForObject(
                                "SELECT COUNT(1) FROM ressource_app WHERE application_id = ? AND name = ?",
                                Integer.class,
                                themisAppId,
                                "Niveau de permission");
                if (count == null || count == 0) {
                        insertAppResource(themisAppId, droitsCategoryId, "Niveau de permission", "");
                }
        }
}
