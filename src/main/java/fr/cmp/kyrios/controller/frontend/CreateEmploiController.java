package fr.cmp.kyrios.controller.frontend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import fr.cmp.kyrios.model.Emploi.EmploiModel;
import fr.cmp.kyrios.service.FrontendReferenceDataService;

@Controller
public class CreateEmploiController {
    @Autowired
    private FrontendReferenceDataService frontendReferenceDataService;

    @GetMapping("/emploi/create")
    public String createEmploi(Model model) {
        model.addAttribute("currentPage", "/emploi/create");
        model.addAttribute("pageTitle", "Création d'un emploi");
        model.addAttribute("pageHeader", "Création d'un emploi");
        model.addAttribute("contentPage", "createEmploi.jsp");
        model.addAttribute("pageCss", "form");

        model.addAttribute("directions", frontendReferenceDataService.getDirections());
        model.addAttribute("services", frontendReferenceDataService.getServices());
        model.addAttribute("domaines", frontendReferenceDataService.getDomaines());
        model.addAttribute("profilsSI", frontendReferenceDataService.getProfilsSI());
        model.addAttribute("statusOptions", EmploiModel.Status.values());

        return "layout";
    }
}
