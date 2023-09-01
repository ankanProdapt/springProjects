package com.springProjects.cycles;

import java.util.List;

import org.hibernate.annotations.SQLUpdate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CycleRepository extends CrudRepository<Cycle, Integer>{
	@Query(value="SELECT * FROM cycle WHERE is_available = ?1", nativeQuery = true)
	List<Cycle> findByAvailability(boolean isAvailable);
}
