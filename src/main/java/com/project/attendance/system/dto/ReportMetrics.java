package com.project.attendance.system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportMetrics {
    private int enrolled;
    private int present;
    private int absent;
    private int rate;
}
