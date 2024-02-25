package com.aptiv.trainig_tracker.services.serviceImpl;


import com.aptiv.trainig_tracker.models.TrainingDataFormatter;
import com.aptiv.trainig_tracker.models.TrainingFromExcel;
import com.aptiv.trainig_tracker.repositories.EmployeeRepo;
import com.aptiv.trainig_tracker.repositories.TrainingRepo;
import com.aptiv.trainig_tracker.repositories.TrainingTitleRepo;
import com.aptiv.trainig_tracker.repositories.TrainingTypeRepo;
import com.aptiv.trainig_tracker.services.TrainingService;
import com.aptiv.trainig_tracker.services.UploadEmployeeData;
import com.aptiv.trainig_tracker.ui.Utils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class TrainingServiceImpl implements TrainingService {
    Utils utils;
    EmployeeRepo employeeRepo;
    TrainingTypeRepo trainingTypeRepo;
    TrainingTitleRepo trainingTitleRepo;
    TrainingRepo trainingRepo;

    @Override
    public void saveTrainingDataToDb(MultipartFile file) throws IllegalAccessException {
        if (UploadEmployeeData.isValidFormat(file)) {
            try {
                List<TrainingFromExcel> trainingFromExcels =
                        UploadEmployeeData.getTrainingDataFromExcel(file.getInputStream());
                List<TrainingDataFormatter> trainingDataFormatters = formatData(trainingFromExcels);
                for (TrainingDataFormatter tf: trainingDataFormatters){
                    System.out.println(
                            tf.getTrainingId()+" / "+tf.getDdf()+" / "+tf.getDdb()+" / "+tf.getTrainingType()+" / "+
                                    tf.getTrainingTitle()
                    );
                    for (Long l:tf.getMatricules()){
                        System.out.println(l);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<TrainingDataFormatter> formatData(List<TrainingFromExcel> trainingFromExcels) {
        List<TrainingDataFormatter> trainingDataFormatters = new ArrayList<>();
        for (TrainingFromExcel tfe : trainingFromExcels) {
            if (trainingDataFormatters.isEmpty()) {
                TrainingDataFormatter tdf = new TrainingDataFormatter();
                tdf.setTrainingId(utils.getGeneratedId(22));
                tdf.setModalite(tfe.getModalite());
                tdf.setDph(tfe.getDph());
                tdf.setDdb(tfe.getDdb());
                tdf.setDdf(tfe.getDdf());
                tdf.setPrestataire(tfe.getPrestataire());
                tdf.setFormatteur(tfe.getFormatteur());
                tdf.setEva(tfe.isEva());
                tdf.setTrainingTitle(tfe.getTrainingTitle());
                tdf.setTrainingType(tfe.getTrainingType());
                List<Long> matricules = new ArrayList<>();
                matricules.add(tfe.getMatricule());
                tdf.setMatricules(matricules);
                trainingDataFormatters.add(tdf);
            } else {
                boolean flag=false;
                for (TrainingDataFormatter tf : trainingDataFormatters) {
                        if (tf.getTrainingTitle().equals(tfe.getTrainingTitle()) && tf.getTrainingType().equals(tfe.getTrainingType())
                                && tf.getDdb().equals(tfe.getDdb()) && tf.getDdf().equals(tfe.getDdf())) {
                            flag=true;
                            tf.getMatricules().add(tfe.getMatricule());
                        }
                }
                if (!flag){
                    TrainingDataFormatter tdf = new TrainingDataFormatter();
                    tdf.setTrainingId(utils.getGeneratedId(22));
                    tdf.setModalite(tfe.getModalite());
                    tdf.setDph(tfe.getDph());
                    tdf.setDdb(tfe.getDdb());
                    tdf.setDdf(tfe.getDdf());
                    tdf.setPrestataire(tfe.getPrestataire());
                    tdf.setFormatteur(tfe.getFormatteur());
                    tdf.setEva(tfe.isEva());
                    tdf.setTrainingTitle(tfe.getTrainingTitle());
                    tdf.setTrainingType(tfe.getTrainingType());
                    List<Long> matricules = new ArrayList<>();
                    matricules.add(tfe.getMatricule());
                    tdf.setMatricules(matricules);
                    trainingDataFormatters.add(tdf);
                }
            }
        }
        return trainingDataFormatters;
    }
}
