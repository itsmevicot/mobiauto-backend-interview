package com.mobiauto.backend.application.mappers;

import com.mobiauto.backend.application.dtos.Oportunidade.OportunidadeDTO;
import com.mobiauto.backend.application.dtos.Oportunidade.CreateOportunidadeDTO;
import com.mobiauto.backend.application.dtos.Oportunidade.UpdateOportunidadeDTO;
import com.mobiauto.backend.domain.models.Oportunidade;
import com.mobiauto.backend.domain.models.Cliente;
import com.mobiauto.backend.domain.models.Revenda;
import com.mobiauto.backend.domain.models.Veiculo;
import com.mobiauto.backend.domain.models.Usuario;
import org.springframework.stereotype.Component;

@Component
public class OportunidadeMapper {

    public OportunidadeDTO toDTO(Oportunidade oportunidade) {
        return new OportunidadeDTO(
                oportunidade.getId(),
                oportunidade.getCodigo(),
                oportunidade.getStatus(),
                oportunidade.getMotivoConclusao(),
                oportunidade.getDataAtribuicao(),
                oportunidade.getDataConclusao(),
                oportunidade.getCliente().getId(),
                oportunidade.getRevenda().getId(),
                oportunidade.getVeiculo().getId(),
                oportunidade.getResponsavelAtendimento().getId()
        );
    }

    public Oportunidade toEntity(CreateOportunidadeDTO dto, Cliente cliente, Revenda revenda, Veiculo veiculo, Usuario responsavelAtendimento) {
        Oportunidade oportunidade = new Oportunidade();
        oportunidade.setCodigo(dto.codigo());
        oportunidade.setStatus(dto.status());
        oportunidade.setMotivoConclusao(dto.motivoConclusao());
        oportunidade.setDataAtribuicao(dto.dataAtribuicao());
        oportunidade.setDataConclusao(dto.dataConclusao());
        oportunidade.setCliente(cliente);
        oportunidade.setRevenda(revenda);
        oportunidade.setVeiculo(veiculo);
        oportunidade.setResponsavelAtendimento(responsavelAtendimento);
        return oportunidade;
    }

    public void updateEntityFromDTO(UpdateOportunidadeDTO dto, Oportunidade oportunidade, Cliente cliente, Revenda revenda, Veiculo veiculo, Usuario responsavelAtendimento) {
        oportunidade.setCodigo(dto.codigo());
        oportunidade.setStatus(dto.status());
        oportunidade.setMotivoConclusao(dto.motivoConclusao());
        oportunidade.setDataAtribuicao(dto.dataAtribuicao());
        oportunidade.setDataConclusao(dto.dataConclusao());
        oportunidade.setCliente(cliente);
        oportunidade.setRevenda(revenda);
        oportunidade.setVeiculo(veiculo);
        oportunidade.setResponsavelAtendimento(responsavelAtendimento);
    }
}
