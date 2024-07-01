package com.mobiauto.backend.domain.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Revenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String cnpj;

    @Column(nullable = false, unique = true)
    private String codigo;

    @Column(nullable = false)
    private String nomeSocial;

    @OneToMany(mappedBy = "revenda")
    private List<Perfil> perfis;

    @OneToMany(mappedBy = "revenda")
    private List<Oportunidade> oportunidades;

    @OneToMany(mappedBy = "revenda")
    private List<Veiculo> veiculos;

    @Column(nullable = false)
    private boolean ativo = true;
}
