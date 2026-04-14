package fr.cmp.kyrios.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import fr.cmp.kyrios.model.common.IdNameDTO;

@Service
public class ReferenceDataService {

    private final JdbcTemplate jdbcTemplate;

    public ReferenceDataService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<IdNameDTO> getDirections() {
        return fetchIdName("directions");
    }

    public List<IdNameDTO> getServices() {
        return fetchIdName("services");
    }

    public List<IdNameDTO> getDomaines() {
        return fetchIdName("domaines");
    }

    public List<IdNameDTO> getProfilsSI() {
        return fetchIdName("profils_si");
    }

    public List<Map<String, Object>> getCategoriesWithRessources() {
        String sql = """
                SELECT c.id AS categorie_id,
                       c.name AS categorie_name,
                       r.id AS ressource_id,
                       r.name AS ressource_name
                FROM categories c
                LEFT JOIN ressource_si r ON r.categorie_id = c.id
                  ORDER BY c.id, r.id
                """;

        Map<Integer, Map<String, Object>> categories = new LinkedHashMap<>();

        jdbcTemplate.query(sql, rs -> {
            int categorieId = rs.getInt("categorie_id");
            String categorieName = rs.getString("categorie_name");
            Map<String, Object> categorie = categories.computeIfAbsent(categorieId, id -> {
                Map<String, Object> c = new LinkedHashMap<>();
                c.put("id", categorieId);
                c.put("name", categorieName);
                c.put("ressources", new ArrayList<Map<String, Object>>());
                return c;
            });

            Integer ressourceId = (Integer) rs.getObject("ressource_id");
            if (ressourceId != null) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> ressources = (List<Map<String, Object>>) categorie.get("ressources");

                Map<String, Object> ressource = new LinkedHashMap<>();
                ressource.put("id", ressourceId);
                ressource.put("name", rs.getString("ressource_name"));
                ressources.add(ressource);
            }
        });

        return new ArrayList<>(categories.values());
    }

    public List<Map<String, Object>> getDirectionsWithDefaultRessources() {
        String sql = """
                SELECT d.id AS direction_id,
                       d.name AS direction_name,
                       r.id AS ressource_id,
                  COALESCE(drd.type_acces, r.type_acces) AS type_acces
                FROM directions d
                LEFT JOIN direction_ressources_default drd ON drd.direction_id = d.id
                LEFT JOIN ressource_si r ON r.id = drd.ressource_id
                ORDER BY d.name, r.id
                """;

        Map<Integer, Map<String, Object>> directions = new LinkedHashMap<>();

        jdbcTemplate.query(sql, rs -> {
            int directionId = rs.getInt("direction_id");
            String directionName = rs.getString("direction_name");
            Map<String, Object> direction = directions.computeIfAbsent(directionId, id -> {
                Map<String, Object> d = new LinkedHashMap<>();
                d.put("id", directionId);
                d.put("name", directionName);
                d.put("ressourcesDefault", new ArrayList<Map<String, Object>>());
                return d;
            });

            Integer ressourceId = (Integer) rs.getObject("ressource_id");
            if (ressourceId != null) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> ressourcesDefault = (List<Map<String, Object>>) direction
                        .get("ressourcesDefault");

                Map<String, Object> ressource = new LinkedHashMap<>();
                ressource.put("id", ressourceId);
                ressource.put("typeAcces", rs.getString("type_acces"));
                ressourcesDefault.add(ressource);
            }
        });

        return new ArrayList<>(directions.values());
    }

    private List<IdNameDTO> fetchIdName(String table) {
        String sql = "SELECT id, name FROM " + table + " ORDER BY name";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new IdNameDTO(rs.getInt("id"), rs.getString("name")));
    }
}