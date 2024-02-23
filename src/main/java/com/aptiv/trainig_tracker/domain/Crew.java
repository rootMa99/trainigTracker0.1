package com.aptiv.trainig_tracker.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity(name = "crew")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Crew {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "crewName", nullable = false)
    private String crewName;

    @OneToMany(mappedBy = "crew", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Employee> employees;
}
