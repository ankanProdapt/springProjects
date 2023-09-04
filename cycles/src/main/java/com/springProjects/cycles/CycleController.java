package com.springProjects.cycles;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/cycles")
public class CycleController {
    @Autowired
    private CycleRepository cycleRepository;

    @Autowired
    private BrandRepository brandRepository;

    @GetMapping("/list")
    public String listAll(Model model){
        model.addAttribute("brands", brandRepository.findAll());
        return "cycleStore";
    }

    @GetMapping("/restock/{id}")
    public String restock(@PathVariable int id, @RequestParam(name = "qty", defaultValue = "1") int qty){
        Brand brand = brandRepository.findById(id).get();
        for(int i = 0; i < qty; i++){
            Cycle cycle = new Cycle();
            cycle.setBrand(brand);
            cycle.setAvailable(true);
            cycleRepository.save(cycle);
        }
        brand.setStock(brand.getStock() + qty);
        brandRepository.save(brand);
        return "redirect:/cycles/list";
    }

    @GetMapping("/borrow/{id}")
    public String borrow(@PathVariable int id){
        Brand brand = brandRepository.findById(id).get();
        Optional<Cycle> c = cycleRepository.findOneAvailableCycle(id);
        if(c.isPresent()){
            Cycle cycle = c.get();
            cycle.setAvailable(false);
            brand.setStock(brand.getStock() - 1);
            cycleRepository.save(cycle);
            brandRepository.save(brand);
        }
        return "redirect:/cycles/list";
    }

}
