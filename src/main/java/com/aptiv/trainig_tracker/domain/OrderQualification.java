package com.aptiv.trainig_tracker.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;


@Entity(name = "order_qualification")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class OrderQualification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String shift;
    @Temporal(TemporalType.DATE)
    @Column(name = "order_date")
    private Date orderDate;
    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "Order_Employee_Mapping",joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "matricule")
    )
    private List<Employee> employees;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "Order_Training_Mapping",joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "training_title_id")
    )
    private List<TrainingTitle> trainingTitles;
}
