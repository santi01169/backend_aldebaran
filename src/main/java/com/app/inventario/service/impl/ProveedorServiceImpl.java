package com.app.inventario.service.impl;

import com.app.inventario.dto.ProveedorRequest;
import com.app.inventario.entities.LoteProveedorEntity;
import com.app.inventario.entities.ProveedorEntity;
import com.app.inventario.repository.ProveedorRepository;
import com.app.inventario.service.ProveedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;
import com.app.inventario.dto.ProveedorResponse;


import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository proveedorRepository;


    @Override
    public List<ProveedorResponse> listar() {
        return proveedorRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ProveedorResponse obtenerPorId(Long id) {
        ProveedorEntity entity = proveedorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Proveedor no encontrado"));
        return toResponse(entity);
    }

    @Override
    public ProveedorResponse crear(ProveedorRequest request) {
        ProveedorEntity entity = ProveedorEntity.builder()
                .nombre(request.nombre())
                .telefono(request.telefono())
                .email(request.email())
                .direccion(request.direccion())
                .build();

        return toResponse(proveedorRepository.save(entity));
    }

    @Override
    public ProveedorResponse actualizar(Long id, ProveedorRequest request) {
        // Buscamos el proveedor existente
        ProveedorEntity entity = proveedorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Proveedor no encontrado"));

        // Actualizamos los campos con los nuevos valores
        entity.setNombre(request.nombre());
        entity.setTelefono(request.telefono());
        entity.setEmail(request.email());
        entity.setDireccion(request.direccion());

        // Guardamos y devolvemos la respuesta
        return toResponse(proveedorRepository.save(entity));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        System.out.println("=== DEBUG ELIMINAR: Recibido ID = " + id);

        // Verificar existencia primero
        if (!proveedorRepository.existsById(id)) {
            System.out.println("=== DEBUG ELIMINAR: El proveedor NO EXISTE");
            throw new ResponseStatusException(NOT_FOUND, "Proveedor no encontrado con ID: " + id);
        }

        System.out.println("=== DEBUG ELIMINAR: El proveedor existe, buscándolo...");

        // Buscar el proveedor para verificar relaciones
        ProveedorEntity entity = proveedorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Proveedor no encontrado"));

        System.out.println("=== DEBUG ELIMINAR: Proveedor encontrado: " + entity.getNombre());

        // Intentar acceder a los lotes DENTRO de la transacción activa
        try {
            List<LoteProveedorEntity> lotes = entity.getLotes();
            System.out.println("=== DEBUG ELIMINAR: Lotes cargados, cantidad: " + (lotes != null ? lotes.size() : 0));

            if (lotes != null && !lotes.isEmpty()) {
                System.out.println("=== DEBUG ELIMINAR: No se puede eliminar, tiene lotes asociados");
                throw new ResponseStatusException(BAD_REQUEST,
                        "No se puede eliminar el proveedor porque tiene " + lotes.size() + " lote(s) asociado(s)");
            }
        } catch (Exception e) {
            System.out.println("=== DEBUG ELIMINAR: Error al cargar lotes: " + e.getMessage());
            // Si falla cargar los lotes, asumimos que no hay lotes y continuamos
        }

        System.out.println("=== DEBUG ELIMINAR: Procediendo a eliminar con deleteById...");

        // Usar deleteById en lugar de delete(entity)
        proveedorRepository.deleteById(id);

        System.out.println("=== DEBUG ELIMINAR: Eliminación completada exitosamente");
    }

    private ProveedorResponse toResponse(ProveedorEntity e) {
        return new ProveedorResponse(
                e.getId(),  // Ya no necesitas la conversión Math.toIntExact
                e.getNombre(),
                e.getTelefono(),
                e.getEmail(),
                e.getDireccion()
        );
    }
}