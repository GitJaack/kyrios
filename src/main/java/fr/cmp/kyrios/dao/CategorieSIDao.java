package fr.cmp.kyrios.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class CategorieSIDao {

    private final JdbcTemplate jdbcTemplate;

    public CategorieSIDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<CategorieReadRow> findAll() {
        String sql = "SELECT id, name FROM categories ORDER BY id";
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> new CategorieReadRow(rs.getInt("id"), rs.getString("name")));
    }

    public Optional<CategorieReadRow> findById(int id) {
        String sql = "SELECT id, name FROM categories WHERE id = ?";
        List<CategorieReadRow> rows = jdbcTemplate.query(sql,
                (rs, rowNum) -> new CategorieReadRow(rs.getInt("id"), rs.getString("name")),
                id);
        return rows.stream().findFirst();
    }

    public boolean existsByName(String name) {
        String sql = "SELECT COUNT(1) FROM categories WHERE name = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, name);
        return count != null && count > 0;
    }

    public boolean existsByNameExcludingId(String name, int excludedId) {
        String sql = "SELECT COUNT(1) FROM categories WHERE name = ? AND id <> ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, name, excludedId);
        return count != null && count > 0;
    }

    public int insert(String name) {
        String sql = "INSERT INTO categories (name) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            var stmt = connection.prepareStatement(sql, new String[] { "id" });
            stmt.setString(1, name);
            return stmt;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalStateException("Impossible de recuperer l'ID de la categorie creee");
        }
        return key.intValue();
    }

    public void updateName(int id, String name) {
        jdbcTemplate.update("UPDATE categories SET name = ? WHERE id = ?", name, id);
    }

    public void deleteDefaultRessourceByCategorieId(int categorieId) {
        String sql = """
                DELETE FROM direction_ressources_default
                WHERE ressource_id IN (SELECT id FROM ressource_si WHERE categorie_id = ?)
                """;
        jdbcTemplate.update(sql, categorieId);
    }

    public void deleteProfilRessourceByCategorieId(int categorieId) {
        String sql = """
                DELETE FROM profil_si_ressources
                WHERE ressource_id IN (SELECT id FROM ressource_si WHERE categorie_id = ?)
                """;
        jdbcTemplate.update(sql, categorieId);
    }

    public void deleteRessourcesByCategorieId(int categorieId) {
        jdbcTemplate.update("DELETE FROM ressource_si WHERE categorie_id = ?", categorieId);
    }

    public void deleteById(int id) {
        jdbcTemplate.update("DELETE FROM categories WHERE id = ?", id);
    }

    public record CategorieReadRow(int id, String name) {
    }
}
