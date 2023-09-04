package com.springProjects.cycles;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Brand {
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

    @Column
    private String name;

    @Column
    private int stock;
}