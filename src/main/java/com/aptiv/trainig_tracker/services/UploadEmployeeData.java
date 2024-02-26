package com.aptiv.trainig_tracker.services;


import com.aptiv.trainig_tracker.models.DataExcelEmployee;
import com.aptiv.trainig_tracker.models.TrainingFromExcel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static org.apache.poi.ss.usermodel.CellType.BLANK;

@Service
public class UploadEmployeeData {
    public static boolean isValidFormat(MultipartFile file) {
        return Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    public static List<DataExcelEmployee> getEmployeesDataFromExcel(InputStream is) {
        List<DataExcelEmployee> dataExcelEmployees = new ArrayList<>();
        boolean done = false;
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(is);
            XSSFSheet sheet = workbook.getSheet("GLOBAL");
            int rowIndex = 0;
            for (Row row : sheet) {
                if (rowIndex == 0) {
                    rowIndex++;
                    continue;
                }
                if (done) {
                    break;
                }
                Iterator<Cell> cellIterator = row.iterator();
                int cellIndex = 0;
                DataExcelEmployee dataExcelEmployee = new DataExcelEmployee();
                while (cellIterator.hasNext() && !done) {
                    Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    switch (cellIndex) {
                        case 0 -> {
                            if (cell.getCellType() == BLANK) {
                                done = true;
                            }
                            dataExcelEmployee.setMatricule((long) cell.getNumericCellValue());
                            System.out.println(cell.getNumericCellValue());
                        }
                        case 1 -> {
                            if (cell.getCellType() == CellType.STRING) {
                                dataExcelEmployee.setNom(cell.getStringCellValue());
                            }
                        }
                        case 2 -> {
                            if (cell.getCellType() == CellType.STRING) {
                                dataExcelEmployee.setPrenom(cell.getStringCellValue());
                            }
                        }
                        case 3 -> {
                            if (cell.getCellType() == CellType.STRING) {
                                dataExcelEmployee.setCategory(cell.getStringCellValue());
                            }
                        }
                        case 4 -> {
                            if (cell.getCellType() == CellType.STRING) {
                                dataExcelEmployee.setFonction(cell.getStringCellValue());
                            }
                        }
                        case 5 -> {
                            if (cell.getCellType() == CellType.STRING) {
                                dataExcelEmployee.setDepartment(cell.getStringCellValue());
                            }
                        }
                        case 6 -> {
                            if (cell.getCellType() == CellType.STRING && cell.getCellType() != CellType.BLANK) {
                                dataExcelEmployee.setPoste(cell.getStringCellValue());
                            }
                        }
                        case 7 -> {
                            if (cell.getCellType() == CellType.STRING && cell.getCellType() != CellType.BLANK) {
                                dataExcelEmployee.setCrew(cell.getStringCellValue());
                            }
                        }
                        case 8 -> {
                            if (cell.getCellType() == CellType.STRING && cell.getCellType() != CellType.BLANK) {
                                dataExcelEmployee.setFamily(cell.getStringCellValue());
                            }
                        }
                        case 9 -> {
                            if (cell.getCellType() == CellType.STRING && cell.getCellType() != CellType.BLANK) {
                                dataExcelEmployee.setProject(cell.getStringCellValue());
                            }
                        }
                        case 10 -> {
                            if (cell.getCellType() == CellType.STRING && cell.getCellType() != CellType.BLANK) {
                                dataExcelEmployee.setCoordinator(cell.getStringCellValue());
                            }
                        }
                        case 11 -> {
                            if (cell.getCellType() == CellType.STRING && cell.getCellType() != CellType.BLANK) {
                                dataExcelEmployee.setShiftLeader(cell.getStringCellValue());
                            }
                        }
                        case 12 -> {
                            if (cell.getCellType() == CellType.STRING && cell.getCellType() != CellType.BLANK) {
                                dataExcelEmployee.setTeamLeader(cell.getStringCellValue());
                            }
                        }
                        default -> {

                        }
                    }
                    cellIndex++;
                    if (cellIndex == 13) {
                        rowIndex++;
                        break;
                    }
                }
                rowIndex++;
                dataExcelEmployees.add(dataExcelEmployee);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return dataExcelEmployees;
    }

    public static List<TrainingFromExcel> getTrainingDataFromExcel(InputStream is) {
        List<TrainingFromExcel> tfeList = new ArrayList<>();
        boolean done = false;
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(is);
            XSSFSheet sheet = workbook.getSheet("Déploiement");
            int rowIndex = 0;
            for (Row row : sheet) {
                if (rowIndex < 2) {
                    rowIndex++;
                    continue;
                }
                if (done) {
                    break;
                }
                Iterator<Cell> cellIterator = row.iterator();
                int cellIndex = 0;
                TrainingFromExcel trainingFromExcel = new TrainingFromExcel();
                while (cellIterator.hasNext() && !done) {
                    Cell cell = cellIterator.next();
                    switch (cellIndex) {
                        case 0 -> {
                            if (cell.getCellType() == BLANK) {
                                done = true;
                            }
                            if (cell.getCellType() == CellType.NUMERIC) {
                                trainingFromExcel.setMatricule((long) cell.getNumericCellValue());

                            } else {
                                trainingFromExcel.setMatricule(0L);
                            }

                        }
                        case 3 -> {
                            if (cell.getCellType() != BLANK) {
                                trainingFromExcel.setTrainingTitle(cell.getStringCellValue());
                            }
                        }
                        case 5 -> {
                            if (cell.getCellType() != BLANK) {
                                trainingFromExcel.setTrainingType(cell.getStringCellValue());
                            }
                        }
                        case 6 -> {
                            if (cell.getCellType() != BLANK) {
                                trainingFromExcel.setModalite(cell.getStringCellValue());
                            }
                        }
                        case 7 -> {
                            if (cell.getCellType() != BLANK) {
                                trainingFromExcel.setDph(cell.getNumericCellValue());
                            }
                        }
                        case 8 -> {
                            if (cell.getCellType() != BLANK) {
                                trainingFromExcel.setDdb(cell.getDateCellValue());
                            }
                        }
                        case 9 -> {
                            if (cell.getCellType() != BLANK) {
                                trainingFromExcel.setDdf(cell.getDateCellValue());
                            }
                        }
                        case 11 -> {
                            if (cell.getCellType() != BLANK) {
                                trainingFromExcel.setPrestataire(cell.getStringCellValue());
                            }
                        }
                        case 12 -> {
                            if (cell.getCellType() != BLANK) {
                                trainingFromExcel.setFormatteur(cell.getStringCellValue());
                            }
                        }
                        case 13 -> {
                            if (cell.getCellType() == CellType.BOOLEAN) {
                                trainingFromExcel.setEva(cell.getBooleanCellValue());
                            } else {
                                trainingFromExcel.setEva(cell.getStringCellValue().equals("oui")
                                        || cell.getStringCellValue().equals("Oui"));
                            }
                        }
                        default -> {

                        }
                    }
                    cellIndex++;
                }
                tfeList.add(trainingFromExcel);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return tfeList;
    }
}
