package com.mobiauto.backend.domain.models;

import com.mobiauto.backend.domain.enums.CargosEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "cargo", nullable = false)
    private CargosEnum cargo;


    @Override
    public String getAuthority() {
        return cargo.name();
    }
}
