package fr.cmp.kyrios.controller.frontend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import fr.cmp.kyrios.repository.App.AppRepository;
import fr.cmp.kyrios.repository.App.ProfilAppRepository;

@Controller
public class HomeProfilAppController {
    @Autowired
    private AppRepository appRepository;

    @Autowired
    private ProfilAppRepository profilAppRepository;

    @GetMapping("/profil-app")
    public String profilApp(Model model) {
        model.addAttribute("currentPage", "/profil-app");
        model.addAttribute("pageTitle", "Gestion des profils applicatifs");
        model.addAttribute("pageHeader", "Gestion des profils applicatifs");
        model.addAttribute("contentPage", "homeProfilApp.jsp");
        model.addAttribute("pageCss", "homeProfilApp");

        model.addAttribute("apps", appRepository.findAll());
        model.addAttribute("profilApps", profilAppRepository.findAll());

        return "layout";
    }

}
