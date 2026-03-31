package fr.cmp.kyrios.controller.frontend;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import fr.cmp.kyrios.model.Si.dto.profilSI.ProfilSIDTOResponse;
import fr.cmp.kyrios.model.common.IdNameDTO;
import fr.cmp.kyrios.service.ReferenceDataService;
import fr.cmp.kyrios.service.ProfilSIService;
import fr.cmp.kyrios.util.DateTimeUtil;

@Controller

public class UpdateProfilSIController {
    @Autowired
    private ProfilSIService profilSIService;

    @Autowired
    private ReferenceDataService referenceDataService;

    @GetMapping("/profilSI/edit/{id}")
    public String updateProfilSI(@PathVariable int id, Model model) {
        ProfilSIDTOResponse profil = profilSIService.getById(id);
        List<IdNameDTO> directions = referenceDataService.getDirections();
        Integer directionId = resolveIdByName(directions, profil.getDirection());

        model.addAttribute("currentPage", "/profilSI");
        model.addAttribute("pageTitle", "Modification du profil SI");
        model.addAttribute("pageHeader", "Modification du profil SI : n°" + id + " " + profil.getName());
        model.addAttribute("pageDescription",
                "Dernière modification : " + DateTimeUtil.formatDisplayDateTime(profil.getDateUpdated()));
        model.addAttribute("contentPage", "updateProfilSI.jsp");
        model.addAttribute("pageCss", "form");
        model.addAttribute("profilId", id);
        model.addAttribute("directionId", directionId != null ? directionId : 0);
        model.addAttribute("categories", referenceDataService.getCategoriesWithRessources());
        model.addAttribute("directions", referenceDataService.getDirectionsWithDefaultRessources());

        return "layout";
    }

    private Integer resolveIdByName(List<IdNameDTO> options, String name) {
        if (name == null || name.isBlank()) {
            return null;
        }
        return options.stream()
                .filter(option -> name.equals(option.getName()))
                .map(IdNameDTO::getId)
                .findFirst()
                .orElse(null);
    }

}
