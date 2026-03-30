package fr.cmp.kyrios.controller.frontend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import fr.cmp.kyrios.model.App.dto.ProfilAppDTOResponse;
import fr.cmp.kyrios.service.ProfilAppService;
import fr.cmp.kyrios.util.DateTimeUtil;

@Controller
public class ViewProfilAppController {
    @Autowired
    private ProfilAppService profilAppService;

    @GetMapping("/profil-app/view/{id}")
    public String viewProfilApp(@PathVariable int id, Model model) {
        ProfilAppDTOResponse profilApp = profilAppService.getById(id);

        model.addAttribute("currentPage", "/profil-app");
        model.addAttribute("pageTitle", "Consultation du profil applicatif");
        model.addAttribute("pageHeader", "Consultation du profil applicatif : " + profilApp.getName());
        model.addAttribute("pageDescription", "Dernière modification : "
                + DateTimeUtil.formatDisplayDateTime(profilApp.getDateUpdated()));
        model.addAttribute("contentPage", "viewProfilApp.jsp");
        model.addAttribute("pageCss", "viewProfilApp");

        model.addAttribute("profilApp", profilApp);

        return "layout";
    }
}
