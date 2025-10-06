package br.com.codex.v1.service;

import br.com.codex.v1.domain.dto.ValeAlimentacaoRefeicaoDto;
import br.com.codex.v1.domain.repository.CadastroColaboradoresRepository;
import br.com.codex.v1.domain.repository.ValeAlimentacaoRefeicaoRepository;
import br.com.codex.v1.domain.rh.CadastroColaboradores;
import br.com.codex.v1.domain.rh.ValeAlimentacaoRefeicao;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ValeAlimentacaoRefeicaoService {

    @Autowired
    private ValeAlimentacaoRefeicaoRepository repository;

    @Autowired
    private CadastroColaboradoresRepository colaboradoresRepository;

    public ValeAlimentacaoRefeicao create(ValeAlimentacaoRefeicaoDto valeAlimentacaoRefeicaoDto) {
        ValeAlimentacaoRefeicao vale = new ValeAlimentacaoRefeicao();
        return saveValeAlimentacaoRefeicao(vale, valeAlimentacaoRefeicaoDto);
    }

    public ValeAlimentacaoRefeicao update(Long id, ValeAlimentacaoRefeicaoDto valeAlimentacaoRefeicaoDto) {
        ValeAlimentacaoRefeicao vale = findById(id);
        return saveValeAlimentacaoRefeicao(vale, valeAlimentacaoRefeicaoDto);
    }

    private ValeAlimentacaoRefeicao saveValeAlimentacaoRefeicao(ValeAlimentacaoRefeicao vale, ValeAlimentacaoRefeicaoDto dto) {
        CadastroColaboradores colaborador = colaboradoresRepository.findById(dto.getColaboradorId())
                .orElseThrow(() -> new ObjectNotFoundException("Colaborador não encontrado! Id: " + dto.getColaboradorId()));

        // Validações de negócio
        validarDadosBeneficio(dto);

        // Desativar benefício anterior se existir
        if (dto.getAtivo()) {
            Optional<ValeAlimentacaoRefeicao> beneficioAtivo = repository.findAtivoByColaboradorId(dto.getColaboradorId());
            if (beneficioAtivo.isPresent() && !beneficioAtivo.get().getId().equals(vale.getId())) {
                beneficioAtivo.get().setAtivo(false);
                repository.save(beneficioAtivo.get());
            }
        }

        // Mapear dados
        vale.setColaborador(colaborador);
        vale.setDataInicio(dto.getDataInicio());
        vale.setDataFim(dto.getDataFim());
        vale.setTipoBeneficio(dto.getTipoBeneficio());
        vale.setFormaUtilizacao(dto.getFormaUtilizacao());
        vale.setValorDiarioAlimentacao(dto.getValorDiarioAlimentacao());
        vale.setValorDiarioRefeicao(dto.getValorDiarioRefeicao());
        vale.setDiasUteis(dto.getDiasUteis());
        vale.setNumeroCartao(dto.getNumeroCartao());
        vale.setEmpresaFornecedora(dto.getEmpresaFornecedora());
        vale.setBandeiraCartao(dto.getBandeiraCartao());
        vale.setRefeitorioInterno(dto.getRefeitorioInterno());
        vale.setRestauranteConveniado(dto.getRestauranteConveniado());
        vale.setObservacoes(dto.getObservacoes());
        vale.setAtivo(dto.getAtivo());
        vale.setDataVencimentoCartao(dto.getDataVencimentoCartao());
        vale.setDataBloqueio(dto.getDataBloqueio());

        // Calcular valores totais
        vale.calcularValoresTotais();

        return repository.save(vale);
    }

    private void validarDadosBeneficio(ValeAlimentacaoRefeicaoDto dto) {
        // Validar se pelo menos um valor diário foi informado
        if (dto.getValorDiarioAlimentacao() == null && dto.getValorDiarioRefeicao() == null) {
            throw new IllegalArgumentException("Pelo menos um valor diário (alimentação ou refeição) deve ser informado");
        }

        // Validar valores baseados no tipo de benefício
        if ("ALIMENTACAO".equals(dto.getTipoBeneficio()) && dto.getValorDiarioAlimentacao() == null) {
            throw new IllegalArgumentException("Valor diário de alimentação é obrigatório para benefício do tipo ALIMENTACAO");
        }

        if ("REFEICAO".equals(dto.getTipoBeneficio()) && dto.getValorDiarioRefeicao() == null) {
            throw new IllegalArgumentException("Valor diário de refeição é obrigatório para benefício do tipo REFEICAO");
        }

        if ("AMBOS".equals(dto.getTipoBeneficio()) &&
                (dto.getValorDiarioAlimentacao() == null || dto.getValorDiarioRefeicao() == null)) {
            throw new IllegalArgumentException("Ambos os valores diários (alimentação e refeição) são obrigatórios para benefício do tipo AMBOS");
        }
    }

    public void delete(Long id) {
        ValeAlimentacaoRefeicao vale = findById(id);
        repository.delete(vale);
    }

    public ValeAlimentacaoRefeicao findById(Long id) {
        Optional<ValeAlimentacaoRefeicao> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Vale Alimentação/Refeição não encontrado! Id: " + id));
    }

    public List<ValeAlimentacaoRefeicao> findAll() {
        return repository.findAllWithColaborador();
    }

    public List<ValeAlimentacaoRefeicao> findByColaboradorId(Long colaboradorId) {
        return repository.findByColaboradorId(colaboradorId);
    }

    public List<ValeAlimentacaoRefeicao> findAtivos() {
        return repository.findByAtivoTrue();
    }

    public List<ValeAlimentacaoRefeicao> findByTipoBeneficio(String tipoBeneficio) {
        return repository.findByTipoBeneficio(tipoBeneficio);
    }

    public List<ValeAlimentacaoRefeicao> findByFormaUtilizacao(String formaUtilizacao) {
        return repository.findByFormaUtilizacao(formaUtilizacao);
    }

    public ValeAlimentacaoRefeicao findAtivoByColaboradorCpf(String cpf) {
        return repository.findAtivoByColaboradorCpf(cpf)
                .orElseThrow(() -> new ObjectNotFoundException("Vale Alimentação/Refeição ativo não encontrado para o CPF: " + cpf));
    }

    public List<ValeAlimentacaoRefeicao> findVencimentosProximos() {
        java.time.LocalDate hoje = java.time.LocalDate.now();
        java.time.LocalDate dataLimite = hoje.plusDays(30);
        return repository.findVencimentosProximos(hoje, dataLimite);
    }

    public List<ValeAlimentacaoRefeicao> findByRefeitorioInterno() {
        return repository.findByRefeitorioInternoTrue();
    }

    public List<ValeAlimentacaoRefeicao> findByRestauranteConveniado() {
        return repository.findByRestauranteConveniadoTrue();
    }

    // Métudo para desativar benefício
    public ValeAlimentacaoRefeicao desativar(Long id) {
        ValeAlimentacaoRefeicao vale = findById(id);
        vale.setAtivo(false);
        vale.setDataFim(java.time.LocalDate.now());
        return repository.save(vale);
    }

    // Métudo para reativar benefício
    public ValeAlimentacaoRefeicao reativar(Long id) {
        ValeAlimentacaoRefeicao vale = findById(id);

        // Verificar se já existe um benefício ativo para o colaborador
        Optional<ValeAlimentacaoRefeicao> beneficioAtivo = repository.findAtivoByColaboradorId(vale.getColaborador().getId());
        if (beneficioAtivo.isPresent() && !beneficioAtivo.get().getId().equals(id)) {
            throw new IllegalStateException("Já existe um benefício ativo para este colaborador. Desative-o primeiro.");
        }

        vale.setAtivo(true);
        vale.setDataFim(null);
        return repository.save(vale);
    }
}
