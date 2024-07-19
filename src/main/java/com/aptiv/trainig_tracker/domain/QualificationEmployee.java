package com.aptiv.trainig_tracker.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity(name = "qualification_employee")
@Data
public class QualificationEmployee {
    @EmbeddedId
    private QualificationEmployeeId id;
}
