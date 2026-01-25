package com.project.attendance.system.service;

import com.project.attendance.system.dto.AdminRegistrationRequest;
import com.project.attendance.system.dto.LecturerRegistrationRequest;
import com.project.attendance.system.dto.StudentRegistrationRequest;
import com.project.attendance.system.models.Lecturer;
import com.project.attendance.system.models.Role;
import com.project.attendance.system.models.Student;
import com.project.attendance.system.models.User;
import com.project.attendance.system.repo.LecturerRepository;
import com.project.attendance.system.repo.RoleRepository;
import com.project.attendance.system.repo.StudentRepository;
import com.project.attendance.system.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

@Service
public class RegistrationService {
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private LecturerRepository lecturerRepository;
    @Autowired
    private RoleRepository roleRepository;

    public void registerNewStudent(StudentRegistrationRequest request) throws Exception{
        if (userRepository.findByUsername(request.getMatricNumber()).isPresent())throw new  IllegalStateException("Error :Student already registered.");

        User user = new User();
        user.setUsername(request.getMatricNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setActive(true);

        Role studentRole =roleRepository.findByName("STUDENT").orElseThrow(()-> new RuntimeException("Role not found:STUDENT"));
        if (user.getRoles() == null)
            user.setRoles(new HashSet<>());
        user.getRoles().add(studentRole);
        User saveUser = userRepository.save(user);

        Student student = new Student();
        student.setMatricNumber(request.getMatricNumber());
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());
        student.setUser(saveUser);

        byte[] fingerprintByte =Base64.getDecoder().decode(request.getFingerprintTemplateBase64());
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedFingerprint =md.digest(fingerprintByte);
        student.setFingerprintTemplate(hashedFingerprint);
        studentRepository.save(student);
    }
    public Lecturer registerNewLecturer(LecturerRegistrationRequest request){
        if (userRepository.findByUsername(request.getStaffId()).isPresent())throw new RuntimeException("Error: staff ID is already registered as a username");
        if (lecturerRepository.findByStaffId(request.getStaffId()).isPresent())throw new RuntimeException("Error: staff ID is already register in this table");

        User user = new User();
        user.setUsername(request.getStaffId());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setActive(true);

        Role lecturerRole = roleRepository.findByName("LECTURER").orElseThrow(()->new RuntimeException("Error Role not found:LECTURER"));
        Set<Role> roles =new HashSet<>();
        roles.add(lecturerRole);
        user.setRoles(roles);
        User saveUser = userRepository.save(user);

        Lecturer lecturer = new Lecturer();
        lecturer.setStaffId(request.getStaffId());
        lecturer.setFirstName(request.getFirstName());
        lecturer.setLastName(request.getLastName());
        lecturer.setDepartment(request.getDepartment());
        lecturer.setEmail(request.getEmail());
        lecturer.setUser(saveUser);

        return lecturerRepository.save(lecturer);

    }
    public void registerNewAdmin(AdminRegistrationRequest request){
        if (userRepository.findByUsername(request.getUsername()).isPresent())throw new RuntimeException("Error: Admin username already registered");

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setActive(true);

        Role adminRole = roleRepository.findByName("ADMIN").orElseThrow(()->new RuntimeException("Error Role not found:ADMIN"));
        Set<Role>roles = new HashSet<>();
        roles.add(adminRole);
        user.setRoles(roles);
        userRepository.save(user);
    }

}
