package com.app.inventario.service.impl;

import com.app.inventario.dto.*;
import com.app.inventario.entities.*;
import com.app.inventario.repository.*;
import com.app.inventario.service.ProduccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ProduccionServiceImpl implements ProduccionService {

    private final ProduccionRepository produccionRepository;
    private final DetalleProduccionRepository detalleProduccionRepository;
    private final ProductoRepository productoRepository;
    private final StockRepository stockRepository;
    private final UsuarioRepository userRepository;
    private final RecetaRepository recetaRepository; // ⬅️ AGREGADO

    @Override
    @Transactional(readOnly = true)
    public List<ProduccionResponse> listar() {
        return produccionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProduccionResponse obtenerPorId(Long id) {
        ProduccionEntity entity = produccionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Producción no encontrada"));
        return toResponse(entity);
    }

    @Override
    public ProduccionResponse crear(ProduccionRequest request) {
        // Validar orden duplicada
        if (produccionRepository.findByOrden(request.orden()).isPresent()) {
            throw new ResponseStatusException(BAD_REQUEST, "Orden duplicada");
        }

        // Buscar usuario
        UsuarioEntity usuario = null;
        if (request.usuarioResponsableId() != null) {
            usuario = userRepository.findById(request.usuarioResponsableId())
                    .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Usuario no encontrado"));
        }

        // Crear producción SIN validar stock (porque no hay detalles aún)
        ProduccionEntity produccion = ProduccionEntity.builder()
                .orden(request.orden())
                .cantidadAProducir(request.cantidadAProducir())
                .fechaProduccion(request.fechaProduccion() != null ? request.fechaProduccion() : LocalDate.now())
                .estado(request.estado())
                .usuarioResponsable(usuario)
                .observacion(request.observacion())
                .build();

        // Si vienen detalles, agregarlos
        if (request.detalles() != null && !request.detalles().isEmpty()) {
            validarStockDisponible(request.detalles());

            for (DetalleProduccionRequest detalleReq : request.detalles()) {
                ProductoEntity producto = productoRepository.findById(detalleReq.productoId())
                        .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Producto no encontrado"));

                DetalleProduccionEntity detalle = DetalleProduccionEntity.builder()
                        .produccion(produccion)
                        .producto(producto)
                        .cantidadUtilizada(detalleReq.cantidadUtilizada())
                        .observacion(detalleReq.observacion())
                        .build();

                produccion.getDetalles().add(detalle);
            }
        }

        ProduccionEntity guardada = produccionRepository.save(produccion);
        return toResponse(guardada);
    }

    @Override
    public ProduccionResponse actualizar(Long id, ProduccionRequest request) {
        ProduccionEntity entity = produccionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Producción no encontrada"));

        if (!"Pendiente".equals(entity.getEstado())) {
            throw new ResponseStatusException(BAD_REQUEST,
                    "Solo se pueden actualizar producciones en estado Pendiente");
        }

        validarStockDisponible(request.detalles());

        entity.setOrden(request.orden());
        entity.setCantidadAProducir(request.cantidadAProducir());
        entity.setFechaProduccion(request.fechaProduccion());
        entity.setEstado(request.estado());
        entity.setObservacion(request.observacion());

        if (request.usuarioResponsableId() != null) {
            UsuarioEntity usuario = userRepository.findById(request.usuarioResponsableId())
                    .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Usuario no encontrado"));
            entity.setUsuarioResponsable(usuario);
        }

        entity.getDetalles().clear();

        for (DetalleProduccionRequest detalleReq : request.detalles()) {
            ProductoEntity producto = productoRepository.findById(detalleReq.productoId())
                    .orElseThrow(() -> new ResponseStatusException(NOT_FOUND,
                            "Producto no encontrado: " + detalleReq.productoId()));

            DetalleProduccionEntity detalle = DetalleProduccionEntity.builder()
                    .produccion(entity)
                    .producto(producto)
                    .cantidadUtilizada(detalleReq.cantidadUtilizada())
                    .observacion(detalleReq.observacion())
                    .build();

            entity.getDetalles().add(detalle);
        }

        ProduccionEntity guardada = produccionRepository.save(entity);
        return toResponse(guardada);
    }

    @Override
    public void eliminar(Long id) {
        ProduccionEntity entity = produccionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Producción no encontrada"));

        if (!"Pendiente".equals(entity.getEstado())) {
            throw new ResponseStatusException(BAD_REQUEST,
                    "Solo se pueden eliminar producciones en estado Pendiente");
        }

        produccionRepository.deleteById(id);
    }

    @Override
    public ProduccionResponse completarProduccion(Long id) {
        ProduccionEntity entity = produccionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Producción no encontrada"));

        if (!"Pendiente".equals(entity.getEstado()) && !"En Proceso".equals(entity.getEstado())) {
            throw new ResponseStatusException(BAD_REQUEST,
                    "Solo se pueden completar producciones en estado Pendiente o En Proceso");
        }

        List<DetalleProduccionRequest> detalles = entity.getDetalles().stream()
                .map(d -> new DetalleProduccionRequest(
                        d.getProducto().getId(),
                        d.getCantidadUtilizada(),
                        d.getObservacion()
                ))
                .toList();
        validarStockDisponible(detalles);

        // Descontar del stock
        for (DetalleProduccionEntity detalle : entity.getDetalles()) {
            StockEntity stock = stockRepository.findByInsumo_Id(detalle.getProducto().getId())
                    .orElseThrow(() -> new ResponseStatusException(NOT_FOUND,
                            "No existe stock para el producto: " + detalle.getProducto().getNombre()));

            int nuevaCantidad = stock.getCantidadActual() - detalle.getCantidadUtilizada();
            stock.setCantidadActual(nuevaCantidad);
            stock.setFechaActualizacion(LocalDate.now());
            stockRepository.save(stock);
        }

        entity.setEstado("Completada");
        ProduccionEntity guardada = produccionRepository.save(entity);
        return toResponse(guardada);
    }

    @Override
    public ProduccionResponse agregarDetallesDesdeReceta(Long produccionId, Long recetaId) {
        ProduccionEntity produccion = produccionRepository.findById(produccionId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Producción no encontrada"));

        if (!"Pendiente".equals(produccion.getEstado())) {
            throw new ResponseStatusException(BAD_REQUEST, "Solo se puede modificar producción Pendiente");
        }

        RecetaEntity receta = recetaRepository.findById(recetaId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Receta no encontrada"));

        // Agregar todos los ingredientes de la receta
        for (DetalleRecetaEntity ingrediente : receta.getIngredientes()) {
            DetalleProduccionEntity detalle = DetalleProduccionEntity.builder()
                    .produccion(produccion)
                    .producto(ingrediente.getInsumo())
                    .cantidadUtilizada(ingrediente.getCantidadRequerida())
                    .observacion("Desde receta: " + receta.getNombre())
                    .build();

            produccion.getDetalles().add(detalle);
        }

        // Validar stock con los nuevos detalles
        List<DetalleProduccionRequest> detallesValidar = produccion.getDetalles().stream()
                .map(d -> new DetalleProduccionRequest(
                        d.getProducto().getId(),
                        d.getCantidadUtilizada(),
                        d.getObservacion()
                )).toList();

        validarStockDisponible(detallesValidar);

        ProduccionEntity guardada = produccionRepository.save(produccion);
        return toResponse(guardada);
    }

    @Override
    public ProduccionResponse agregarDetalleProducto(Long produccionId, DetalleProduccionRequest detalle) {
        ProduccionEntity produccion = produccionRepository.findById(produccionId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Producción no encontrada"));

        if (!"Pendiente".equals(produccion.getEstado())) {
            throw new ResponseStatusException(BAD_REQUEST, "Solo se puede modificar producción Pendiente");
        }

        ProductoEntity producto = productoRepository.findById(detalle.productoId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Producto no encontrado"));

        // Validar stock del nuevo producto
        validarStockDisponible(List.of(detalle));

        DetalleProduccionEntity nuevoDetalle = DetalleProduccionEntity.builder()
                .produccion(produccion)
                .producto(producto)
                .cantidadUtilizada(detalle.cantidadUtilizada())
                .observacion(detalle.observacion())
                .build();

        produccion.getDetalles().add(nuevoDetalle);

        ProduccionEntity guardada = produccionRepository.save(produccion);
        return toResponse(guardada);
    }

    @Override
    public void eliminarDetalle(Long detalleId) {
        DetalleProduccionEntity detalle = detalleProduccionRepository.findById(detalleId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Detalle no encontrado"));

        if (!"Pendiente".equals(detalle.getProduccion().getEstado())) {
            throw new ResponseStatusException(BAD_REQUEST,
                    "No se puede modificar producción no pendiente");
        }

        detalleProduccionRepository.delete(detalle);
    }

    /**
     * MÉTODO CRÍTICO: Valida que haya suficiente stock para todos los insumos
     */
    private void validarStockDisponible(List<DetalleProduccionRequest> detalles) {
        StringBuilder errores = new StringBuilder();

        for (DetalleProduccionRequest detalle : detalles) {
            ProductoEntity producto = productoRepository.findById(detalle.productoId())
                    .orElseThrow(() -> new ResponseStatusException(NOT_FOUND,
                            "Producto no encontrado: " + detalle.productoId()));

            StockEntity stock = stockRepository.findByInsumo_Id(detalle.productoId())
                    .orElse(null);

            if (stock == null) {
                errores.append(String.format("- El producto '%s' no tiene registro de stock.%n",
                        producto.getNombre()));
                continue;
            }

            if (stock.getCantidadActual() < detalle.cantidadUtilizada()) {
                errores.append(String.format(
                        "- El producto '%s' no tiene suficiente stock. Disponible: %d, Requerido: %d.%n",
                        producto.getNombre(),
                        stock.getCantidadActual(),
                        detalle.cantidadUtilizada()
                ));
            }
        }

        if (errores.length() > 0) {
            throw new ResponseStatusException(BAD_REQUEST,
                    "No hay suficiente stock para crear la producción:%n" + errores.toString());
        }
    }

    private ProduccionResponse toResponse(ProduccionEntity e) {
        List<DetalleProduccionResponse> detalles = e.getDetalles().stream()
                .map(d -> new DetalleProduccionResponse(
                        d.getId(),
                        d.getProducto().getId(),
                        d.getProducto().getNombre(),
                        d.getCantidadUtilizada(),
                        d.getObservacion()
                ))
                .collect(Collectors.toList());

        return new ProduccionResponse(
                e.getId(),
                e.getOrden(),
                e.getCantidadAProducir(),
                e.getFechaProduccion(),
                e.getEstado(),
                e.getUsuarioResponsable() != null ? e.getUsuarioResponsable().getId() : null,
                e.getUsuarioResponsable() != null ? e.getUsuarioResponsable().getNombre() : null,
                e.getObservacion(),
                detalles
        );
    }
}
