package com.app.inventario.service.impl;

import com.app.inventario.dto.*;
import com.app.inventario.entities.LoteProveedorEntity;
import com.app.inventario.entities.MovimientoStockEntity;
import com.app.inventario.entities.ProduccionEntity;
import com.app.inventario.entities.StockEntity;
import com.app.inventario.repository.LoteProveedorRepository;
import com.app.inventario.repository.MovimientoStockRepository;
import com.app.inventario.repository.ProduccionRepository;
import com.app.inventario.repository.StockRepository;
import com.app.inventario.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final ProduccionRepository produccionRepository;
    private final StockRepository stockRepository;
    private final LoteProveedorRepository loteProveedorRepository;
    private final MovimientoStockRepository movimientoStockRepository;

    @Override
    public DashboardResponse obtenerDatosDashboard() {
        // 1. Obtener producciones pendientes
        List<ProduccionPendienteDTO> produccionesPendientes = obtenerProduccionesPendientes();

        // 2. Obtener productos con stock bajo
        List<ProductoStockBajoDTO> productosStockBajo = obtenerProductosStockBajo();

        // 3. Obtener lotes próximos a vencer (30 días)
        List<LoteProximoVencerDTO> lotesProximosVencer = obtenerLotesProximosVencer();

        // 4. Obtener últimos 10 movimientos de stock
        List<MovimientoStockResponse> ultimosMovimientos = obtenerUltimosMovimientos();

        return new DashboardResponse(
                produccionesPendientes,
                productosStockBajo,
                lotesProximosVencer,
                ultimosMovimientos
        );
    }

    private List<ProduccionPendienteDTO> obtenerProduccionesPendientes() {
        List<ProduccionEntity> producciones = produccionRepository.findByEstado("Pendiente");

        return producciones.stream()
                .map(p -> new ProduccionPendienteDTO(
                        p.getId(),
                        p.getOrden(),
                        p.getCantidadAProducir(),
                        p.getFechaProduccion(),
                        p.getEstado()
                ))
                .toList();
    }

    private List<ProductoStockBajoDTO> obtenerProductosStockBajo() {
        List<StockEntity> stocks = stockRepository.findProductosConStockBajo();

        return stocks.stream()
                .map(s -> {
                    int cantidadActual = s.getCantidadActual();
                    int stockMinimo = s.getInsumo().getStockMinimo() != null ? s.getInsumo().getStockMinimo() : 0;

                    // Calcular porcentaje (cantidadActual / stockMinimo * 100)
                    double porcentaje = stockMinimo > 0
                            ? (cantidadActual * 100.0) / stockMinimo
                            : 100.0;

                    return new ProductoStockBajoDTO(
                            s.getInsumo().getId(),
                            s.getInsumo().getNombre(),
                            cantidadActual,
                            stockMinimo,
                            porcentaje
                    );
                })
                .toList();
    }

    private List<LoteProximoVencerDTO> obtenerLotesProximosVencer() {
        LocalDate hoy = LocalDate.now();
        LocalDate dentroDeUnMes = hoy.plusDays(30);

        List<LoteProveedorEntity> lotes = loteProveedorRepository
                .findByFechaVencimientoBetween(hoy, dentroDeUnMes);

        return lotes.stream()
                .map(l -> {
                    long diasRestantes = ChronoUnit.DAYS.between(hoy, l.getFechaVencimiento());

                    return new LoteProximoVencerDTO(
                            l.getId(),
                            l.getNumeroLote(),
                            l.getProducto().getNombre(),
                            l.getFechaVencimiento(),
                            diasRestantes
                    );
                })
                .toList();
    }

    private List<MovimientoStockResponse> obtenerUltimosMovimientos() {
        List<MovimientoStockEntity> movimientos = movimientoStockRepository
                .findAllByOrderByFechaMovimientoDesc(PageRequest.of(0, 10));

        return movimientos.stream()
                .map(m -> new MovimientoStockResponse(
                        m.getId(),
                        m.getTipo().name(),
                        m.getCantidad(),
                        m.getStockAntes(),
                        m.getStockDespues(),
                        m.getMotivo(),
                        m.getFechaMovimiento()
                ))
                .toList();
    }
}
