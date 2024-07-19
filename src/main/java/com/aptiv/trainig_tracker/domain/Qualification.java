package com.aptiv.trainig_tracker.domain;

import jakarta.persistence.*;
import lombok.Data;


@Entity(name = "qualification")
@Data
public class Qualification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

}
