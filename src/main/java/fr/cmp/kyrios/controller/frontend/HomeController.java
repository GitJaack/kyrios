package fr.cmp.kyrios.controller.frontend;

import fr.cmp.kyrios.repository.App.AppRepository;
import fr.cmp.kyrios.repository.App.ProfilAppRepository;
import fr.cmp.kyrios.repository.Emploi.EmploiRepository;
import fr.cmp.kyrios.repository.Si.ProfilSIRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @Autowired
    private ProfilSIRepository profilSIRepository;

    @Autowired
    private EmploiRepository emploiRepository;

    @Autowired
    private ProfilAppRepository profilAppRepository;

    @Autowired
    private AppRepository applicationRepository;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("currentPage", "/");
        model.addAttribute("pageTitle", "Accueil - Kyrios");
        model.addAttribute("pageHeader", "Accueil");
        model.addAttribute("pageDescription", "Bienvenue sur Kyrios");
        model.addAttribute("contentPage", "home.jsp");
        model.addAttribute("pageCss", "home");

        model.addAttribute("profilSICount", profilSIRepository.count());
        model.addAttribute("emploiCount", emploiRepository.count());
        model.addAttribute("profilAppCount", profilAppRepository.count());
        model.addAttribute("applicationCount", applicationRepository.count());

        return "layout";
    }
}
