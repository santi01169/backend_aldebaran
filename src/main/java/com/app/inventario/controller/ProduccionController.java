package com.app.inventario.controller;

import com.app.inventario.dto.ProduccionRequest;
import com.app.inventario.dto.ProduccionResponse;
import com.app.inventario.service.ProduccionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/produccion")
public class ProduccionController {

    private final ProduccionService produccionService;

    public ProduccionController(ProduccionService produccionService) {
        this.produccionService = produccionService;
    }

    @GetMapping
    public List<ProduccionResponse> listar() {
        return produccionService.listar();
    }

    @GetMapping("/filtro")
    public ResponseEntity<Page<ProduccionResponse>> filtrar(
            @RequestParam(required = false) String orden,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
            @RequestParam(required = false) Long usuarioResponsableId,
            @PageableDefault(size = 20, sort = "fechaProduccion") Pageable pageable
    ) {
        Page<ProduccionResponse> pagina = produccionService.filtrar(
                orden, estado, fechaDesde, fechaHasta, usuarioResponsableId, pageable);
        return ResponseEntity.ok(pagina);
    }

    @PostMapping
    public ProduccionResponse crear(@RequestBody ProduccionRequest request) {
        return produccionService.crear(request);
    }
}
