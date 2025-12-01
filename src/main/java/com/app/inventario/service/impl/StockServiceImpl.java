package com.app.inventario.service.impl;

import com.app.inventario.dto.MovimientoStockRequest;
import com.app.inventario.dto.MovimientoStockResponse;
import com.app.inventario.dto.StockRequest;
import com.app.inventario.dto.StockResponse;
import com.app.inventario.entities.*;
import com.app.inventario.repository.*;
import com.app.inventario.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final MovimientoStockRepository movimientoStockRepository;
    private final ProductoRepository productoRepository;
    private final LoteProveedorRepository loteProveedorRepository;

    @Override
    public List<StockResponse> listar() {
        return stockRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public StockResponse obtenerPorId(Integer id) {
        StockEntity entity = stockRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Stock no encontrado"));
        return toResponse(entity);
    }

    @Override
    public StockResponse obtenerPorInsumo(Long insumoId) {
        StockEntity entity = stockRepository.findByInsumo_Id(insumoId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Stock no encontrado para el insumo"));
        return toResponse(entity);
    }

    @Override
    public StockResponse crearOActualizar(StockRequest request) {
        ProductoEntity insumo = productoRepository.findById(request.insumoId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Insumo no encontrado"));

        if (insumo.getCategoria() == null) {
            throw new ResponseStatusException(BAD_REQUEST, "El insumo debe tener una categoría asignada");
        }

        if (insumo.getUnidadMedida() == null) {
            throw new ResponseStatusException(BAD_REQUEST, "El insumo debe tener una unidad de medida asignada");
        }

        StockEntity entity = stockRepository.findByInsumo_Id(request.insumoId())
                .orElseGet(() -> {
                    StockEntity nuevo = new StockEntity();
                    nuevo.setInsumo(insumo);
                    return nuevo;
                });

        entity.setCantidadActual(Math.toIntExact(request.cantidadActual()));
        entity.setUbicacion(request.ubicacion());
        entity.setFechaActualizacion(LocalDate.now());

        if (request.loteProveedorId() != null) {
            LoteProveedorEntity lote = loteProveedorRepository.findById(request.loteProveedorId())
                    .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Lote no encontrado"));
            entity.setLoteProveedor(lote);
        } else {
            entity.setLoteProveedor(null);
        }

        StockEntity guardado = stockRepository.save(entity);
        return toResponse(guardado);
    }

    @Override
    public StockResponse registrarEntrada(Integer stockId, MovimientoStockRequest request) {
        StockEntity entity = stockRepository.findById(stockId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Stock no encontrado"));

        int antes = entity.getCantidadActual();
        int despues = antes + request.cantidad();

        entity.setCantidadActual(despues);
        entity.setFechaActualizacion(LocalDate.now());
        stockRepository.save(entity);

        guardarMovimiento(entity, TipoMovimientoStock.ENTRADA, request, antes, despues);

        return toResponse(entity);
    }

    @Override
    public StockResponse registrarSalida(Integer stockId, MovimientoStockRequest request) {
        StockEntity entity = stockRepository.findById(stockId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Stock no encontrado"));

        int antes = entity.getCantidadActual();
        int despues = antes - request.cantidad();

        if (despues < 0) {
            throw new ResponseStatusException(BAD_REQUEST, "No hay suficiente stock para la salida");
        }

        entity.setCantidadActual(despues);
        entity.setFechaActualizacion(LocalDate.now());
        stockRepository.save(entity);

        guardarMovimiento(entity, TipoMovimientoStock.SALIDA, request, antes, despues);

        return toResponse(entity);
    }

    @Override
    public List<MovimientoStockResponse> listarMovimientos(Integer stockId) {
        StockEntity entity = stockRepository.findById(stockId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Stock no encontrado"));

        return movimientoStockRepository.findByStockOrderByFechaMovimientoDesc(entity)
                .stream()
                .map(this::toMovimientoResponse)
                .toList();
    }

    @Override
    public StockResponse actualizar(Integer id, StockRequest request) {
        StockEntity entity = stockRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Stock no encontrado"));

        ProductoEntity insumo = productoRepository.findById(request.insumoId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Insumo no encontrado"));

        if (insumo.getCategoria() == null) {
            throw new ResponseStatusException(BAD_REQUEST, "El insumo debe tener una categoría asignada");
        }

        if (insumo.getUnidadMedida() == null) {
            throw new ResponseStatusException(BAD_REQUEST, "El insumo debe tener una unidad de medida asignada");
        }

        entity.setInsumo(insumo);
        entity.setCantidadActual(Math.toIntExact(request.cantidadActual()));
        entity.setUbicacion(request.ubicacion());
        entity.setFechaActualizacion(LocalDate.now());

        if (request.loteProveedorId() != null) {
            LoteProveedorEntity lote = loteProveedorRepository.findById(request.loteProveedorId())
                    .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Lote no encontrado"));
            entity.setLoteProveedor(lote);
        } else {
            entity.setLoteProveedor(null);
        }

        StockEntity actualizado = stockRepository.save(entity);
        return toResponse(actualizado);
    }

    @Override
    public void eliminar(Integer id) {
        StockEntity entity = stockRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Stock no encontrado"));

        // Verificar si tiene movimientos asociados
        List<MovimientoStockEntity> movimientos = movimientoStockRepository
                .findByStockOrderByFechaMovimientoDesc(entity);

        if (!movimientos.isEmpty()) {
            throw new ResponseStatusException(BAD_REQUEST,
                    "No se puede eliminar el stock porque tiene movimientos registrados");
        }

        stockRepository.delete(entity);
    }


    private void guardarMovimiento(
            StockEntity stock,
            TipoMovimientoStock tipo,
            MovimientoStockRequest request,
            int antes,
            int despues
    ) {
        MovimientoStockEntity mov = MovimientoStockEntity.builder()
                .stock(stock)
                .tipo(tipo)
                .cantidad(request.cantidad())
                .stockAntes(antes)
                .stockDespues(despues)
                .motivo(request.motivo())
                .fechaMovimiento(LocalDateTime.now())
                .build();

        movimientoStockRepository.save(mov);
    }

    private StockResponse toResponse(StockEntity e) {
        ProveedorEntity proveedor = (e.getLoteProveedor() != null) ? e.getLoteProveedor().getProveedor() : null;

        return new StockResponse(
                e.getId(),
                e.getInsumo() != null ? e.getInsumo().getId() : null,
                e.getInsumo() != null ? e.getInsumo().getNombre() : null,
                e.getInsumo() != null && e.getInsumo().getCategoria() != null ? e.getInsumo().getCategoria().getId() : null,
                e.getInsumo() != null && e.getInsumo().getCategoria() != null ? e.getInsumo().getCategoria().getNombre() : null,
                e.getInsumo() != null && e.getInsumo().getUnidadMedida() != null ? e.getInsumo().getUnidadMedida().getId() : null,
                e.getInsumo() != null && e.getInsumo().getUnidadMedida() != null ? e.getInsumo().getUnidadMedida().getNombre() : null,
                e.getCantidadActual(),
                e.getUbicacion(),
                e.getLoteProveedor() != null ? e.getLoteProveedor().getId() : null,
                proveedor != null ? proveedor.getId() : null,
                proveedor != null ? proveedor.getNombre() : null,
                e.getFechaActualizacion(),
                e.getLoteProveedor() != null ? e.getLoteProveedor().getFechaVencimiento() : null,
                e.getLoteProveedor() != null ? e.getLoteProveedor().getNumeroLote() : null,
                e.getInsumo() != null ? e.getInsumo().getStockMinimo() : null
        );
    }

    private MovimientoStockResponse toMovimientoResponse(MovimientoStockEntity m) {
        return new MovimientoStockResponse(
                m.getId(),
                m.getTipo().name(),
                m.getCantidad(),
                m.getStockAntes(),
                m.getStockDespues(),
                m.getMotivo(),
                m.getFechaMovimiento()
        );
    }
}