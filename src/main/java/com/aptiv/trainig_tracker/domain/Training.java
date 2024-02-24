package com.aptiv.trainig_tracker.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;


@Entity(name="training")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "trainingId", nullable = false)
    private String trainingId;
    @Column(name = "modalite", nullable = false)
    private String modalite;
    @Column(name = "dureeparheure")
    private double dureeParHeure;
    @Temporal(TemporalType.DATE)
    @Column(name = "dateDebut")
    private Date dateDebut;
    @Temporal(TemporalType.DATE)
    @Column(name = "datefin")
    private Date dateFin;
    @Column(name = "prestataire")
    private String prestataire;
    @Column(name = "formatteur")
    private String formatteur;
    @Column(name = "eva")
    private boolean eva;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainingType_id")
    private TrainingType trainingType;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "Training_Employee_Mapping",joinColumns = @JoinColumn(name = "training_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id")
            )
    private List<Employee> employees;

}
