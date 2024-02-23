package com.aptiv.trainig_tracker.domain;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

public class Category {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "categoryName")
    private String categoryName;
}
