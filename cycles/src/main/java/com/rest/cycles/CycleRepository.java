package com.rest.cycles;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CycleRepository extends CrudRepository<Cycle, Integer>{
    @Query(value = "SELECT * FROM cycle WHERE brand_id = ?1 AND is_available = true LIMIT 1", nativeQuery = true)
    Optional<Cycle> findOneAvailableCycle(int brand_id);
}
