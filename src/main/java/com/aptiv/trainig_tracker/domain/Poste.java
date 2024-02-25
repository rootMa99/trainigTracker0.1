package com.aptiv.trainig_tracker.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity(name = "poste")
@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
public class Poste {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "posteName")
    private String posteName;
    @OneToMany(mappedBy = "poste", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Employee> employees;
}
