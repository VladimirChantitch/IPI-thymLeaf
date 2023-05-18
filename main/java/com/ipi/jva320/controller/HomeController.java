package com.ipi.jva320.controller;

import com.ipi.jva320.exception.SalarieException;
import com.ipi.jva320.model.SalarieAideADomicile;
import com.ipi.jva320.service.SalarieAideADomicileService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Controller
public class HomeController {

    @Autowired
    private SalarieAideADomicileService salarieAideADomicileService;


    @GetMapping(value = "/")
    public String home(ModelMap model) {
        model.put("salarieCount", salarieAideADomicileService.countSalaries());
        return "home";
    }

    @GetMapping(value = "/salaries/{id}")
    public String salarie(ModelMap model, @PathVariable Long id) {
        model.put("salarie", salarieAideADomicileService.getSalarie(id));
        return "detail_Salarie";    
    }

    @GetMapping(value = "/salaries/aide/new")
    public String newSalarie(ModelMap model) {
        return "detail_Salarie";
    }

    @PostMapping(value = "/salaries/save")
    public String createSalarie(ModelMap model, SalarieAideADomicile salarie) throws SalarieException {
        try{
            salarieAideADomicileService.creerSalarieAideADomicile(salarie);
        }catch(Exception e){
            model.put("fieldError", true);
        }
        return "redirect:/salaries/" + salarie.getId();
    }

    @PostMapping(value = "/salaries/{id}")
    public String updateSalarie(SalarieAideADomicile salarie) throws SalarieException {
        salarieAideADomicileService.updateSalarieAideADomicile(salarie);
        return "redirect:/salaries/" + salarie.getId();
    }

    @GetMapping(value = "/salaries/{id}/delete")
    public String deleteSalarie(@PathVariable Long id, ModelMap model) throws SalarieException {
        try{
            salarieAideADomicileService.deleteSalarieAideADomicile(id);
        }catch(Exception e){
            model.put("typeError", true);
        }
        return "redirect:/salaries";
    }

    @GetMapping("/salaries")
    public String getSalaries(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              ModelMap model) {
        Page<SalarieAideADomicile> salariePage = salarieAideADomicileService.getSalaries(PageRequest.of(page, size));
        
        model.put("salaries", salariePage.getContent());
        model.put("currentPage", page);
        model.put("totalPages", salariePage.getTotalPages());
        model.put("pageSize", size);
        model.put("sortProperty", "nom"); 
        model.put("sortDirection", "ASC");
        
        return "list";
    }

    @PostMapping(value = "/salaries")
    public String searchSalaries(@RequestParam("matricule") String matricule, ModelMap model)  {
        SalarieAideADomicile salarieAideADomicile = salarieAideADomicileService.getSalarie(matricule);
        if (salarieAideADomicile != null){
            return (new StringBuilder("redirect:/salaries/").append(salarieAideADomicile.getId())).toString();
        }
        return"/home";
    }

}
