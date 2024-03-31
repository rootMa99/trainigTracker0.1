package com.aptiv.trainig_tracker.services.serviceImpl;

import com.aptiv.trainig_tracker.domain.*;
import com.aptiv.trainig_tracker.models.*;
import com.aptiv.trainig_tracker.repositories.*;
import com.aptiv.trainig_tracker.services.OtherService;
import com.aptiv.trainig_tracker.ui.Utils;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OtherServiceImpl implements OtherService {
    //CoordinatorRepo coordinatorRepo;
    //CrewRepo crewRepo;
    //FamilyRepo familyRepo;
    //PosteRepo posteRepo;
    //TeamLeaderRepo teamLeaderRepo;
    //TrainingRepo trainingRepo;
    Utils utils;
    EmployeeRepo employeeRepo;
    ShiftLeaderRepo shiftLeaderRepo;
    TrainingTitleRepo trainingTitleRepo;
    OrderRepo orderRepo;
    TrainingTypeRepo trainingTypeRepo;
    CategoryRepo categoryRepo;
    DepartmentRepo departmentRepo;
    UserRepo userRepo;
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
        orderRest.setStatus(order.getStatus());
        List<EmployeeRest> ers = new ArrayList<>();
        for (Employee e : order.getEmployees()) {
            ers.add(getEmployeeRest(e));
        }
        orderRest.setEmployeeRests(ers);
        return orderRest;
    }

    @Override
    public List<OrderRest> getAllOrderBySl(String slName, Date startDate, Date endDate) {
        ShiftLeader shiftLeader = shiftLeaderRepo.findByNameAndOrderQualificationsOrderDateBetween(slName,startDate,
                endDate);
        List<OrderRest> orderRests = new ArrayList<>();
        if (shiftLeader==null){
            return orderRests;
        }
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
    @Override
    public void deleteOrder(String orderID){
        OrderQualification order = orderRepo.findByOrderId(orderID);
        if (order != null) {
            orderRepo.delete(order);
        };
    }
    @Override
    public void deleteMultiOrder(List<String> orderIds){
        for (String orderID: orderIds){
            deleteOrder(orderID);
        }
    }
    @Override
    public List<TrainingTypeAndTitlesDto> getAllTrainingsTypeAndTitles(){
        List<TrainingType> trainingTypes= trainingTypeRepo.findAll();
        List<TrainingTypeAndTitlesDto>tttds=new ArrayList<>();
        for (TrainingType tp: trainingTypes){
            TrainingTypeAndTitlesDto tttd=new TrainingTypeAndTitlesDto();
            tttd.setTrainingType(tp.getTtName());
            List<String> tts=new ArrayList<>();
            for (TrainingTitle tl: tp.getTrainingTitles()){
                tts.add(tl.getTrainingTitleName());
            }
            tttd.setTrainingTitles(tts);
            tttds.add(tttd);
        }
        return  tttds;
    }
    @Override
    public  HandyData getCategoriesAndDepartments(){
        List<Category> categoryList= categoryRepo.findAll();
        List<Department> departmentList= departmentRepo.findAll();
        HandyData handyData=new HandyData();
        List<String> categories=new ArrayList<>();
        List<String> department=new ArrayList<>();
        for (Category c: categoryList){
            categories.add(c.getCategoryName());
        }
        for (Department d: departmentList){
            department.add(d.getDepartmentName());
        }
        handyData.setCategories(categories);
        handyData.setDepartments(department);
        return handyData;
    }

    @Override
    public List<String> getShiftLeaders(){
        List<String> shiftLeaders= new ArrayList<>();
        List<ShiftLeader> shiftLeaders1=shiftLeaderRepo.findAll();
        for (ShiftLeader s:shiftLeaders1){
            shiftLeaders.add(s.getName());
        }
        return shiftLeaders;
    }
    @Override
    public List<UserRest> getUserRest(){
        List<UserRest> userRests=new ArrayList<>();
        List<User> users= userRepo.findAll();
        for (User u: users){
            UserRest ur=new UserRest();
            ur.setUserName(u.getUsername());
            ur.setRole(u.getRole().name());
           userRests.add(ur);
        }
        return userRests;
    }

    @Override
    public void changePwd(String name, String pwd){
        Optional<User> user = userRepo.findByUserName(name);

        if (user.isPresent()) {
            User us = user.get();
            us.setPassword(new BCryptPasswordEncoder().encode(pwd));
            userRepo.save(us);
        } else {
            throw new RuntimeException("no user found");
        }
    }

    @Override
    public void editDateByTrainer(Date updatedDate, List<String> orderTds){
        for (String id:orderTds){
            OrderQualification oq=orderRepo.findByOrderId(id);
            if (oq!=null){
                oq.setOrderDate(updatedDate);
                oq.setStatus("Confirmed");
                orderRepo.save(oq);
            }
        }

    }
}
