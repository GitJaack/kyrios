package fr.cmp.kyrios.controller.frontend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fr.cmp.kyrios.model.Si.ProfilSIModel;
import fr.cmp.kyrios.model.Si.ProfilSIRessource;
import fr.cmp.kyrios.service.ProfilSIService;

@Controller
public class ViewProfilSIController {
    @Autowired
    private ProfilSIService profilSIService;

    @GetMapping("/profilSI/view/{id}")
    public String viewProfilSI(@PathVariable int id, Model model) {
        ProfilSIModel profil = profilSIService.getById(id);

        Map<String, List<ProfilSIRessource>> ressourcesByCategorie = new LinkedHashMap<>();
        if (profil.getProfilSIRessources() != null) {
            for (ProfilSIRessource profilRessource : profil.getProfilSIRessources()) {
                String categorieName = profilRessource.getRessource().getCategorie().getName();
                ressourcesByCategorie
                        .computeIfAbsent(categorieName, key -> new ArrayList<>())
                        .add(profilRessource);
            }
        }

        model.addAttribute("currentPage", "/profilSI");
        model.addAttribute("pageTitle", "Consultation du profil SI");
        model.addAttribute("pageHeader", "Consultation du profil SI : n°" + id + " " + profil.getName());
        model.addAttribute("pageDescription", "Dernière modification : " + profil.getFormattedDateUpdated());
        model.addAttribute("contentPage", "viewProfilSI.jsp");
        model.addAttribute("pageCss", "viewProfilSI");

        model.addAttribute("profilSI", profil);
        model.addAttribute("ressourcesByCategorie", ressourcesByCategorie);

        return "layout";
    }
}
