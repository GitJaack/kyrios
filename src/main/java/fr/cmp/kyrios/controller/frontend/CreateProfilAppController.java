package fr.cmp.kyrios.controller.frontend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import fr.cmp.kyrios.repository.App.AppRepository;
import fr.cmp.kyrios.repository.Si.ProfilSIRepository;

@Controller
public class CreateProfilAppController {
    @Autowired
    private AppRepository appRepository;

    @Autowired
    private ProfilSIRepository profilSIRepository;

    @GetMapping("/profil-app/create")
    public String createProfilApp(Model model) {
        model.addAttribute("currentPage", "/profil-app/create");
        model.addAttribute("pageTitle", "Création du profil applicatif");
        model.addAttribute("pageHeader", "Création du profil applicatif");
        model.addAttribute("contentPage", "createProfilApp.jsp");
        model.addAttribute("pageCssCommon", "form");
        model.addAttribute("pageCss", "createProfilApp");

        model.addAttribute("apps", appRepository.findAll());
        model.addAttribute("profilsSI", profilSIRepository.findAll());

        return "layout";
    }
}
