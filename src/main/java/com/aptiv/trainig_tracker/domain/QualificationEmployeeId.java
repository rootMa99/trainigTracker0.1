package com.aptiv.trainig_tracker.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;


@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
public class QualificationEmployeeId implements Serializable {
    @Column(name = "matricule")
    private Long matricule;
    @Column(name = "qualification_id")
    private Long qualificationId;
}
