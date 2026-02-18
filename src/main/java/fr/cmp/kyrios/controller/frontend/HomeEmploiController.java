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

import fr.cmp.kyrios.model.Emploi.EmploiModel;
import fr.cmp.kyrios.repository.Emploi.EmploiRepository;
import fr.cmp.kyrios.util.DateTimeUtil;

@Controller
public class HomeEmploiController {
    @Autowired
    private EmploiRepository emploiRepository;

    @GetMapping("/emploi")
    public String homeEmploi(Model model, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size) {
        model.addAttribute("currentPage", "/emploi");
        model.addAttribute("pageTitle", "Gestion des emplois");
        model.addAttribute("pageHeader", "Gestion des emplois");
        model.addAttribute("pageDescription", "Gérer les emplois et leurs profils SI associés");
        model.addAttribute("contentPage", "homeEmploi.jsp");
        model.addAttribute("pageCss", "homeEmploi");

        Page<EmploiModel> emplois = emploiRepository.findAll(PageRequest.of(page, size));

        Map<Integer, String> dateUpdatedById = new LinkedHashMap<>();
        for (EmploiModel emploi : emplois.getContent()) {
            dateUpdatedById.put(emploi.getId(), DateTimeUtil.formatDisplayDateTime(emploi.getDateUpdated()));
        }

        model.addAttribute("emplois", emplois.getContent());
        model.addAttribute("dateUpdatedById", dateUpdatedById);
        model.addAttribute("currentPageNumber", page);
        model.addAttribute("totalPages", emplois.getTotalPages());
        model.addAttribute("totalItems", emplois.getTotalElements());

        return "layout";
    }
}
