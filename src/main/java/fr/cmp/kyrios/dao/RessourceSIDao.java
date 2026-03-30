package fr.cmp.kyrios.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class RessourceSIDao {

    private final JdbcTemplate jdbcTemplate;

    public RessourceSIDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<RessourceReadRow> findAll() {
        String sql = """
                SELECT r.id, r.categorie_id, c.name AS categorie_name, r.name, r.type_acces
                FROM ressource_si r
                JOIN categories c ON c.id = r.categorie_id
                ORDER BY r.id
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new RessourceReadRow(
                rs.getInt("id"),
                rs.getInt("categorie_id"),
                rs.getString("categorie_name"),
                rs.getString("name"),
                rs.getString("type_acces")));
    }

    public Optional<RessourceReadRow> findById(int id) {
        String sql = """
                SELECT r.id, r.categorie_id, c.name AS categorie_name, r.name, r.type_acces
                FROM ressource_si r
                JOIN categories c ON c.id = r.categorie_id
                WHERE r.id = ?
                """;

        List<RessourceReadRow> rows = jdbcTemplate.query(sql, (rs, rowNum) -> new RessourceReadRow(
                rs.getInt("id"),
                rs.getInt("categorie_id"),
                rs.getString("categorie_name"),
                rs.getString("name"),
                rs.getString("type_acces")), id);

        return rows.stream().findFirst();
    }

    public List<RessourceReadRow> findByCategorieId(int categorieId) {
        String sql = """
                SELECT r.id, r.categorie_id, c.name AS categorie_name, r.name, r.type_acces
                FROM ressource_si r
                JOIN categories c ON c.id = r.categorie_id
                WHERE r.categorie_id = ?
                ORDER BY r.id
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new RessourceReadRow(
                rs.getInt("id"),
                rs.getInt("categorie_id"),
                rs.getString("categorie_name"),
                rs.getString("name"),
                rs.getString("type_acces")), categorieId);
    }

    public List<RessourceReadRow> findDefaultByDirectionId(int directionId) {
        String sql = """
                SELECT r.id, r.categorie_id, c.name AS categorie_name, r.name, r.type_acces
                FROM direction_ressources_default drd
                JOIN ressource_si r ON r.id = drd.ressource_id
                JOIN categories c ON c.id = r.categorie_id
                WHERE drd.direction_id = ?
                ORDER BY c.name, r.name
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new RessourceReadRow(
                rs.getInt("id"),
                rs.getInt("categorie_id"),
                rs.getString("categorie_name"),
                rs.getString("name"),
                rs.getString("type_acces")), directionId);
    }

    public boolean existsByName(String name) {
        String sql = "SELECT COUNT(1) FROM ressource_si WHERE name = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, name);
        return count != null && count > 0;
    }

    public int insert(int categorieId, String name, String typeAcces) {
        String sql = "INSERT INTO ressource_si (categorie_id, name, type_acces) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            var stmt = connection.prepareStatement(sql, new String[] { "id" });
            stmt.setInt(1, categorieId);
            stmt.setString(2, name);
            stmt.setString(3, typeAcces);
            return stmt;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalStateException("Impossible de recuperer l'ID de la ressource SI creee");
        }
        return key.intValue();
    }

    public void deleteDefaultLinksByRessourceId(int ressourceId) {
        jdbcTemplate.update("DELETE FROM direction_ressources_default WHERE ressource_id = ?", ressourceId);
    }

    public void deleteProfilLinksByRessourceId(int ressourceId) {
        jdbcTemplate.update("DELETE FROM profil_si_ressources WHERE ressource_id = ?", ressourceId);
    }

    public void deleteById(int id) {
        jdbcTemplate.update("DELETE FROM ressource_si WHERE id = ?", id);
    }

    public record RessourceReadRow(int id, int categorieId, String categorieName, String name, String typeAcces) {
    }
}
