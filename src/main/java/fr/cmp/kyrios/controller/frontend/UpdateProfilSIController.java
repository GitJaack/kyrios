package fr.cmp.kyrios.controller.frontend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import fr.cmp.kyrios.model.Si.ProfilSIModel;
import fr.cmp.kyrios.repository.Emploi.DirectionRepository;
import fr.cmp.kyrios.repository.Si.CategorieSIRepository;
import fr.cmp.kyrios.service.ProfilSIService;
import fr.cmp.kyrios.util.DateTimeUtil;

@Controller

public class UpdateProfilSIController {
    @Autowired
    private ProfilSIService profilSIService;

    @Autowired
    private CategorieSIRepository categorieSIRepository;

    @Autowired
    private DirectionRepository directionRepository;

    @GetMapping("/profilSI/edit/{id}")
    public String updateProfilSI(@PathVariable int id, Model model) {
        ProfilSIModel profil = profilSIService.getById(id);

        model.addAttribute("currentPage", "/profilSI");
        model.addAttribute("pageTitle", "Modification du profil SI");
        model.addAttribute("pageHeader", "Modification du profil SI : n°" + id + " " + profil.getName());
        model.addAttribute("pageDescription", "Dernière modification : "
                + DateTimeUtil.formatDisplayDateTime(profil.getDateUpdated()));
        model.addAttribute("contentPage", "updateProfilSI.jsp");
        model.addAttribute("pageCss", "createProfilSI");
        model.addAttribute("profilId", id);
        model.addAttribute("directionId", profil.getDirection() != null ? profil.getDirection().getId() : 0);
        model.addAttribute("categories", categorieSIRepository.findAll());
        model.addAttribute("directions", directionRepository.findAll());

        return "layout";
    }

}
