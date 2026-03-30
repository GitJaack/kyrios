package fr.cmp.kyrios.controller.frontend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("currentPage", "/");
        model.addAttribute("pageTitle", "Accueil - Kyrios");
        model.addAttribute("pageHeader", "Accueil");
        model.addAttribute("pageDescription", "Bienvenue sur Kyrios");
        model.addAttribute("contentPage", "home.jsp");
        model.addAttribute("pageCss", "home");

        model.addAttribute("profilSICount", count("profils_si"));
        model.addAttribute("emploiCount", count("emplois"));
        model.addAttribute("profilAppCount", count("profil_app"));
        model.addAttribute("applicationCount", count("applications"));

        return "layout";
    }

    private long count(String table) {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM " + table, Integer.class);
        return count != null ? count.longValue() : 0L;
    }
}
