package com.rest.cycles.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.rest.cycles.entity.Cycle;

public interface CycleRepository extends CrudRepository<Cycle, Integer>{
    @Query(value = "SELECT * FROM cycle WHERE brand_id = ?1 AND is_available = true LIMIT 1", nativeQuery = true)
    Optional<Cycle> findOneAvailableCycle(int brand_id);

    @Query(value = "SELECT * FROM cycle WHERE is_available = false", nativeQuery = true)
    List<Cycle> findAllBorrowedCycles();

    @Query(value = "SELECT * FROM cycle WHERE is_available = true", nativeQuery = true)
    List<Cycle> findAllAvailableCycles();

    @Query(value = "SELECT COUNT(*) FROM cycle WHERE is_available = true AND brand_id = ?1", nativeQuery = true)
    int countAvailableByBrandId(int id);
}
