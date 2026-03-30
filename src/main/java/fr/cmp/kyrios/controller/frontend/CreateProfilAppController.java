package fr.cmp.kyrios.controller.frontend;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import fr.cmp.kyrios.model.common.IdNameDTO;
import fr.cmp.kyrios.service.AppService;
import fr.cmp.kyrios.service.ProfilSIService;

@Controller
public class CreateProfilAppController {
    @Autowired
    private AppService appService;

    @Autowired
    private ProfilSIService profilSIService;

    @GetMapping("/profil-app/create")
    public String createProfilApp(Model model) {
        model.addAttribute("currentPage", "/profil-app/create");
        model.addAttribute("pageTitle", "Création du profil applicatif");
        model.addAttribute("pageHeader", "Création du profil applicatif");
        model.addAttribute("contentPage", "createProfilApp.jsp");
        model.addAttribute("pageCssCommon", "form");
        model.addAttribute("pageCss", "createProfilApp");

        List<IdNameDTO> apps = appService.listAll().stream()
                .map(app -> new IdNameDTO(app.getId(), app.getName()))
                .toList();

        List<IdNameDTO> profilsSI = profilSIService.listAll().stream()
                .map(profil -> new IdNameDTO(profil.getIdProfilSI(), profil.getName()))
                .toList();

        model.addAttribute("apps", apps);
        model.addAttribute("profilsSI", profilsSI);

        return "layout";
    }
}
