package com.rest.cycles;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cycles")
@CrossOrigin
public class CycleController {
    @Autowired
    private CycleRepository cycleRepository;

    @Autowired
    private BrandRepository brandRepository;

    @GetMapping("/list")
    @ResponseBody
    public List<Brand> listAll(){
        return (List<Brand>)brandRepository.findAll();
    }

    @GetMapping("/restock/{id}")
    @ResponseBody
    public List<Brand> restock(@PathVariable int id, @RequestParam(name = "qty", defaultValue = "1") int qty){
        Brand brand = brandRepository.findById(id).get();
        for(int i = 0; i < qty; i++){
            Cycle cycle = new Cycle();
            cycle.setBrand(brand);
            cycle.setAvailable(true);
            cycleRepository.save(cycle);
        }
        brand.setStock(brand.getStock() + qty);
        brandRepository.save(brand);
        return (List<Brand>)brandRepository.findAll();
    }

    @GetMapping("/borrow/{id}")
    @ResponseBody
    public List<Brand> borrow(@PathVariable int id){
        Brand brand = brandRepository.findById(id).get();
        Optional<Cycle> c = cycleRepository.findOneAvailableCycle(id);
        if(c.isPresent()){
            Cycle cycle = c.get();
            cycle.setAvailable(false);
            brand.setStock(brand.getStock() - 1);
            cycleRepository.save(cycle);
            brandRepository.save(brand);
        }
        return (List<Brand>)brandRepository.findAll();
    }

}
