package com.aptiv.trainig_tracker.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class OperationStatusResult {
    private String operationName;
    private String operationResult;
}
