package com.aptiv.trainig_tracker.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @Column(name = "categorie", nullable = false)
    private String categorie;
    @Column(name = "fonctionEntreprise", nullable = false)
    private String fonctionEntreprise;
}
