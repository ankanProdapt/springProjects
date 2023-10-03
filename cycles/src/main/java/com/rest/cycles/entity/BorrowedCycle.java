package com.rest.cycles.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class BorrowedCycle {
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

    @ManyToOne
    @JoinColumn(name = "cycle_id", referencedColumnName = "id")
    private Cycle cycle;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    
    private Date borrowedAt;
    private Date returnedAt;
}
