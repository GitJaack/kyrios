package fr.cmp.kyrios.controller.frontend;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import fr.cmp.kyrios.model.Emploi.EmploiModel.Status;
import fr.cmp.kyrios.model.Emploi.dto.EmploiDTOResponse;
import fr.cmp.kyrios.model.common.IdNameDTO;
import fr.cmp.kyrios.service.EmploiService;
import fr.cmp.kyrios.service.FrontendReferenceDataService;
import fr.cmp.kyrios.util.DateTimeUtil;

@Controller
public class UpdateEmploiController {
    @Autowired
    private EmploiService emploiService;

    @Autowired
    private FrontendReferenceDataService frontendReferenceDataService;

    @GetMapping("/emploi/edit/{id}")
    public String updateEmploi(Model model, @PathVariable int id) {
        EmploiDTOResponse emploi = emploiService.getById(id);

        List<IdNameDTO> directions = frontendReferenceDataService.getDirections();
        List<IdNameDTO> services = frontendReferenceDataService.getServices();
        List<IdNameDTO> domaines = frontendReferenceDataService.getDomaines();
        List<IdNameDTO> profilsSI = frontendReferenceDataService.getProfilsSI();

        Integer selectedDirectionId = resolveIdByName(directions, emploi.getDirection());
        Integer selectedServiceId = resolveIdByName(services, emploi.getService());
        Integer selectedDomaineId = resolveIdByName(domaines, emploi.getDomaine());

        model.addAttribute("currentPage", "/emploi");
        model.addAttribute("pageTitle", "Modification de l'emploi");
        model.addAttribute("pageHeader", "Modification de l'emploi : " + emploi.getEmploi());
        model.addAttribute("pageDescription",
                "Dernière modification : " + DateTimeUtil.formatDisplayDateTime(emploi.getDateUpdated()));
        model.addAttribute("contentPage", "updateEmploi.jsp");
        model.addAttribute("pageCss", "form");

        model.addAttribute("directions", directions);
        model.addAttribute("services", services);
        model.addAttribute("domaines", domaines);
        model.addAttribute("profilsSI", profilsSI);
        model.addAttribute("statusOptions", Status.values());
        model.addAttribute("emploi", emploi);
        model.addAttribute("emploiId", id);
        model.addAttribute("selectedDirectionId", selectedDirectionId);
        model.addAttribute("selectedServiceId", selectedServiceId);
        model.addAttribute("selectedDomaineId", selectedDomaineId);

        return "layout";
    }

    private Integer resolveIdByName(List<IdNameDTO> options, String name) {
        if (name == null || name.isBlank()) {
            return null;
        }
        return options.stream()
                .filter(option -> name.equals(option.getName()))
                .map(IdNameDTO::getId)
                .findFirst()
                .orElse(null);
    }

}
