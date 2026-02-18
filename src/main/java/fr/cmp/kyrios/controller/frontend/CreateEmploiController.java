package fr.cmp.kyrios.controller.frontend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import fr.cmp.kyrios.repository.Emploi.DirectionRepository;
import fr.cmp.kyrios.repository.Emploi.ServiceRepository;
import fr.cmp.kyrios.repository.Emploi.DomaineRepository;
import fr.cmp.kyrios.repository.Si.ProfilSIRepository;
import fr.cmp.kyrios.model.Emploi.EmploiModel;

@Controller
public class CreateEmploiController {
    @Autowired
    private DirectionRepository directionRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private DomaineRepository domaineRepository;

    @Autowired
    private ProfilSIRepository profilSIRepository;

    @GetMapping("/emploi/create")
    public String createEmploi(Model model) {
        model.addAttribute("currentPage", "/emploi/create");
        model.addAttribute("pageTitle", "Création d'un emploi");
        model.addAttribute("pageHeader", "Création d'un emploi");
        model.addAttribute("contentPage", "createEmploi.jsp");
        model.addAttribute("pageCss", "form");

        model.addAttribute("directions", directionRepository.findAll());
        model.addAttribute("services", serviceRepository.findAll());
        model.addAttribute("domaines", domaineRepository.findAll());
        model.addAttribute("profilsSI", profilSIRepository.findAll());
        model.addAttribute("statusOptions", EmploiModel.Status.values());

        return "layout";
    }
}
