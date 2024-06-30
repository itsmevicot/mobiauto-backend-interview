package com.mobiauto.backend.domain.models;

import com.mobiauto.backend.domain.enums.CargosEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Cargo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CargosEnum nome;

    @OneToMany(mappedBy = "cargo")
    private List<Perfil> perfis;

    @ManyToMany
    @JoinTable(
            name = "CargoPermissao",
            joinColumns = @JoinColumn(name = "cargo_id"),
            inverseJoinColumns = @JoinColumn(name = "permissao_id")
    )
    private List<Permissao> permissoes;

}
