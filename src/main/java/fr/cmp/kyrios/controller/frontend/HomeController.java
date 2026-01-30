package fr.cmp.kyrios.controller.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("currentPage", "/");
        model.addAttribute("pageTitle", "Accueil");
        model.addAttribute("contentPage", "home.jsp");
        return "layout";
    }
}
