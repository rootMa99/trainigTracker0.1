package com.aptiv.trainig_tracker.services.serviceImpl;

import com.aptiv.trainig_tracker.domain.Employee;
import com.aptiv.trainig_tracker.domain.OrderQualification;
import com.aptiv.trainig_tracker.domain.ShiftLeader;
import com.aptiv.trainig_tracker.domain.Training;
import com.aptiv.trainig_tracker.models.*;
import com.aptiv.trainig_tracker.repositories.*;
import com.aptiv.trainig_tracker.services.OtherService;
import com.aptiv.trainig_tracker.ui.Utils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class OtherServiceImpl implements OtherService {
    //CategoryRepo categoryRepo;
    //CoordinatorRepo coordinatorRepo;
    //CrewRepo crewRepo;
    //DepartmentRepo departmentRepo;
    //FamilyRepo familyRepo;
    //PosteRepo posteRepo;
    //TeamLeaderRepo teamLeaderRepo;
    //TrainingTypeRepo trainingTypeRepo;
    //TrainingRepo trainingRepo;
    Utils utils;
    EmployeeRepo employeeRepo;
    ShiftLeaderRepo shiftLeaderRepo;
    TrainingTitleRepo trainingTitleRepo;
    OrderRepo orderRepo;

    @Override
    public StatusRest saveOrderToDb(List<OrderDto> orderDtoList) {
        List<OrderQualification> orderQualifications = new ArrayList<>();
        StatusRest statusRest = new StatusRest();
        List<Long> notFound = new ArrayList<>();
        List<QualificationRest> qualificationRests = new ArrayList<>();
        for (OrderDto o : orderDtoList) {
            QualificationRest qr = new QualificationRest();
            List<EmployeeRest> employeeRests = new ArrayList<>();
            OrderQualification orderQualification = new OrderQualification();
            orderQualification.setOrderId(utils.getGeneratedId(22));
            orderQualification.setShift(o.getShift());
            orderQualification.setShiftLeader(shiftLeaderRepo.findByName(o.getShiftLeaderName()));
            orderQualification.setTrainingTitle(trainingTitleRepo.findByTrainingTitleName(o.getQualification()));
            orderQualification.setOrderDate(o.getOrderdate());
            orderQualification.setOrderDateSubmit(new Date());
            List<Employee> employees = new ArrayList<>();
            for (Long l : o.getMatricules()) {
                Employee e = employeeRepo.findByMatricule(l);
                if (e == null) {
                    notFound.add(l);
                } else {
                    employees.add(e);
                    employeeRests.add(getEmployeeRest(e));
                }
            }
            orderQualification.setEmployees(employees);
            orderQualifications.add(orderQualification);
            qr.setQualificationId(orderQualification.getOrderId());
            qr.setEmployeeRests(employeeRests);
            qr.setQualification(o.getQualification());
            qr.setQualificationDate(o.getOrderdate());
            qualificationRests.add(qr);
        }
        orderRepo.saveAll(orderQualifications);
        statusRest.setNotFound(notFound);
        statusRest.setQualificationRests(qualificationRests);
        return statusRest;
    }

    private static EmployeeRest getEmployeeRest(Employee e) {
        EmployeeRest employeeRest = new EmployeeRest();
        employeeRest.setMatricule(e.getMatricule());
        employeeRest.setNom(e.getNom());
        employeeRest.setPrenom(e.getPrenom());
        employeeRest.setCategory(e.getCategory().getCategoryName());
        employeeRest.setFonction(e.getFonctionEntreprise());
        employeeRest.setDepartment(e.getDepartment().getDepartmentName());
        employeeRest.setPoste(e.getPoste().getPosteName());
        employeeRest.setCrew(e.getCrew().getCrewName());
        employeeRest.setFamily(e.getFamily().getFamilyName());
        employeeRest.setCoordinator(e.getCoordinator().getName());
        employeeRest.setShiftLeader(e.getShiftLeader().getName());
        employeeRest.setTeamLeader(e.getTeamLeader().getName());
        return employeeRest;
    }

    @Override
    public OrderRest getOderById(String orderId) {
        OrderQualification order = orderRepo.findByOrderId(orderId);
        return getOrderRest(order);
    }

    private static OrderRest getOrderRest(OrderQualification order) {
        OrderRest orderRest = new OrderRest();
        orderRest.setQualificationId(order.getOrderId());
        orderRest.setQualification(order.getTrainingTitle().getTrainingTitleName());
        orderRest.setShiftLeader(order.getShiftLeader().getName());
        orderRest.setQualificationDate(order.getOrderDate());
        orderRest.setSubmitDate(order.getOrderDateSubmit());
        orderRest.setShift(order.getShift());
        List<EmployeeRest> ers = new ArrayList<>();
        for (Employee e : order.getEmployees()) {
            ers.add(getEmployeeRest(e));
        }
        orderRest.setEmployeeRests(ers);
        return orderRest;
    }

    @Override
    public List<OrderRest> getAllOrderBySl(String slName) {
        ShiftLeader shiftLeader = shiftLeaderRepo.findByName(slName);
        List<OrderRest> orderRests = new ArrayList<>();
        if (!shiftLeader.getOrderQualifications().isEmpty()) {
            for (OrderQualification o : shiftLeader.getOrderQualifications()) {
                orderRests.add(getOrderRest(o));
            }
        }
        return orderRests;
    }

    @Override
    public List<OrderRest> getOrdersByDateBetween(Date startDate, Date endDate) {
        List<OrderQualification> orderQualifications = orderRepo.findAllByOrderDateBetween(startDate, endDate);
        List<OrderRest> orderRests = new ArrayList<>();
        if (!orderQualifications.isEmpty()) {
            for (OrderQualification o : orderQualifications) {
                orderRests.add(getOrderRest(o));
            }
        }
        return orderRests;
    }

    @Override
    public StatusRest updateOrder(String orderId, OrderDto o) {
        OrderQualification orderQualification = orderRepo.findByOrderId(orderId);
        StatusRest statusRest = new StatusRest();
        if (orderQualification != null) {
            QualificationRest qr = new QualificationRest();
            List<Long> notFound = new ArrayList<>();
            List<QualificationRest> qualificationRests = new ArrayList<>();
            List<Employee> employees = new ArrayList<>();
            List<EmployeeRest> employeeRests = new ArrayList<>();
            if (o.getShift()!=null) {
                orderQualification.setShift(o.getShift());
            }
            if (o.getShiftLeaderName()!=null) {
                orderQualification.setShiftLeader(shiftLeaderRepo.findByName(o.getShiftLeaderName()));
            }
            if (o.getQualification()!=null) {
                orderQualification.setTrainingTitle(trainingTitleRepo.findByTrainingTitleName(o.getQualification()));
            }
            if (o.getOrderdate() != null) {
                orderQualification.setOrderDate(o.getOrderdate());
            }

            for (Long l : o.getMatricules()) {
                Employee e = employeeRepo.findByMatricule(l);
                if (e == null) {
                    notFound.add(l);
                } else {
                    employees.add(e);
                    employeeRests.add(getEmployeeRest(e));
                }
            }
            orderQualification.setEmployees(employees);
            statusRest.setNotFound(notFound);
            qr.setQualificationId(orderQualification.getOrderId());
            qr.setEmployeeRests(employeeRests);
            qr.setQualification(o.getQualification());
            qr.setQualificationDate(o.getOrderdate());
            qualificationRests.add(qr);
            statusRest.setQualificationRests(qualificationRests);
            orderRepo.save(orderQualification);
        } else {
            statusRest.setStatus("not found");
        }
        return statusRest;
    }
    public void deleteOrder(String orderID){
        OrderQualification order = orderRepo.findByOrderId(orderID);
        if (order == null) throw new RuntimeException("training Does Not Exist," +
                " You Try To Delete Formation With ID: " + orderID);
        orderRepo.delete(order);
    }

}
