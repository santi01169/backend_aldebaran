package com.app.inventario.service.impl;

import com.app.inventario.dto.ProveedorRequest;
import com.app.inventario.dto.ProveedorResponse;
import com.app.inventario.entities.ProveedorEntity;
import com.app.inventario.repository.ProveedorRepository;
import com.app.inventario.service.ProveedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
                .contacto(request.contacto())
                .telefono(request.telefono())
                .email(request.email())
                .direccion(request.direccion())
                .build();

        return toResponse(proveedorRepository.save(entity));
    }

    @Override
    public ProveedorResponse actualizar(Long id, ProveedorRequest request) {
        return null;
    }

    @Override
    public void eliminar(Long id) {

    }

    @Override
    public ProveedorResponse actualizar(Integer id, ProveedorRequest request) {
        ProveedorEntity entity = proveedorRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Proveedor no encontrado"));

        entity.setNombre(request.nombre());
        entity.setContacto(request.contacto());
        entity.setTelefono(request.telefono());
        entity.setEmail(request.email());
        entity.setDireccion(request.direccion());

        return toResponse(proveedorRepository.save(entity));
    }

    @Override
    public void eliminar(Integer id) {
        ProveedorEntity entity = proveedorRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Proveedor no encontrado"));

        proveedorRepository.delete(entity);
    }

    private ProveedorResponse toResponse(ProveedorEntity e) {
        return new ProveedorResponse(
                (long) Math.toIntExact(e.getId()),
                e.getNombre(),
                e.getContacto(),
                e.getTelefono(),
                e.getEmail(),
                e.getDireccion()
        );
    }
}
