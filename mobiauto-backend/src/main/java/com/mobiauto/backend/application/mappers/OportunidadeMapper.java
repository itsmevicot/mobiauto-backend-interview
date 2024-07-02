package com.mobiauto.backend.application.mappers;

import com.mobiauto.backend.application.dtos.Oportunidade.OportunidadeDTO;
import com.mobiauto.backend.application.dtos.Oportunidade.CreateOportunidadeDTO;
import com.mobiauto.backend.application.dtos.Oportunidade.UpdateOportunidadeDTO;
import com.mobiauto.backend.domain.exceptions.Cliente.ClienteNotFoundException;
import com.mobiauto.backend.domain.exceptions.Revenda.RevendaNotFoundException;
import com.mobiauto.backend.domain.exceptions.Usuario.UsuarioNotFoundException;
import com.mobiauto.backend.domain.exceptions.Veiculo.VeiculoNotFoundException;
import com.mobiauto.backend.domain.models.Oportunidade;
import com.mobiauto.backend.domain.models.Cliente;
import com.mobiauto.backend.domain.models.Revenda;
import com.mobiauto.backend.domain.models.Veiculo;
import com.mobiauto.backend.domain.models.Usuario;
import com.mobiauto.backend.domain.repositories.ClienteRepository;
import com.mobiauto.backend.domain.repositories.RevendaRepository;
import com.mobiauto.backend.domain.repositories.UsuarioRepository;
import com.mobiauto.backend.domain.repositories.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OportunidadeMapper {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private RevendaRepository revendaRepository;

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

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

    public Oportunidade toEntityFromDTO(OportunidadeDTO dto) {
        Oportunidade oportunidade = new Oportunidade();
        oportunidade.setCodigo(dto.codigo());
        oportunidade.setStatus(dto.status());
        oportunidade.setMotivoConclusao(dto.motivoConclusao());
        oportunidade.setDataAtribuicao(dto.dataAtribuicao());
        oportunidade.setDataConclusao(dto.dataConclusao());

        Cliente cliente = clienteRepository.findById(dto.clienteId())
                .orElseThrow(ClienteNotFoundException::new);
        Revenda revenda = revendaRepository.findById(dto.revendaId())
                .orElseThrow(RevendaNotFoundException::new);
        Veiculo veiculo = veiculoRepository.findById(dto.veiculoId())
                .orElseThrow(VeiculoNotFoundException::new);
        Usuario responsavelAtendimento = usuarioRepository.findById(dto.responsavelAtendimentoId())
                .orElseThrow(UsuarioNotFoundException::new);

        oportunidade.setCliente(cliente);
        oportunidade.setRevenda(revenda);
        oportunidade.setVeiculo(veiculo);
        oportunidade.setResponsavelAtendimento(responsavelAtendimento);

        return oportunidade;
    }

    public void updateEntityFromDTO(UpdateOportunidadeDTO dto, Oportunidade oportunidade) {
        oportunidade.setStatus(dto.status());
        oportunidade.setMotivoConclusao(dto.motivoConclusao());
        oportunidade.setDataAtribuicao(dto.dataAtribuicao());
        oportunidade.setDataConclusao(dto.dataConclusao());
    }
}
