package com.project.attendance.system.service;

import com.project.attendance.system.models.User;
import com.project.attendance.system.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class CustomUserDetailsService implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String Principal) throws UsernameNotFoundException {

        Optional<User>userOptional;
        if (Principal.contains("@")){
           userOptional= userRepository.findByEmail(Principal);
        }else{
          userOptional=  userRepository.findByUsername(Principal);
        }

        User user = userOptional.orElseThrow(()-> new UsernameNotFoundException("User not found :" +Principal));

        List<SimpleGrantedAuthority>authorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().toUpperCase())).toList();
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );

    }
}