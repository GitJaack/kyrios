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

import fr.cmp.kyrios.model.Emploi.dto.EmploiDTOResponse;
import fr.cmp.kyrios.model.Si.dto.profilSI.ProfilSIDTOResponse;
import fr.cmp.kyrios.model.Si.dto.ressourceSI.RessourceSIDTO;
import fr.cmp.kyrios.service.EmploiService;
import fr.cmp.kyrios.service.ProfilSIService;
import fr.cmp.kyrios.util.DateTimeUtil;

@Controller
public class ViewProfilSIController {
    @Autowired
    private ProfilSIService profilSIService;

    @Autowired
    private EmploiService emploiService;

    @GetMapping("/profilSI/view/{id}")
    public String viewProfilSI(@PathVariable int id, Model model) {
        ProfilSIDTOResponse profil = profilSIService.getById(id);

        List<EmploiDTOResponse> emploisAssocies = emploiService.listAll().stream()
                .filter(emploi -> emploi.getProfilSI() != null && emploi.getProfilSI().getId() == id)
                .toList();

        Map<String, List<RessourceSIDTO>> ressourcesByCategorie = new LinkedHashMap<>();
        if (profil.getRessources() != null) {
            for (RessourceSIDTO ressource : profil.getRessources()) {
                String categorieName = ressource.getCategorie();
                ressourcesByCategorie
                        .computeIfAbsent(categorieName, key -> new ArrayList<>())
                        .add(ressource);
            }
        }

        model.addAttribute("currentPage", "/profilSI");
        model.addAttribute("pageTitle", "Consultation du profil SI");
        model.addAttribute("pageHeader", "Consultation du profil SI : n°" + id + " " + profil.getName());
        model.addAttribute("pageDescription", "Dernière modification : "
                + DateTimeUtil.formatDisplayDateTime(profil.getDateUpdated()));
        model.addAttribute("contentPage", "viewProfilSI.jsp");
        model.addAttribute("pageCss", "viewProfilSI");

        model.addAttribute("profilSI", profil);
        model.addAttribute("emploisAssocies", emploisAssocies);
        model.addAttribute("ressourcesByCategorie", ressourcesByCategorie);
        model.addAttribute("profilApp", profil.getProfilApps());

        return "layout";
    }
}
