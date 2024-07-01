package com.mobiauto.backend.domain.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
public class Perfil implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "revenda_id", nullable = false)
    private Revenda revenda;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "cargo_id", nullable = false)
    private Cargo cargo;

    @Override
    public String getAuthority() {
        return cargo.getNome().name();
    }

    public Collection<? extends GrantedAuthority> getPermissions() {
        return cargo.getPermissoes().stream()
                .map(permissao -> new SimpleGrantedAuthority(permissao.getDescricao()))
                .collect(Collectors.toList());
    }
}
