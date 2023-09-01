package com.springProjects.cycles;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cycles")
public class CycleController {
	@Autowired
	private CycleRepository cycleRepository;
	
	@GetMapping("/showAllAvailableCycles")
	public String showAvailableCycles(Model model) {
		model.addAttribute("cycles", cycleRepository.findByAvailability(true));
		model.addAttribute("status", "borrow");
		return "cycles";
	}
	
	@GetMapping("/showAllBorrowedCycles")
	public String showAllBorrowedCycles(Model model) {
		model.addAttribute("cycles", cycleRepository.findByAvailability(false));
		model.addAttribute("status", "return");
		return "cycles";
	}
	
	@GetMapping("/borrow/{id}")
	public String borrowCycle(@PathVariable int id) {
		Optional<Cycle> cycle = cycleRepository.findById(id);
		if(cycle.isPresent()) {
			Cycle c = cycle.get();
			c.setAvailable(false);
			cycleRepository.save(c);
		}
		//cycleRepository.borrowCycle(id);
		return "redirect:/cycles/showAllAvailableCycles";
	}
	
	@GetMapping("/return/{id}")
	public String returnCycle(@PathVariable int id) {
		Optional<Cycle> cycle = cycleRepository.findById(id);
		if(cycle.isPresent()) {
			Cycle c = cycle.get();
			c.setAvailable(true);
			cycleRepository.save(c);
		}
		//cycleRepository.borrowCycle(id);
		return "redirect:/cycles/showAllBorrowedCycles";
	}
	
	@PostMapping("/add")
	public String addCycle(String brand, String color, String availability) {
		Cycle cycle = new Cycle();
		cycle.setBrand(brand);
		cycle.setColor(color);
		cycle.setAvailable(availability.equals("true") ? true : false);
		cycleRepository.save(cycle);
		return "redirect:/cycles/showAllAvailableCycles";
	}
	
}
