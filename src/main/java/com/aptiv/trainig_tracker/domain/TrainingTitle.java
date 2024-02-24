package com.aptiv.trainig_tracker.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "trainingTitle")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainingTitle{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "trainingTitleName", nullable = false)
    private String tTitleName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainingType_id")
    private TrainingType trainingType;
}
