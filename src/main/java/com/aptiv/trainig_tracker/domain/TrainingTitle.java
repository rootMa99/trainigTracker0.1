package com.aptiv.trainig_tracker.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity(name = "trainingTitle")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainingTitle{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "trainingTitleName", nullable = false)
    private String trainingTitleName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainingType_id")
    private TrainingType trainingType;
    @OneToMany(mappedBy = "trainingTitle", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Training> trainings;
}
