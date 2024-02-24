package com.aptiv.trainig_tracker.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity(name = "trainingType")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainingType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name ="trainingTypeName", nullable = false)
    private String ttName;
    @OneToMany(mappedBy = "trainingType", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<TrainingTitle> trainingTitles;
    @OneToMany(mappedBy = "trainingType", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Training> trainings;
}
