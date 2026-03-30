package fr.cmp.kyrios.controller.frontend;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fr.cmp.kyrios.model.App.dto.AppDTOResponse;
import fr.cmp.kyrios.model.App.dto.ProfilAppDTOResponse;
import fr.cmp.kyrios.service.AppService;
import fr.cmp.kyrios.service.ProfilAppService;
import fr.cmp.kyrios.util.DateTimeUtil;

@Controller
public class HomeProfilAppController {
    @Autowired
    private AppService appService;

    @Autowired
    private ProfilAppService profilAppService;

    @GetMapping("/profil-app")
    public String homeProfilApp(Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {
        model.addAttribute("currentPage", "/profil-app");
        model.addAttribute("pageTitle", "Gestion des profils applicatifs");
        model.addAttribute("pageHeader", "Gestion des profils applicatifs");
        model.addAttribute("contentPage", "homeProfilApp.jsp");
        model.addAttribute("pageCss", "homeProfilApp");

        List<AppDTOResponse> apps = appService.listAll();
        model.addAttribute("apps", apps);

        List<ProfilAppDTOResponse> allProfilApps = profilAppService.listAll();
        int totalItems = allProfilApps.size();
        int totalPages = totalItems == 0 ? 1 : (int) Math.ceil((double) totalItems / size);
        int safePage = Math.max(0, Math.min(page, totalPages - 1));
        int fromIndex = Math.min(safePage * size, totalItems);
        int toIndex = Math.min(fromIndex + size, totalItems);
        List<ProfilAppDTOResponse> profilApps = allProfilApps.subList(fromIndex, toIndex);

        Map<Integer, String> dateUpdatedById = new LinkedHashMap<>();
        for (ProfilAppDTOResponse profilApp : profilApps) {
            dateUpdatedById.put(profilApp.getId(), DateTimeUtil.formatDisplayDateTime(profilApp.getDateUpdated()));
        }

        model.addAttribute("profilApps", profilApps);
        model.addAttribute("dateUpdatedById", dateUpdatedById);
        model.addAttribute("currentPageNumber", safePage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);

        return "layout";
    }

}
