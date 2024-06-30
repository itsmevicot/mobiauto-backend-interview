package com.mobiauto.backend.application.mappers;

import com.mobiauto.backend.application.dtos.Veiculo.VeiculoDTO;
import com.mobiauto.backend.application.dtos.Veiculo.CreateVeiculoDTO;
import com.mobiauto.backend.application.dtos.Veiculo.UpdateVeiculoDTO;
import com.mobiauto.backend.domain.models.Veiculo;
import com.mobiauto.backend.domain.models.Revenda;
import org.springframework.stereotype.Component;

@Component
public class VeiculoMapper {

    public VeiculoDTO toDTO(Veiculo veiculo) {
        return new VeiculoDTO(
                veiculo.getId(),
                veiculo.getCodigo(),
                veiculo.getMarca(),
                veiculo.getModelo(),
                veiculo.getVersao(),
                veiculo.getAnoModelo(),
                veiculo.isAtivo(),
                veiculo.getRevenda().getId()
        );
    }

    public Veiculo toEntity(CreateVeiculoDTO dto, Revenda revenda) {
        Veiculo veiculo = new Veiculo();
        veiculo.setCodigo(dto.codigo());
        veiculo.setMarca(dto.marca());
        veiculo.setModelo(dto.modelo());
        veiculo.setVersao(dto.versao());
        veiculo.setAnoModelo(dto.anoModelo());
        veiculo.setAtivo(dto.ativo());
        veiculo.setRevenda(revenda);
        return veiculo;
    }

    public void updateEntityFromDTO(UpdateVeiculoDTO dto, Veiculo veiculo, Revenda revenda) {
        veiculo.setCodigo(dto.codigo());
        veiculo.setMarca(dto.marca());
        veiculo.setModelo(dto.modelo());
        veiculo.setVersao(dto.versao());
        veiculo.setAnoModelo(dto.anoModelo());
        veiculo.setAtivo(dto.ativo());
        veiculo.setRevenda(revenda);
    }
}
