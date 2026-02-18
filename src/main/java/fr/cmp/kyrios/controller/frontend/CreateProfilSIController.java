package fr.cmp.kyrios.controller.frontend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import fr.cmp.kyrios.repository.Emploi.DirectionRepository;
import fr.cmp.kyrios.repository.Emploi.ServiceRepository;
import fr.cmp.kyrios.repository.Emploi.DomaineRepository;
import fr.cmp.kyrios.repository.Si.CategorieSIRepository;
import fr.cmp.kyrios.model.Emploi.EmploiModel;

@Controller
public class CreateProfilSIController {
    @Autowired
    private CategorieSIRepository categorieSIRepository;

    @Autowired
    private DirectionRepository directionRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private DomaineRepository domaineRepository;

    @GetMapping("/profilSI/create")
    public String createProfilSI(Model model) {
        model.addAttribute("currentPage", "/profilSI/create");
        model.addAttribute("pageTitle", "Création du profil SI");
        model.addAttribute("pageHeader", "Création du profil SI");
        model.addAttribute("contentPage", "createProfilSI.jsp");
        model.addAttribute("pageCss", "form");

        model.addAttribute("directions", directionRepository.findAll());
        model.addAttribute("services", serviceRepository.findAll());
        model.addAttribute("domaines", domaineRepository.findAll());
        model.addAttribute("statusOptions", EmploiModel.Status.values());

        model.addAttribute("categories", categorieSIRepository.findAll());

        return "layout";
    }

}
