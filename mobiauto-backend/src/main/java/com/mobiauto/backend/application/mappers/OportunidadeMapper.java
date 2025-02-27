package com.mobiauto.backend.application.mappers;

import com.mobiauto.backend.application.dtos.Oportunidade.CreateOportunidadeDTO;
import com.mobiauto.backend.application.dtos.Oportunidade.OportunidadeDTO;
import com.mobiauto.backend.application.dtos.Oportunidade.UpdateOportunidadeDTO;
import com.mobiauto.backend.domain.models.Cliente;
import com.mobiauto.backend.domain.models.Oportunidade;
import com.mobiauto.backend.domain.models.Revenda;
import com.mobiauto.backend.domain.models.Veiculo;
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
                oportunidade.getResponsavelAtendimento() != null ? oportunidade.getResponsavelAtendimento().getId() : null
        );
    }

    public Oportunidade toEntity(CreateOportunidadeDTO dto, Cliente cliente, Revenda revenda, Veiculo veiculo) {
        Oportunidade oportunidade = new Oportunidade();
        oportunidade.setMotivoConclusao(dto.motivoConclusao());
        oportunidade.setDataAtribuicao(dto.dataAtribuicao());
        oportunidade.setDataConclusao(dto.dataConclusao());
        oportunidade.setCliente(cliente);
        oportunidade.setRevenda(revenda);
        oportunidade.setVeiculo(veiculo);
        return oportunidade;
    }

    public void updateEntityFromDTO(UpdateOportunidadeDTO dto, Oportunidade oportunidade) {
        oportunidade.setStatus(dto.status());
        oportunidade.setMotivoConclusao(dto.motivoConclusao());
        oportunidade.setDataAtribuicao(dto.dataAtribuicao());
        oportunidade.setDataConclusao(dto.dataConclusao());
    }

    public Oportunidade toEntityFromDTO(OportunidadeDTO dto) {
        Oportunidade oportunidade = new Oportunidade();
        oportunidade.setCodigo(dto.codigo());
        oportunidade.setStatus(dto.status());
        oportunidade.setMotivoConclusao(dto.motivoConclusao());
        oportunidade.setDataAtribuicao(dto.dataAtribuicao());
        oportunidade.setDataConclusao(dto.dataConclusao());
        return oportunidade;
    }
}
