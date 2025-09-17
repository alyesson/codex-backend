package br.com.codex.v1.service;

import br.com.codex.v1.domain.dto.NotaFiscalServicoDto;
import br.com.codex.v1.domain.fiscal.NotaFiscalServico;
import br.com.codex.v1.domain.repository.NotaFiscalServicoRepository;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class NotaFiscalServicoService {

    @Autowired
    private NotaFiscalServicoRepository repository;

    public NotaFiscalServico create(NotaFiscalServicoDto dto) {
        NotaFiscalServico obj = new NotaFiscalServico();
        mapDtoToEntity(dto, obj);
        return repository.save(obj);
    }

    public void delete(Long id) {
        NotaFiscalServico obj = findById(id);
        repository.delete(obj);
    }

    public NotaFiscalServico findById(Long id) {
        Optional<NotaFiscalServico> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Nota Fiscal de Serviço não encontrada! Id: " + id + ", Tipo: " + NotaFiscalServico.class.getName()));
    }

    public List<NotaFiscalServico> findByYear(Integer year) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);
        return repository.findByCompetenciaBetween(startDate, endDate);
    }

    public List<NotaFiscalServico> findByPeriodo(LocalDate dataInicial, LocalDate dataFinal) {
        return repository.findByCompetenciaBetween(dataInicial, dataFinal);
    }

    private void mapDtoToEntity(NotaFiscalServicoDto dto, NotaFiscalServico obj) {
        obj.setDataEmissao(dto.getDataEmissao());
        obj.setCompetencia(dto.getCompetencia());
        obj.setCodigoVerificacao(dto.getCodigoVerificacao());
        obj.setNumeroNfse(dto.getNumeroNfse());
        obj.setNumeroRps(dto.getNumeroRps());
        obj.setNumeroNfseSubstituida(dto.getNumeroNfseSubstituida());
        obj.setLocalPrestacao(dto.getLocalPrestacao());

        // Mapear dados do prestador
        obj.setPrestadorRazaoSocial(dto.getPrestadorRazaoSocial());
        obj.setPrestadorNomeFantasia(dto.getPrestadorNomeFantasia());
        obj.setPrestadorCnpjCpf(dto.getPrestadorCnpjCpf());
        obj.setPrestadorInscricaoMunicipal(dto.getPrestadorInscricaoMunicipal());
        obj.setPrestadorMunicipio(dto.getPrestadorMunicipio());
        obj.setPrestadorEndereco(dto.getPrestadorEndereco());
        obj.setPrestadorComplemento(dto.getPrestadorComplemento());
        obj.setPrestadorTelefone(dto.getPrestadorTelefone());
        obj.setPrestadorEmail(dto.getPrestadorEmail());

        // Mapear dados do tomador
        obj.setTomadorRazaoSocial(dto.getTomadorRazaoSocial());
        obj.setTomadorCnpjCpf(dto.getTomadorCnpjCpf());
        obj.setTomadorInscricaoMunicipal(dto.getTomadorInscricaoMunicipal());
        obj.setTomadorMunicipio(dto.getTomadorMunicipio());
        obj.setTomadorEndereco(dto.getTomadorEndereco());
        obj.setTomadorComplemento(dto.getTomadorComplemento());
        obj.setTomadorTelefone(dto.getTomadorTelefone());
        obj.setTomadorEmail(dto.getTomadorEmail());

        // Mapear dados do serviço
        obj.setDiscriminacaoServicos(dto.getDiscriminacaoServicos());
        obj.setCodigoServico(dto.getCodigoServico());
        obj.setDescricaoServico(dto.getDescricaoServico());
        obj.setDetalhamentoConstrucaoCivil(dto.getDetalhamentoConstrucaoCivil());

        // Mapear valores e tributos
        obj.setValorServicos(dto.getValorServicos());
        obj.setDescontoIncondicionado(dto.getDescontoIncondicionado());
        obj.setDescontoCondicionado(dto.getDescontoCondicionado());
        obj.setRetencoesFederais(dto.getRetencoesFederais());
        obj.setOutrasRetencoes(dto.getOutrasRetencoes());
        obj.setIssRetido(dto.getIssRetido());
        obj.setValorLiquido(dto.getValorLiquido());
        obj.setNaturezaOperacao(dto.getNaturezaOperacao());
        obj.setRegimeEspecialTributacao(dto.getRegimeEspecialTributacao());
        obj.setOpcaoSimplesNacional(dto.getOpcaoSimplesNacional());
        obj.setIncentivadorCultural(dto.getIncentivadorCultural());
        obj.setDeducoesPermitidas(dto.getDeducoesPermitidas());
        obj.setBaseCalculo(dto.getBaseCalculo());
        obj.setAliquota(dto.getAliquota());
        obj.setValorIss(dto.getValorIss());
        obj.setIssReter(dto.getIssReter());
    }
}