package fr.cmp.kyrios.dao;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ProfilAppDao {

    private final JdbcTemplate jdbcTemplate;

    public ProfilAppDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ProfilAppReadRow> findAll() {
        String sql = """
                SELECT pa.id, pa.name, pa.application_id, a.name AS application_name, pa.date_created, pa.date_updated
                FROM profil_app pa
                JOIN applications a ON a.id = pa.application_id
                ORDER BY pa.id
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new ProfilAppReadRow(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("application_id"),
                rs.getString("application_name"),
                rs.getTimestamp("date_created") != null ? rs.getTimestamp("date_created").toLocalDateTime() : null,
                rs.getTimestamp("date_updated") != null ? rs.getTimestamp("date_updated").toLocalDateTime() : null));
    }

    public Optional<ProfilAppReadRow> findById(int id) {
        String sql = """
                SELECT pa.id, pa.name, pa.application_id, a.name AS application_name, pa.date_created, pa.date_updated
                FROM profil_app pa
                JOIN applications a ON a.id = pa.application_id
                WHERE pa.id = ?
                """;

        List<ProfilAppReadRow> rows = jdbcTemplate.query(sql, (rs, rowNum) -> new ProfilAppReadRow(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("application_id"),
                rs.getString("application_name"),
                rs.getTimestamp("date_created") != null ? rs.getTimestamp("date_created").toLocalDateTime() : null,
                rs.getTimestamp("date_updated") != null ? rs.getTimestamp("date_updated").toLocalDateTime() : null),
                id);

        return rows.stream().findFirst();
    }

    public List<ProfilAppReadRow> findByApplicationId(int applicationId) {
        String sql = """
                                SELECT pa.id, pa.name, pa.application_id, a.name AS application_name, pa.date_created, pa.date_updated
                FROM profil_app pa
                JOIN applications a ON a.id = pa.application_id
                WHERE pa.application_id = ?
                ORDER BY pa.id
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new ProfilAppReadRow(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("application_id"),
                rs.getString("application_name"),
                rs.getTimestamp("date_created") != null ? rs.getTimestamp("date_created").toLocalDateTime() : null,
                rs.getTimestamp("date_updated") != null ? rs.getTimestamp("date_updated").toLocalDateTime() : null),
                applicationId);
    }

    public boolean existsByNameAndApplicationId(String name, int applicationId) {
        String sql = "SELECT COUNT(1) FROM profil_app WHERE name = ? AND application_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, name, applicationId);
        return count != null && count > 0;
    }

    public boolean existsByNameAndApplicationIdExcludingId(String name, int applicationId, int excludedId) {
        String sql = "SELECT COUNT(1) FROM profil_app WHERE name = ? AND application_id = ? AND id <> ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, name, applicationId, excludedId);
        return count != null && count > 0;
    }

    public boolean existsProfilSIInApplication(int applicationId, int profilSIId) {
        String sql = "SELECT COUNT(1) FROM profil_app_profil_si WHERE application_id = ? AND profil_si_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, applicationId, profilSIId);
        return count != null && count > 0;
    }

    public boolean existsProfilSIInApplicationExcludingProfilApp(int applicationId, int profilSIId, int profilAppId) {
        String sql = """
                SELECT COUNT(1)
                FROM profil_app_profil_si
                WHERE application_id = ? AND profil_si_id = ? AND profil_app_id <> ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, applicationId, profilSIId, profilAppId);
        return count != null && count > 0;
    }

    public int insertProfilApp(String name, int applicationId, LocalDateTime dateCreated) {
        String sql = "INSERT INTO profil_app (name, application_id, date_created) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            var stmt = connection.prepareStatement(sql, new String[] { "id" });
            stmt.setString(1, name);
            stmt.setInt(2, applicationId);
            stmt.setObject(3, dateCreated);
            return stmt;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalStateException("Impossible de récupérer l'ID du profil applicatif créé");
        }
        return key.intValue();
    }

    public void updateProfilApp(int id, String name, LocalDateTime dateUpdated) {
        String sql = "UPDATE profil_app SET name = ?, date_updated = ? WHERE id = ?";
        jdbcTemplate.update(sql, name, dateUpdated, id);
    }

    public void deleteProfilSIByProfilAppId(int profilAppId) {
        jdbcTemplate.update("DELETE FROM profil_app_profil_si WHERE profil_app_id = ?", profilAppId);
    }

    public void insertProfilSILink(int profilAppId, int profilSIId, int applicationId) {
        String sql = "INSERT INTO profil_app_profil_si (profil_app_id, profil_si_id, application_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, profilAppId, profilSIId, applicationId);
    }

    public void deleteRessourcesByProfilAppId(int profilAppId) {
        jdbcTemplate.update("DELETE FROM profil_app_ressources WHERE profil_app_id = ?", profilAppId);
    }

    public void insertRessourceLink(int profilAppId, int ressourceAppId) {
        String sql = "INSERT INTO profil_app_ressources (profil_app_id, ressource_app_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, profilAppId, ressourceAppId);
    }

    public void deleteProfilAppById(int profilAppId) {
        jdbcTemplate.update("DELETE FROM profil_app WHERE id = ?", profilAppId);
    }

    public List<ProfilSIReadRow> findProfilSIByProfilAppId(int profilAppId) {
        String sql = """
                SELECT psi.id, psi.name
                FROM profil_app_profil_si paps
                JOIN profils_si psi ON psi.id = paps.profil_si_id
                WHERE paps.profil_app_id = ?
                ORDER BY psi.id
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new ProfilSIReadRow(
                rs.getInt("id"),
                rs.getString("name")), profilAppId);
    }

    public List<RessourceAppReadRow> findRessourcesByProfilAppId(int profilAppId) {
        String sql = """
                SELECT ra.id,
                       ra.name,
                       ra.description,
                       a.name AS application_name
                FROM profil_app_ressources par
                JOIN ressource_app ra ON ra.id = par.ressource_app_id
                JOIN applications a ON a.id = ra.application_id
                WHERE par.profil_app_id = ?
                ORDER BY ra.name
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new RessourceAppReadRow(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getString("application_name")), profilAppId);
    }

    public record ProfilAppReadRow(int id, String name, int applicationId, String applicationName,
            java.time.LocalDateTime dateCreated, java.time.LocalDateTime dateUpdated) {
    }

    public record ProfilSIReadRow(int id, String name) {
    }

    public record RessourceAppReadRow(int id, String name, String description, String applicationName) {
    }
}
