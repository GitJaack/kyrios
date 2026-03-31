package fr.cmp.kyrios.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class RessourceAppDao {

    private final JdbcTemplate jdbcTemplate;

    public RessourceAppDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<RessourceAppReadRow> findAll() {
        String sql = """
                  SELECT ra.id,
                         ra.name,
                         ra.description,
                         ra.application_id,
                    a.name AS application_name,
                    ra.category_id,
                    ca.name AS category_name
                  FROM ressource_app ra
                  JOIN applications a ON a.id = ra.application_id
                LEFT JOIN categories_app ca ON ca.id = ra.category_id
                  ORDER BY ca.id, ra.id
                  """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new RessourceAppReadRow(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getInt("application_id"),
                rs.getString("application_name"),
                (Integer) rs.getObject("category_id"),
                rs.getString("category_name")));
    }

    public Optional<RessourceAppReadRow> findById(int id) {
        String sql = """
                  SELECT ra.id,
                         ra.name,
                         ra.description,
                         ra.application_id,
                    a.name AS application_name,
                    ra.category_id,
                    ca.name AS category_name
                  FROM ressource_app ra
                  JOIN applications a ON a.id = ra.application_id
                LEFT JOIN categories_app ca ON ca.id = ra.category_id
                  WHERE ra.id = ?
                  """;

        List<RessourceAppReadRow> rows = jdbcTemplate.query(sql, (rs, rowNum) -> new RessourceAppReadRow(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getInt("application_id"),
                rs.getString("application_name"),
                (Integer) rs.getObject("category_id"),
                rs.getString("category_name")), id);

        return rows.stream().findFirst();
    }

    public List<RessourceAppReadRow> findByApplicationId(int applicationId) {
        String sql = """
                  SELECT ra.id,
                         ra.name,
                         ra.description,
                         ra.application_id,
                    a.name AS application_name,
                    ra.category_id,
                    ca.name AS category_name
                  FROM ressource_app ra
                  JOIN applications a ON a.id = ra.application_id
                LEFT JOIN categories_app ca ON ca.id = ra.category_id
                  WHERE ra.application_id = ?
                  ORDER BY ca.id, ra.id
                  """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new RessourceAppReadRow(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getInt("application_id"),
                rs.getString("application_name"),
                (Integer) rs.getObject("category_id"),
                rs.getString("category_name")), applicationId);
    }

    public List<RessourceAppCategoryRow> findByCategoryByApplicationId(int applicationId) {
        String sql = """
                SELECT ca.id AS category_id,
                             ca.name AS category_name,
                             ra.id AS ressource_id,
                             ra.name AS ressource_name,
                             ra.description AS ressource_description
                FROM categories_app ca
                LEFT JOIN ressource_app ra ON ra.category_id = ca.id
                WHERE ca.application_id = ?
                UNION ALL
                SELECT 0 AS category_id,
                             'Sans categorie' AS category_name,
                             ra.id AS ressource_id,
                             ra.name AS ressource_name,
                             ra.description AS ressource_description
                FROM ressource_app ra
                WHERE ra.application_id = ?
                    AND ra.category_id IS NULL
                ORDER BY category_name, ressource_id
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new RessourceAppCategoryRow(
                rs.getInt("category_id"),
                rs.getString("category_name"),
                (Integer) rs.getObject("ressource_id"),
                rs.getString("ressource_name"),
                rs.getString("ressource_description")),
                applicationId,
                applicationId);
    }

    public boolean existsByNameAndApplicationId(String name, int applicationId) {
        String sql = "SELECT COUNT(1) FROM ressource_app WHERE name = ? AND application_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, name, applicationId);
        return count != null && count > 0;
    }

    public boolean existsByNameAndApplicationIdExcludingId(String name, int applicationId, int excludedId) {
        String sql = "SELECT COUNT(1) FROM ressource_app WHERE name = ? AND application_id = ? AND id <> ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, name, applicationId, excludedId);
        return count != null && count > 0;
    }

    public int insert(String name, String description, Integer categoryId, int applicationId) {
        String sql = "INSERT INTO ressource_app (name, description, category_id, application_id) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            var stmt = connection.prepareStatement(sql, new String[] { "id" });
            stmt.setString(1, name);
            stmt.setString(2, description);
            if (categoryId == null) {
                stmt.setNull(3, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(3, categoryId);
            }
            stmt.setInt(4, applicationId);
            return stmt;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalStateException("Impossible de recuperer l'ID de la ressource applicative creee");
        }
        return key.intValue();
    }

    public void update(int id, String name, String description, Integer categoryId) {
        String sql = "UPDATE ressource_app SET name = ?, description = ?, category_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, name, description, categoryId, id);
    }

    public void deleteById(int id) {
        jdbcTemplate.update("DELETE FROM ressource_app WHERE id = ?", id);
    }

    public record RessourceAppReadRow(int id, String name, String description, int applicationId,
            String applicationName, Integer categoryId, String categoryName) {
    }

    public record RessourceAppCategoryRow(int categoryId, String categoryName, Integer ressourceId,
            String ressourceName, String ressourceDescription) {
    }
}
