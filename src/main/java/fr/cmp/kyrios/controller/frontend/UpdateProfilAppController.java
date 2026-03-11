package fr.cmp.kyrios.controller.frontend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import fr.cmp.kyrios.model.App.ProfilAppModel;
import fr.cmp.kyrios.repository.Si.ProfilSIRepository;
import fr.cmp.kyrios.service.ProfilAppService;
import fr.cmp.kyrios.util.DateTimeUtil;

@Controller
public class UpdateProfilAppController {
    @Autowired
    private ProfilAppService profilAppService;

    @Autowired
    private ProfilSIRepository profilSIRepository;

    @GetMapping("/profil-app/edit/{id}")
    public String updateProfilApp(@PathVariable int id, Model model) {
        ProfilAppModel profilApp = profilAppService.getById(id);

        model.addAttribute("profilApp", profilApp);
        model.addAttribute("currentPage", "/profil-app");
        model.addAttribute("pageTitle", "Modification du profil applicatif");
        model.addAttribute("pageHeader", "Modification du profil applicatif : " + profilApp.getName());
        model.addAttribute("pageDescription",
                "Dernière modification : " + DateTimeUtil.formatDisplayDateTime(profilApp.getDateUpdated()));
        model.addAttribute("contentPage", "updateProfilApp.jsp");
        model.addAttribute("pageCssCommon", "form");
        model.addAttribute("pageCss", "createProfilApp");

        model.addAttribute("profilsSI", profilSIRepository.findAll());

        return "layout";
    }

}
