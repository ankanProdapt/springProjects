package com.rest.cycles.controller;

import java.security.Principal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.rest.cycles.entity.Brand;
import com.rest.cycles.entity.Cycle;
import com.rest.cycles.entity.User;
import com.rest.cycles.repository.BrandRepository;
import com.rest.cycles.repository.CycleRepository;
import com.rest.cycles.repository.UserRepository;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class CycleRestController {
    
    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CycleRepository cycleRepository;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        try {
            if (userRepository.existsByName(user.getName())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed: " + e.getMessage());
        }
    }

    @GetMapping("/health")
    public String checkhealth() {
        return "healthy";
    }

    @GetMapping("/cycle/list")
    public List<Brand> all(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        System.out.println(jwt.getClaimAsString("scope"));
        return (List<Brand>)brandRepository.findAll();
    }

    @PostMapping("/borrow/{id}")
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

    @PostMapping("/restock/{id}")
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
}