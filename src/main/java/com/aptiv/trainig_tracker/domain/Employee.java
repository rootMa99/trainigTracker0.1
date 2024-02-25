package com.aptiv.trainig_tracker.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity(name = "employee")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    private Long matricule;
    @Column(name = "nom", nullable = false)
    private String nom;
    @Column(name = "prenom", nullable = false)
    private String prenom;
    @Column(name = "fonctionEntreprise", nullable = false)
    private String fonctionEntreprise;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_id")
    private Crew crew;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poste_id")
    private Poste poste;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="family_id")
    private Family family;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coordinator_id")
    private Coordinator coordinator;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shiftLeader_id")
    private ShiftLeader shiftLeader;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamLeader_id")
    private TeamLeader teamLeader;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "Training_Employee_Mapping", joinColumns = @JoinColumn(name = "matricule"),
    inverseJoinColumns = @JoinColumn(name = "training_id"))
    private List<Training> trainings;
}
