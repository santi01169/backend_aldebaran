package com.app.inventario.service.impl;

import com.app.inventario.dto.*;
import com.app.inventario.entities.*;
import com.app.inventario.repository.*;
import com.app.inventario.service.RecetaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
@Transactional
public class RecetaServiceImpl implements RecetaService {

    private final RecetaRepository recetaRepository;
    private final ProductoRepository productoRepository;
    private final DetalleRecetaRepository detalleRecetaRepository;

    @Override
    public RecetaResponse crear(RecetaRequest request) {
        RecetaEntity receta = RecetaEntity.builder()
                .nombre(request.nombre())
                .cantidadBaseProducida(request.cantidadBaseProducida())
                .activo(request.activo())
                .build();

        // Si vienen ingredientes, agregarlos (OPCIONAL)
        if (request.ingredientes() != null && !request.ingredientes().isEmpty()) {
            for (DetalleRecetaRequest detalle : request.ingredientes()) {
                ProductoEntity insumo = productoRepository.findById(detalle.insumoId())
                        .orElseThrow(() -> new ResponseStatusException(NOT_FOUND,
                                "Insumo no encontrado: " + detalle.insumoId()));

                DetalleRecetaEntity ingrediente = DetalleRecetaEntity.builder()
                        .receta(receta)
                        .insumo(insumo)
                        .cantidadRequerida(detalle.cantidadRequerida())
                        .build();

                receta.getIngredientes().add(ingrediente);
            }
        }

        RecetaEntity guardada = recetaRepository.save(receta);
        return toResponse(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecetaResponse> listar() {
        return recetaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RecetaResponse obtenerPorId(Long id) {
        RecetaEntity receta = recetaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Receta no encontrada"));
        return toResponse(receta);
    }

    @Override
    public RecetaResponse actualizar(Long id, RecetaRequest request) {
        RecetaEntity receta = recetaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Receta no encontrada"));

        receta.setNombre(request.nombre());
        receta.setCantidadBaseProducida(request.cantidadBaseProducida());
        receta.setActivo(request.activo());

        // Actualizar ingredientes solo si vienen en el request
        if (request.ingredientes() != null) {
            receta.getIngredientes().clear();

            for (DetalleRecetaRequest detalle : request.ingredientes()) {
                ProductoEntity insumo = productoRepository.findById(detalle.insumoId())
                        .orElseThrow(() -> new ResponseStatusException(NOT_FOUND,
                                "Insumo no encontrado: " + detalle.insumoId()));

                DetalleRecetaEntity ingrediente = DetalleRecetaEntity.builder()
                        .receta(receta)
                        .insumo(insumo)
                        .cantidadRequerida(detalle.cantidadRequerida())
                        .build();

                receta.getIngredientes().add(ingrediente);
            }
        }

        RecetaEntity guardada = recetaRepository.save(receta);
        return toResponse(guardada);
    }

    @Override
    public void eliminar(Long id) {
        RecetaEntity receta = recetaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Receta no encontrada"));

        recetaRepository.delete(receta);
    }

    // ============ NUEVOS MÉTODOS (IGUAL QUE PRODUCCIÓN) ============

    // ✅ MÉTODO MODIFICADO: Suma cantidades si el insumo ya existe
    @Override
    public RecetaResponse agregarIngrediente(Long recetaId, DetalleRecetaRequest detalle) {
        RecetaEntity receta = recetaRepository.findById(recetaId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Receta no encontrada"));

        ProductoEntity insumo = productoRepository.findById(detalle.insumoId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Insumo no encontrado"));

        // ✅ NUEVO: Verificar si el insumo ya existe en la receta
        DetalleRecetaEntity ingredienteExistente = receta.getIngredientes().stream()
                .filter(i -> i.getInsumo().getId().equals(detalle.insumoId()))
                .findFirst()
                .orElse(null);

        if (ingredienteExistente != null) {
            // ✅ Si ya existe, SUMAR la cantidad
            int nuevaCantidad = ingredienteExistente.getCantidadRequerida() + detalle.cantidadRequerida();
            ingredienteExistente.setCantidadRequerida(nuevaCantidad);
        } else {
            // ✅ Si NO existe, agregar nuevo ingrediente
            DetalleRecetaEntity nuevoIngrediente = DetalleRecetaEntity.builder()
                    .receta(receta)
                    .insumo(insumo)
                    .cantidadRequerida(detalle.cantidadRequerida())
                    .build();

            receta.getIngredientes().add(nuevoIngrediente);
        }

        RecetaEntity guardada = recetaRepository.save(receta);
        return toResponse(guardada);
    }

    @Override
    public void eliminarIngrediente(Long ingredienteId) {
        DetalleRecetaEntity ingrediente = detalleRecetaRepository.findById(ingredienteId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Ingrediente no encontrado"));

        detalleRecetaRepository.delete(ingrediente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleProduccionRequest> obtenerDetallesDeReceta(Long recetaId) {
        RecetaEntity receta = recetaRepository.findById(recetaId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Receta no encontrada"));

        return receta.getIngredientes().stream()
                .map(ing -> new DetalleProduccionRequest(
                        ing.getInsumo().getId(),
                        ing.getCantidadRequerida(),
                        "Según receta: " + receta.getNombre()
                ))
                .collect(Collectors.toList());
    }

    private RecetaResponse toResponse(RecetaEntity entity) {
        List<DetalleRecetaResponse> detalles = entity.getIngredientes().stream()
                .map(d -> new DetalleRecetaResponse(
                        d.getId(),
                        d.getInsumo().getId(),
                        d.getInsumo().getNombre(),
                        d.getCantidadRequerida()
                ))
                .collect(Collectors.toList());

        return new RecetaResponse(
                entity.getId(),
                entity.getNombre(),
                entity.getCantidadBaseProducida(),
                entity.isActivo(),
                detalles
        );
    }
}
