package fr.cmp.kyrios.controller.frontend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import fr.cmp.kyrios.model.Emploi.EmploiModel;
import fr.cmp.kyrios.repository.Emploi.DirectionRepository;
import fr.cmp.kyrios.repository.Emploi.DomaineRepository;
import fr.cmp.kyrios.repository.Emploi.ServiceRepository;
import fr.cmp.kyrios.repository.Si.ProfilSIRepository;
import fr.cmp.kyrios.service.EmploiService;
import fr.cmp.kyrios.util.DateTimeUtil;

@Controller
public class UpdateEmploiController {
    @Autowired
    private EmploiService emploiService;

    @Autowired
    private DirectionRepository directionRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private DomaineRepository domaineRepository;

    @Autowired
    private ProfilSIRepository profilSIRepository;

    @GetMapping("/emploi/edit/{id}")
    public String updateEmploi(Model model, @PathVariable int id) {
        EmploiModel emploi = emploiService.getById(id);

        model.addAttribute("currentPage", "/emploi");
        model.addAttribute("pageTitle", "Modification de l'emploi");
        model.addAttribute("pageHeader", "Modification de l'emploi : " + emploi.getEmploiName());
        model.addAttribute("pageDescription",
                "Dernière modification : " + DateTimeUtil.formatDisplayDateTime(emploi.getDateUpdated()));
        model.addAttribute("contentPage", "updateEmploi.jsp");
        model.addAttribute("pageCss", "form");

        model.addAttribute("directions", directionRepository.findAll());
        model.addAttribute("services", serviceRepository.findAll());
        model.addAttribute("domaines", domaineRepository.findAll());
        model.addAttribute("profilsSI", profilSIRepository.findAll());
        model.addAttribute("statusOptions", EmploiModel.Status.values());
        model.addAttribute("emploi", emploi);
        model.addAttribute("emploiId", id);

        return "layout";
    }

}
