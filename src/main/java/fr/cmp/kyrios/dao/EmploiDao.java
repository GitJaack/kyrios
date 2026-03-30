package fr.cmp.kyrios.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class EmploiDao {

    private final JdbcTemplate jdbcTemplate;

    public EmploiDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<EmploiReadRow> findAll() {
        String sql = """
                SELECT e.id,
                       e.emploi_name,
                       d.name AS direction_name,
                       s.name AS service_name,
                       dm.name AS domaine_name,
                       e.status,
                       p.id AS profil_si_id,
                       p.name AS profil_si_name,
                       e.date_created,
                       e.date_updated
                FROM emplois e
                JOIN directions d ON d.id = e.direction_id
                LEFT JOIN services s ON s.id = e.service_id
                LEFT JOIN domaines dm ON dm.id = e.domaine_id
                LEFT JOIN profils_si p ON p.id = e.profil_si_id
                ORDER BY e.id
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new EmploiReadRow(
                rs.getInt("id"),
                rs.getString("emploi_name"),
                rs.getString("direction_name"),
                rs.getString("service_name"),
                rs.getString("domaine_name"),
                rs.getString("status"),
                (Integer) rs.getObject("profil_si_id"),
                rs.getString("profil_si_name"),
                rs.getTimestamp("date_created") != null ? rs.getTimestamp("date_created").toLocalDateTime() : null,
                rs.getTimestamp("date_updated") != null ? rs.getTimestamp("date_updated").toLocalDateTime() : null));
    }

    public Optional<EmploiReadRow> findById(int id) {
        String sql = """
                SELECT e.id,
                       e.emploi_name,
                       d.name AS direction_name,
                       s.name AS service_name,
                       dm.name AS domaine_name,
                       e.status,
                       p.id AS profil_si_id,
                       p.name AS profil_si_name,
                       e.date_created,
                       e.date_updated
                FROM emplois e
                JOIN directions d ON d.id = e.direction_id
                LEFT JOIN services s ON s.id = e.service_id
                LEFT JOIN domaines dm ON dm.id = e.domaine_id
                LEFT JOIN profils_si p ON p.id = e.profil_si_id
                WHERE e.id = ?
                """;

        List<EmploiReadRow> rows = jdbcTemplate.query(sql, (rs, rowNum) -> new EmploiReadRow(
                rs.getInt("id"),
                rs.getString("emploi_name"),
                rs.getString("direction_name"),
                rs.getString("service_name"),
                rs.getString("domaine_name"),
                rs.getString("status"),
                (Integer) rs.getObject("profil_si_id"),
                rs.getString("profil_si_name"),
                rs.getTimestamp("date_created") != null ? rs.getTimestamp("date_created").toLocalDateTime() : null,
                rs.getTimestamp("date_updated") != null ? rs.getTimestamp("date_updated").toLocalDateTime() : null),
                id);

        return rows.stream().findFirst();
    }

    public int insert(String emploiName, int directionId, Integer serviceId, Integer domaineId,
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

    public void update(int id, String emploiName, int directionId, Integer serviceId, Integer domaineId,
            String status, int profilSIId, LocalDateTime dateUpdated) {
        String sql = """
                UPDATE emplois
                SET emploi_name = ?, direction_id = ?, service_id = ?, domaine_id = ?,
                    status = ?, profil_si_id = ?, date_updated = ?
                WHERE id = ?
                """;

        jdbcTemplate.update(sql,
                emploiName,
                directionId,
                serviceId,
                domaineId,
                status,
                profilSIId,
                dateUpdated,
                id);
    }

    public void deleteById(int id) {
        jdbcTemplate.update("DELETE FROM emplois WHERE id = ?", id);
    }

    public record EmploiReadRow(int id, String emploiName, String direction, String service, String domaine,
            String status, Integer profilSIId, String profilSIName,
            LocalDateTime dateCreated, LocalDateTime dateUpdated) {
    }
}
