package fr.cmp.kyrios.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ProfilSIDao {

    private final JdbcTemplate jdbcTemplate;

    public ProfilSIDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ProfilSIReadRow> findAll() {
        String sql = """
                SELECT psi.id, psi.name, psi.direction_id, d.name AS direction_name, psi.date_created, psi.date_updated
                FROM profils_si psi
                LEFT JOIN directions d ON d.id = psi.direction_id
                ORDER BY psi.id
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new ProfilSIReadRow(
                rs.getInt("id"),
                rs.getString("name"),
                (Integer) rs.getObject("direction_id"),
                rs.getString("direction_name"),
                rs.getTimestamp("date_created") != null ? rs.getTimestamp("date_created").toLocalDateTime() : null,
                rs.getTimestamp("date_updated") != null ? rs.getTimestamp("date_updated").toLocalDateTime() : null));
    }

    public List<ProfilSIReadRow> findByDirectionId(int directionId) {
        String sql = """
                SELECT psi.id, psi.name, psi.direction_id, d.name AS direction_name, psi.date_created, psi.date_updated
                FROM profils_si psi
                LEFT JOIN directions d ON d.id = psi.direction_id
                WHERE psi.direction_id = ?
                ORDER BY psi.id
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new ProfilSIReadRow(
                rs.getInt("id"),
                rs.getString("name"),
                (Integer) rs.getObject("direction_id"),
                rs.getString("direction_name"),
                rs.getTimestamp("date_created") != null ? rs.getTimestamp("date_created").toLocalDateTime() : null,
                rs.getTimestamp("date_updated") != null ? rs.getTimestamp("date_updated").toLocalDateTime() : null),
                directionId);
    }

    public Optional<ProfilSIReadRow> findById(int id) {
        String sql = """
                SELECT psi.id, psi.name, psi.direction_id, d.name AS direction_name, psi.date_created, psi.date_updated
                FROM profils_si psi
                LEFT JOIN directions d ON d.id = psi.direction_id
                WHERE psi.id = ?
                """;

        List<ProfilSIReadRow> rows = jdbcTemplate.query(sql, (rs, rowNum) -> new ProfilSIReadRow(
                rs.getInt("id"),
                rs.getString("name"),
                (Integer) rs.getObject("direction_id"),
                rs.getString("direction_name"),
                rs.getTimestamp("date_created") != null ? rs.getTimestamp("date_created").toLocalDateTime() : null,
                rs.getTimestamp("date_updated") != null ? rs.getTimestamp("date_updated").toLocalDateTime() : null),
                id);

        return rows.stream().findFirst();
    }

    public List<RessourceReadRow> findRessourcesByProfilSIId(int profilSIId) {
        String sql = """
                SELECT r.id, c.name AS categorie_name, r.name, psr.type_acces
                FROM profil_si_ressources psr
                JOIN ressource_si r ON r.id = psr.ressource_id
                JOIN categories c ON c.id = r.categorie_id
                WHERE psr.profil_si_id = ?
                ORDER BY c.id, r.id
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new RessourceReadRow(
                rs.getInt("id"),
                rs.getString("categorie_name"),
                rs.getString("name"),
                rs.getString("type_acces")), profilSIId);
    }

    public List<ProfilAppReadRow> findProfilAppsByProfilSIId(int profilSIId) {
        String sql = """
                SELECT pa.id, pa.name, a.name AS application_name
                FROM profil_app_profil_si paps
                JOIN profil_app pa ON pa.id = paps.profil_app_id
                JOIN applications a ON a.id = paps.application_id
                WHERE paps.profil_si_id = ?
                ORDER BY pa.id
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new ProfilAppReadRow(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("application_name")), profilSIId);
    }

    public List<EmploiReadRow> findEmploisByProfilSIId(int profilSIId) {
        String sql = """
                SELECT e.id, e.emploi_name
                FROM emplois e
                WHERE e.profil_si_id = ?
                ORDER BY e.id
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new EmploiReadRow(
                rs.getInt("id"),
                rs.getString("emploi_name")), profilSIId);
    }

    public boolean existsByName(String name) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(1) FROM profils_si WHERE name = ?",
                Integer.class,
                name);
        return count != null && count > 0;
    }

    public boolean existsByNameExcludingId(String name, int excludedId) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(1) FROM profils_si WHERE name = ? AND id <> ?",
                Integer.class,
                name,
                excludedId);
        return count != null && count > 0;
    }

    public int insertProfilSI(String name, int directionId, LocalDateTime dateCreated) {
        String sql = "INSERT INTO profils_si (name, direction_id, date_created) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            var stmt = connection.prepareStatement(sql, new String[] { "id" });
            stmt.setString(1, name);
            stmt.setInt(2, directionId);
            stmt.setObject(3, dateCreated);
            return stmt;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalStateException("Impossible de récupérer l'ID du profil SI créé");
        }
        return key.intValue();
    }

    public void updateProfilSI(int id, String name, LocalDateTime dateUpdated) {
        jdbcTemplate.update("UPDATE profils_si SET name = ?, date_updated = ? WHERE id = ?", name, dateUpdated, id);
    }

    public void deleteProfilSIRessourcesByProfilSIId(int profilSIId) {
        jdbcTemplate.update("DELETE FROM profil_si_ressources WHERE profil_si_id = ?", profilSIId);
    }

    public void insertProfilSIRessource(int profilSIId, int ressourceId, String typeAcces) {
        jdbcTemplate.update(
                "INSERT INTO profil_si_ressources (profil_si_id, ressource_id, type_acces) VALUES (?, ?, ?)",
                profilSIId,
                ressourceId,
                typeAcces);
    }

    public List<RessourceSimpleRow> findDefaultRessourcesByDirectionId(int directionId) {
        String sql = """
                SELECT r.id, r.type_acces
                FROM direction_ressources_default drd
                JOIN ressource_si r ON r.id = drd.ressource_id
                WHERE drd.direction_id = ?
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new RessourceSimpleRow(
                rs.getInt("id"),
                rs.getString("type_acces")), directionId);
    }

    public int insertEmploi(String emploiName, int directionId, Integer serviceId, Integer domaineId,
            String status, int profilSIId, LocalDateTime dateCreated) {
        String sql = """
                INSERT INTO emplois (emploi_name, direction_id, service_id, domaine_id, status, profil_si_id, date_created)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            var stmt = connection.prepareStatement(sql, new String[] { "id" });
            stmt.setString(1, emploiName);
            stmt.setInt(2, directionId);
            if (serviceId == null) {
                stmt.setNull(3, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(3, serviceId);
            }
            if (domaineId == null) {
                stmt.setNull(4, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(4, domaineId);
            }
            stmt.setString(5, status);
            stmt.setInt(6, profilSIId);
            stmt.setObject(7, dateCreated);
            return stmt;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalStateException("Impossible de récupérer l'ID de l'emploi créé");
        }
        return key.intValue();
    }

    public void detachEmploisByProfilSIId(int profilSIId) {
        jdbcTemplate.update("UPDATE emplois SET profil_si_id = NULL WHERE profil_si_id = ?", profilSIId);
    }

    public void deleteProfilAppLinksByProfilSIId(int profilSIId) {
        jdbcTemplate.update("DELETE FROM profil_app_profil_si WHERE profil_si_id = ?", profilSIId);
    }

    public void deleteProfilSIById(int profilSIId) {
        jdbcTemplate.update("DELETE FROM profils_si WHERE id = ?", profilSIId);
    }

    public record ProfilSIReadRow(int id, String name, Integer directionId, String direction,
            LocalDateTime dateCreated, LocalDateTime dateUpdated) {
    }

    public record RessourceReadRow(int id, String categorie, String name, String typeAcces) {
    }

    public record ProfilAppReadRow(int id, String name, String application) {
    }

    public record EmploiReadRow(int id, String emploiName) {
    }

    public record RessourceSimpleRow(int id, String typeAcces) {
    }
}
