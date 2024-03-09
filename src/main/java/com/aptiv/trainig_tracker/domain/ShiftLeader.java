package com.aptiv.trainig_tracker.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity(name = "shift_leader")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShiftLeader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "shiftLeader", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Employee> employees;
    @OneToMany(mappedBy = "shiftLeader", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<OrderQualification> orderQualifications;
}
