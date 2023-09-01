package com.springProjects.sudoku;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SudokuRepository extends CrudRepository<Sudoku, Integer>{
	
}
