package fr.cmp.kyrios.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ReferenceDao {

    private final JdbcTemplate jdbcTemplate;

    public ReferenceDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean existsDirectionById(int id) {
        return existsById("directions", id);
    }

    public boolean existsServiceById(int id) {
        return existsById("services", id);
    }

    public boolean existsDomaineById(int id) {
        return existsById("domaines", id);
    }

    public boolean existsApplicationById(int id) {
        return existsById("applications", id);
    }

    public boolean existsProfilSIById(int id) {
        return existsById("profils_si", id);
    }

    public boolean existsRessourceSIById(int id) {
        return existsById("ressource_si", id);
    }

    public boolean existsEmploiById(int id) {
        return existsById("emplois", id);
    }

    public Optional<String> findDirectionNameById(int id) {
        return findNameById("directions", id);
    }

    public Optional<String> findApplicationNameById(int id) {
        return findNameById("applications", id);
    }

    public Optional<String> findProfilSINameById(int id) {
        return findNameById("profils_si", id);
    }

    public Optional<String> findServiceNameById(Integer id) {
        if (id == null) {
            return Optional.empty();
        }
        return findNameById("services", id);
    }

    public Optional<String> findDomaineNameById(Integer id) {
        if (id == null) {
            return Optional.empty();
        }
        return findNameById("domaines", id);
    }

    public Optional<RessourceAppRef> findRessourceAppById(int id) {
        String sql = "SELECT id, name, application_id FROM ressource_app WHERE id = ?";
        List<RessourceAppRef> rows = jdbcTemplate.query(sql,
                (rs, rowNum) -> new RessourceAppRef(rs.getInt("id"), rs.getString("name"), rs.getInt("application_id")),
                id);
        return rows.stream().findFirst();
    }

    private boolean existsById(String table, int id) {
        String sql = "SELECT COUNT(1) FROM " + table + " WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    private Optional<String> findNameById(String table, int id) {
        String sql = "SELECT name FROM " + table + " WHERE id = ?";
        List<String> rows = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("name"), id);
        return rows.stream().findFirst();
    }

    public record RessourceAppRef(int id, String name, int applicationId) {
    }
}
