package com.aptiv.trainig_tracker.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity(name = "qualification")
@Data
public class Qualification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "qualification")
    private List<QualificationEmployee> qualificationEmployees;

}
