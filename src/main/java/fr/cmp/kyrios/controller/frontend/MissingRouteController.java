package fr.cmp.kyrios.controller.frontend;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MissingRouteController {

    @GetMapping("/settings")
    public String settingsNotFound(Model model, HttpServletResponse response) {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return renderNotFound(model, "/settings");
    }

    private String renderNotFound(Model model, String path) {
        model.addAttribute("currentPage", "");
        model.addAttribute("pageTitle", "Page non trouvee");
        model.addAttribute("pageHeader", "Erreur 404");
        model.addAttribute("pageDescription", "La page demandee est introuvable.");
        model.addAttribute("pageCss", "error");
        model.addAttribute("contentPage", "error404.jsp");
        model.addAttribute("requestedPath", path);
        return "layout";
    }
}
