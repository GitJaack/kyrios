package fr.cmp.kyrios.controller.frontend;

import fr.cmp.kyrios.repository.Si.ProfilSIRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeProfilSIController {

    @Autowired
    private ProfilSIRepository profilSIRepository;

    @GetMapping("/profilSI")
    public String profilSI(Model model, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        model.addAttribute("currentPage", "/profilSI");
        model.addAttribute("pageTitle", "Gestion des profils SI - Kyrios");
        model.addAttribute("pageHeader", "Gestion des profils SI");
        model.addAttribute("pageDescription", "Gérer les profils système d'information");
        model.addAttribute("contentPage", "homeProfilSI.jsp");
        model.addAttribute("pageCss", "homeProfilSI");

        Page<fr.cmp.kyrios.model.Si.ProfilSIModel> profilPage = profilSIRepository.findAll(PageRequest.of(page, size));

        model.addAttribute("profilsSI", profilPage.getContent());
        model.addAttribute("currentPageNumber", page);
        model.addAttribute("totalPages", profilPage.getTotalPages());
        model.addAttribute("totalItems", profilPage.getTotalElements());

        return "layout";
    }
}
