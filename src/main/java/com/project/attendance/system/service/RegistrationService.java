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
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RegistrationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private LecturerRepository lecturerRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Transactional(rollbackFor = Exception.class)
    public void registerNewStudent(StudentRegistrationRequest request) throws Exception {
        if (userRepository.findByUsername(request.getMatricNumber()).isPresent())
            throw new IllegalStateException("Error: Matric number is already registered.");

        if (userRepository.existsByEmail(request.getEmail()))
            throw new IllegalStateException("Error: Email is already in use.");

        User user = new User();
        user.setUsername(request.getMatricNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setFingerprintTemplateBase64(request.getFingerprintTemplateBase64());
        user.setActive(true);

        Role studentRole = roleRepository.findByName("STUDENT")
                .orElseThrow(() -> new RuntimeException("Role not found: STUDENT"));

        Set<Role> roles = new HashSet<>();
        roles.add(studentRole);
        user.setRoles(roles);

        User savedUser = userRepository.saveAndFlush(user);

        Student student = new Student();
        student.setMatricNumber(request.getMatricNumber());
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());
        student.setDepartment(request.getDepartment());
        student.setUser(savedUser);
        student.setFingerprintTemplate(request.getFingerprintTemplateBase64());

        studentRepository.save(student);
    }

    @Transactional(rollbackFor = Exception.class)
    public Lecturer registerNewLecturer(LecturerRegistrationRequest request) {
        if (userRepository.findByUsername(request.getStaffId()).isPresent())
            throw new RuntimeException("Error: Staff ID is already registered.");

        if (userRepository.existsByEmail(request.getEmail()))
            throw new RuntimeException("Error: Email is already in use.");

        User user = new User();
        user.setUsername(request.getStaffId());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setActive(true);

        Role lecturerRole = roleRepository.findByName("LECTURER")
                .orElseThrow(() -> new RuntimeException("Error Role not found: LECTURER"));

        Set<Role> roles = new HashSet<>();
        roles.add(lecturerRole);
        user.setRoles(roles);

        User savedUser = userRepository.saveAndFlush(user);

        Lecturer lecturer = new Lecturer();
        lecturer.setStaffId(request.getStaffId());
        lecturer.setFirstName(request.getFirstName());
        lecturer.setLastName(request.getLastName());
        lecturer.setDepartment(request.getDepartment());
        lecturer.setEmail(request.getEmail());
        lecturer.setUser(savedUser);

        return lecturerRepository.save(lecturer);
    }
    @Transactional(rollbackFor = Exception.class)
    public void registerNewAdmin(AdminRegistrationRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent())
            throw new RuntimeException("Error: Admin username already registered");

        if (userRepository.existsByEmail(request.getEmail()))
            throw new RuntimeException("Error: Admin email already registered");

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setActive(true);

        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new RuntimeException("Error Role not found: ADMIN"));

        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);
        user.setRoles(roles);

        userRepository.saveAndFlush(user);
    }
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getAllRegisteredUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(u -> {
            Map<String, Object> map = new HashMap<>();
            map.put("userId", u.getUserId());
            map.put("username", u.getUsername());
            map.put("email", u.getEmail());
            map.put("firstName", u.getFirstName());
            map.put("lastName", u.getLastName());
            map.put("active", u.isActive());
            map.put("roles", u.getRoles());
            map.put("fingerprintTemplateBase64", u.getFingerprintTemplateBase64());

            if (u.getStudent() != null) {
                map.put("matricNumber", u.getStudent().getMatricNumber());
                map.put("department", u.getStudent().getDepartment());
            } else if (u.getLecturer() != null) {
                map.put("matricNumber", u.getLecturer().getStaffId());
                map.put("department", u.getLecturer().getDepartment());
            }

            return map;
        }).collect(Collectors.toList());
    }
}