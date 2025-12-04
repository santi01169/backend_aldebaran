package com.app.inventario.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"Usuarios\"")
public class UsuarioEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"IdUsuario\"")
    private Integer id;

    @NotBlank
    @Size(max = 100)
    @Column(name = "\"Nombre\"", nullable = false)
    private String nombre;

    @NotBlank
    @Size(max = 100)
    @Column(name = "\"Apellido\"", nullable = false)
    private String apellido;

    @Email
    @NotBlank
    @Size(max = 255)
    @Column(name = "\"Correo\"", unique = true, nullable = false)
    private String correo;

    @NotBlank
    @Size(max = 255)
    @Column(name = "\"Contraseña\"")
    private String contrasena;

    @Size(max = 50)
    @Column(name = "\"Rol\"", nullable = true)
    private String rol;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roles = new HashSet<>();


    // ================= UserDetails =================

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();

        if (rol != null && !rol.isBlank()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + rol.toUpperCase()));
        }

        if (roles != null) {
            roles.forEach(r ->
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + r.getRoleEnum().name()))
            );
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return contrasena;
    }

    @Override
    public String getUsername() {
        // username para Spring será el correo
        return correo;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
