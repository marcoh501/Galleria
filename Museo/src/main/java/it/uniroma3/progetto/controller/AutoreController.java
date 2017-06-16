package it.uniroma3.progetto.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.progetto.model.Autore;
import it.uniroma3.progetto.model.Quadro;
import it.uniroma3.progetto.service.AutoreService;
import it.uniroma3.progetto.service.QuadroService;

@Controller
public class AutoreController {
	
	@Autowired 
	private AutoreService autoreService;
	

	@Autowired 
	private QuadroService quadroService;
	
	
	@GetMapping(value = "/")
	public String homex(){
		return "redirect:/index.html";
	}

    @GetMapping("/autore")
    public String showForm(Autore autore) {
        return "formAutore";
    }
	
    @PostMapping("/autore")
    public String checkAutoreInfo(@Valid @ModelAttribute Autore autore, 
    									BindingResult bindingResult, Model model) {
    	
        if (bindingResult.hasErrors()) {
            return "formAutore";
        }
        else {
        	if(autore.getCognome()==autoreService.findByCognome(autore.getCognome()).getCognome())
        		return "formAutore";
        	else{
        	
        	
        	model.addAttribute(autore);
        	try{
        	autoreService.add(autore); 
        
        	}
        	catch(Exception e){
        		return "paginaErrore";
        	}
        	}
        	}
        return "confermaAutore";
    }                                      
	
    @GetMapping("/cancellaAutore")
    public String showFormRimuovi(Autore autore) {
        return "RimuoviAutore";
    }
    @PostMapping("/cancellaAutore")
    public String RimuoviAutore(@ModelAttribute("cognome") String cognome,BindingResult bindingResult,Model model){
       if (bindingResult.hasErrors()) {
            return "RimuoviAutore";
        }
       else {
    	    
    	Autore autoreEliminato=this.autoreService.findByCognome(cognome);
        List<Quadro> quadri=new ArrayList<Quadro>(autoreEliminato.getQuadri());
        for(Quadro q : quadri){
        	quadroService.delete(q);
        }
        model.addAttribute(autoreEliminato);
        this.autoreService.delete(autoreEliminato);
        
        }
        return "confermaRimozioneAutore";
    }

}
