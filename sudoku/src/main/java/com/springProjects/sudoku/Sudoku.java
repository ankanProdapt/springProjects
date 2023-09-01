package com.springProjects.sudoku;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Sudoku {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column
	private String gridData;
	
	public void setGridData(String s) {
		gridData = s;
	}
	
	public String getGridData() {
		return gridData;
	}
	
	@Override
	public String toString() {
		return String.valueOf(id);
	}
}
