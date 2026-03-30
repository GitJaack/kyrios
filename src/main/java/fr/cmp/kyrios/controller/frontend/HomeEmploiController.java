package fr.cmp.kyrios.controller.frontend;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fr.cmp.kyrios.model.Emploi.dto.EmploiDTOResponse;
import fr.cmp.kyrios.service.EmploiService;
import fr.cmp.kyrios.util.DateTimeUtil;

@Controller
public class HomeEmploiController {
    @Autowired
    private EmploiService emploiService;

    @GetMapping("/emploi")
    public String homeEmploi(Model model, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size) {
        model.addAttribute("currentPage", "/emploi");
        model.addAttribute("pageTitle", "Gestion des emplois");
        model.addAttribute("pageHeader", "Gestion des emplois");
        model.addAttribute("pageDescription", "Gérer les emplois et leurs profils SI associés");
        model.addAttribute("contentPage", "homeEmploi.jsp");
        model.addAttribute("pageCss", "homeEmploi");

        List<EmploiDTOResponse> allEmplois = emploiService.listAll();
        int totalItems = allEmplois.size();
        int totalPages = totalItems == 0 ? 1 : (int) Math.ceil((double) totalItems / size);
        int safePage = Math.max(0, Math.min(page, totalPages - 1));
        int fromIndex = Math.min(safePage * size, totalItems);
        int toIndex = Math.min(fromIndex + size, totalItems);
        List<EmploiDTOResponse> emplois = allEmplois.subList(fromIndex, toIndex);

        Map<Integer, String> dateUpdatedById = new LinkedHashMap<>();
        for (EmploiDTOResponse emploi : emplois) {
            dateUpdatedById.put(emploi.getId(), DateTimeUtil.formatDisplayDateTime(emploi.getDateUpdated()));
        }

        model.addAttribute("emplois", emplois);
        model.addAttribute("dateUpdatedById", dateUpdatedById);
        model.addAttribute("currentPageNumber", safePage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);

        return "layout";
    }
}
