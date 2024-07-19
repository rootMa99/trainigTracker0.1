package com.aptiv.trainig_tracker.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity(name = "qualification_employee")
@Data
public class QualificationEmployee {
    @EmbeddedId
    private QualificationEmployeeId id;

    @ManyToOne
    @MapsId("employeeId")
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @MapsId("qualificationId")
    @JoinColumn(name = "qualification_id")
    private Qualification qualification;

}
