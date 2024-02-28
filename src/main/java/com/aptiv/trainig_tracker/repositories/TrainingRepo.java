package com.aptiv.trainig_tracker.repositories;

import com.aptiv.trainig_tracker.domain.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TrainingRepo extends JpaRepository<Training, Long> {
    List<Training> findAllByDateDebutBetween(Date startDate, Date endDate);
    Training findByTrainingId(String trainingId);
    Training findByTrainingTypeTtNameAndTrainingTitleTrainingTitleNameAndDateDebutBetween(String trainingType,
                                                                                          String trainingTitle,
                                                                                          Date startDate, Date endDate);
    List<Training> findAllTrainingId(String trainingId);
}
