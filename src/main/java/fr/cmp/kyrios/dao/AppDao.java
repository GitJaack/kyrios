package fr.cmp.kyrios.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class AppDao {

    private final JdbcTemplate jdbcTemplate;

    public AppDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<AppReadRow> findAll() {
        String sql = """
                SELECT a.id,
                       a.name,
                       a.direction_id,
                       d.name AS direction_name,
                       a.description,
                       a.date_created
                FROM applications a
                  LEFT JOIN directions d ON d.id = a.direction_id
                ORDER BY a.id
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new AppReadRow(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("direction_id"),
                rs.getString("direction_name"),
                rs.getString("description"),
                rs.getTimestamp("date_created") != null ? rs.getTimestamp("date_created").toLocalDateTime() : null));
    }

    public Optional<AppReadRow> findById(int id) {
        String sql = """
                SELECT a.id,
                       a.name,
                       a.direction_id,
                       d.name AS direction_name,
                       a.description,
                       a.date_created
                FROM applications a
                  LEFT JOIN directions d ON d.id = a.direction_id
                WHERE a.id = ?
                """;

        List<AppReadRow> rows = jdbcTemplate.query(sql, (rs, rowNum) -> new AppReadRow(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("direction_id"),
                rs.getString("direction_name"),
                rs.getString("description"),
                rs.getTimestamp("date_created") != null ? rs.getTimestamp("date_created").toLocalDateTime() : null),
                id);

        return rows.stream().findFirst();
    }

    public boolean existsByName(String name) {
        String sql = "SELECT COUNT(1) FROM applications WHERE name = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, name);
        return count != null && count > 0;
    }

    public boolean existsByNameExcludingId(String name, int excludedId) {
        String sql = "SELECT COUNT(1) FROM applications WHERE name = ? AND id <> ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, name, excludedId);
        return count != null && count > 0;
    }

    public int insert(String name, int directionId, String description, LocalDateTime dateCreated) {
        String sql = "INSERT INTO applications (name, direction_id, description, date_created) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            var stmt = connection.prepareStatement(sql, new String[] { "id" });
            stmt.setString(1, name);
            stmt.setInt(2, directionId);
            stmt.setString(3, description);
            stmt.setObject(4, dateCreated);
            return stmt;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalStateException("Impossible de recuperer l'ID de l'application creee");
        }
        return key.intValue();
    }

    public void update(int id, String name, int directionId, String description) {
        String sql = "UPDATE applications SET name = ?, direction_id = ?, description = ? WHERE id = ?";
        jdbcTemplate.update(sql, name, directionId, description, id);
    }

    public void deleteById(int id) {
        jdbcTemplate.update("DELETE FROM applications WHERE id = ?", id);
    }

    public record AppReadRow(int id, String name, int directionId, String directionName,
            String description, LocalDateTime dateCreated) {
    }
}
