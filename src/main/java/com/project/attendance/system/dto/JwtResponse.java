package com.project.attendance.system.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JwtResponse {
    private String accessToken;
    private String type = "Bearer";
    private String username;
    private String role;

    private String department;

    public JwtResponse(String accessToken, String username, String role,String department) {
        this.accessToken = accessToken;
        this.username = username;
        this.role = role;
        this.department =department;
    }
}
