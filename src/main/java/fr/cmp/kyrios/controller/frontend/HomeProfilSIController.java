package fr.cmp.kyrios.controller.frontend;

import fr.cmp.kyrios.model.Si.ProfilSIModel;
import fr.cmp.kyrios.repository.Si.ProfilSIRepository;
import fr.cmp.kyrios.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class HomeProfilSIController {

    @Autowired
    private ProfilSIRepository profilSIRepository;

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

        Page<ProfilSIModel> profilPage = profilSIRepository.findAll(PageRequest.of(page, size));

        Map<Integer, String> dateUpdatedById = new LinkedHashMap<>();
        for (ProfilSIModel profil : profilPage.getContent()) {
            dateUpdatedById.put(profil.getId(), DateTimeUtil.formatDisplayDateTime(profil.getDateUpdated()));
        }

        model.addAttribute("profilsSI", profilPage.getContent());
        model.addAttribute("DateUpdatedById", dateUpdatedById);
        model.addAttribute("currentPageNumber", page);
        model.addAttribute("totalPages", profilPage.getTotalPages());
        model.addAttribute("totalItems", profilPage.getTotalElements());

        return "layout";
    }
}
