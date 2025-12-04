package com.app.inventario.config;

import com.app.inventario.entities.RoleEntity;
import com.app.inventario.entities.RoleEnum;
import com.app.inventario.entities.UsuarioEntity;
import com.app.inventario.repository.RoleRepository;
import com.app.inventario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // Crear roles
        RoleEntity superAdmin = crearRol(RoleEnum.SUPER_ADMIN);
        crearRol(RoleEnum.ADMIN);
        crearRol(RoleEnum.OPERARIO);

        // Crear superusuario
        if (usuarioRepository.findByCorreo("superadmin@aldebaran.com").isEmpty()) {
            UsuarioEntity admin = UsuarioEntity.builder()
                    .nombre("Super")
                    .apellido("Administrador")
                    .correo("superadmin@aldebaran.com")
                    .contrasena(passwordEncoder.encode("SuperAdmin123*"))
                    .rol("SUPER_ADMIN")
                    .roles(Set.of(superAdmin))
                    .build();

            usuarioRepository.save(admin);
            System.out.println("✅ Superusuario creado: superadmin@aldebaran.com / SuperAdmin123*");
        }
    }

    private RoleEntity crearRol(RoleEnum roleEnum) {
        return roleRepository.findByRoleEnum(roleEnum)
                .orElseGet(() -> {
                    RoleEntity rol = RoleEntity.builder()
                            .roleEnum(roleEnum)
                            .build();
                    System.out.println("✅ Rol " + roleEnum.name() + " creado");
                    return roleRepository.save(rol);
                });
    }
}
