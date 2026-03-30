package fr.cmp.kyrios.controller.frontend;

import fr.cmp.kyrios.model.Si.dto.profilSI.ProfilSIDTOResponse;
import fr.cmp.kyrios.service.ProfilSIService;
import fr.cmp.kyrios.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class HomeProfilSIController {

    @Autowired
    private ProfilSIService profilSIService;

    @GetMapping("/profilSI")
    public String profilSI(Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        model.addAttribute("currentPage", "/profilSI");
        model.addAttribute("pageTitle", "Gestion des profils SI");
        model.addAttribute("pageHeader", "Gestion des profils SI");
        model.addAttribute("pageDescription", "Gérer les profils système d'information");
        model.addAttribute("contentPage", "homeProfilSI.jsp");
        model.addAttribute("pageCss", "homeProfilSI");

        List<ProfilSIDTOResponse> allProfils = profilSIService.listAll();
        int totalItems = allProfils.size();
        int totalPages = totalItems == 0 ? 1 : (int) Math.ceil((double) totalItems / size);
        int safePage = Math.max(0, Math.min(page, totalPages - 1));
        int fromIndex = Math.min(safePage * size, totalItems);
        int toIndex = Math.min(fromIndex + size, totalItems);
        List<ProfilSIDTOResponse> profils = allProfils.subList(fromIndex, toIndex);

        Map<Integer, String> dateUpdatedById = new LinkedHashMap<>();
        for (ProfilSIDTOResponse profil : profils) {
            dateUpdatedById.put(profil.getIdProfilSI(), DateTimeUtil.formatDisplayDateTime(profil.getDateUpdated()));
        }

        model.addAttribute("profilsSI", profils);
        model.addAttribute("DateUpdatedById", dateUpdatedById);
        model.addAttribute("currentPageNumber", safePage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);

        return "layout";
    }
}
