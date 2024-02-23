package com.aptiv.trainig_tracker.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity(name = "family")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Family {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "familyName", nullable = false)
    private String familyName;
    @OneToMany(mappedBy = "family", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Employee> employees;
}
