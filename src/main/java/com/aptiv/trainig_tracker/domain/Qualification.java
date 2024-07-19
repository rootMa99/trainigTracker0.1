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
    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "Qualification_Employee_Mapping",joinColumns = @JoinColumn(name = "qualification_id"),
            inverseJoinColumns = @JoinColumn(name = "matricule")
    )
    private List<Employee> employees;
}
