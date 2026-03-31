package fr.cmp.kyrios.controller.frontend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import fr.cmp.kyrios.model.Emploi.EmploiModel;
import fr.cmp.kyrios.service.ReferenceDataService;

@Controller
public class CreateProfilSIController {
    @Autowired
    private ReferenceDataService referenceDataService;

    @GetMapping("/profilSI/create")
    public String createProfilSI(Model model) {
        model.addAttribute("currentPage", "/profilSI/create");
        model.addAttribute("pageTitle", "Création du profil SI");
        model.addAttribute("pageHeader", "Création du profil SI");
        model.addAttribute("contentPage", "createProfilSI.jsp");
        model.addAttribute("pageCss", "form");

        model.addAttribute("directions", referenceDataService.getDirectionsWithDefaultRessources());
        model.addAttribute("services", referenceDataService.getServices());
        model.addAttribute("domaines", referenceDataService.getDomaines());
        model.addAttribute("statusOptions", EmploiModel.Status.values());

        model.addAttribute("categories", referenceDataService.getCategoriesWithRessources());

        return "layout";
    }

}
