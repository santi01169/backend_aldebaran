package com.app.inventario.controller;

import com.app.inventario.dto.DashboardResponse;
import com.app.inventario.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public DashboardResponse obtenerDashboard() {
        return dashboardService.obtenerDatosDashboard();
    }
}
