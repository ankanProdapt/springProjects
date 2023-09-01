package com.springProjects.sudoku;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/sudoku")
public class SudokuController {
	
	@Autowired
	private SudokuRepository sudokuRepository;
	
	private SudokuUtil sudoku;
	
	@GetMapping("/create")
	public String createSudoku(Model model) {
		return "createSudoku";
	}
	
	@GetMapping("/showAll")
	public String showAll(Model model) {
		model.addAttribute("sudokuList",sudokuRepository.findAll());
		return "showAll";
	}
	
	@GetMapping("/solve/{id}")
	public String solveSudoku(@PathVariable int id, Model model) {
		Optional<Sudoku> s = sudokuRepository.findById(id);
		if(s.isPresent()) {
			Sudoku sudoku = s.get();
			String gridData = sudoku.getGridData();
			model.addAttribute("gridData", gridData);
			return "solveSudoku";
		}
		else {
			return "invalid";
		}
	}
	
	@PostMapping("/validate")
	public String validateSudoku(HttpServletRequest req) {
		sudoku = new SudokuUtil(req);
		if(sudoku.isValid() && sudoku.isFilled()) {
			return "valid";
		}
		else {
			return "invalid";
		}
	}
	
	@PostMapping("/save")
	public String saveSudoku(HttpServletRequest req) {
		sudoku = new SudokuUtil(req);
		if(sudoku.isValid()) {
			Sudoku s = new Sudoku();
			s.setGridData(sudoku.toString());
			System.out.println(sudoku.toString());
			sudokuRepository.save(s);
			return "redirect:/sudoku/showAll";
		}
		else {
			System.out.println("Invalid Sudoku");
			return "invalid";
		}
	}
}
