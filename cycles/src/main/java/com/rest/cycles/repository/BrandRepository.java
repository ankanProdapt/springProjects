package com.rest.cycles.repository;


import org.springframework.data.repository.CrudRepository;

import com.rest.cycles.entity.Brand;

public interface BrandRepository extends CrudRepository<Brand, Integer> {
}
