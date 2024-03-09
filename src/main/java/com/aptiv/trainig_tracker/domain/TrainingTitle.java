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
    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "Order_Training_Mapping", joinColumns = @JoinColumn(name = "training_title_id"),
            inverseJoinColumns = @JoinColumn(name = "order_id"))
    private List<OrderQualification> orderQualifications;

}
