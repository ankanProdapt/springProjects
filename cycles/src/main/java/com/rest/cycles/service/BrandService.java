package com.rest.cycles.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rest.cycles.dto.BrandDTO;
import com.rest.cycles.entity.Brand;
import com.rest.cycles.repository.CycleRepository;

@Service
public class BrandService {
    @Autowired
    private CycleRepository cycleRepository;

    public BrandDTO convert(Brand brand) {
        BrandDTO b = new BrandDTO();
        b.setId(brand.getId());
        b.setName(brand.getName());
        b.setStock(cycleRepository.countAvailableByBrandId(brand.getId()));
        return b;
    }

    public List<BrandDTO> convert(List<Brand> brands) {
        List<BrandDTO> b = new ArrayList<>();
        for(Brand brand: brands){
            b.add(convert(brand));
        }
        return b;
    }
}
