package fr.cmp.kyrios.controller.frontend;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fr.cmp.kyrios.model.App.ProfilAppModel;
import fr.cmp.kyrios.repository.App.AppRepository;
import fr.cmp.kyrios.repository.App.ProfilAppRepository;
import fr.cmp.kyrios.util.DateTimeUtil;

@Controller
public class HomeProfilAppController {
    @Autowired
    private AppRepository appRepository;

    @Autowired
    private ProfilAppRepository profilAppRepository;

    @GetMapping("/profil-app")
    public String homeProfilApp(Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {
        model.addAttribute("currentPage", "/profil-app");
        model.addAttribute("pageTitle", "Gestion des profils applicatifs");
        model.addAttribute("pageHeader", "Gestion des profils applicatifs");
        model.addAttribute("contentPage", "homeProfilApp.jsp");
        model.addAttribute("pageCss", "homeProfilApp");

        model.addAttribute("apps", appRepository.findAll());

        Page<ProfilAppModel> profilApps = profilAppRepository.findAll(PageRequest.of(page, size));

        Map<Integer, String> dateUpdatedById = new LinkedHashMap<>();
        for (ProfilAppModel profilApp : profilApps.getContent()) {
            dateUpdatedById.put(profilApp.getId(), DateTimeUtil.formatDisplayDateTime(profilApp.getDateUpdated()));
        }

        model.addAttribute("profilApps", profilApps.getContent());
        model.addAttribute("dateUpdatedById", dateUpdatedById);
        model.addAttribute("currentPageNumber", page);
        model.addAttribute("totalPages", profilApps.getTotalPages());
        model.addAttribute("totalItems", profilApps.getTotalElements());

        return "layout";
    }

}
