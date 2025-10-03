package br.com.codex.v1.service;

import br.com.codex.v1.domain.dto.ValeTransporteDto;
import br.com.codex.v1.domain.repository.CadastroColaboradoresRepository;
import br.com.codex.v1.domain.repository.ValeTransporteRepository;
import br.com.codex.v1.domain.rh.CadastroColaboradores;
import br.com.codex.v1.domain.rh.ValeTransporte;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ValeTransporteService {

    @Autowired
    private ValeTransporteRepository repository;

    @Autowired
    private CadastroColaboradoresRepository colaboradoresRepository;

    public ValeTransporte create(ValeTransporteDto valeTransporteDto) {
        ValeTransporte valeTransporte = new ValeTransporte();
        return saveValeTransporte(valeTransporte, valeTransporteDto);
    }

    public ValeTransporte update(Long id, ValeTransporteDto valeTransporteDto) {
        ValeTransporte valeTransporte = findById(id);
        return saveValeTransporte(valeTransporte, valeTransporteDto);
    }

    private ValeTransporte saveValeTransporte(ValeTransporte valeTransporte, ValeTransporteDto dto) {
        CadastroColaboradores colaborador = colaboradoresRepository.findById(dto.getColaboradorId())
                .orElseThrow(() -> new ObjectNotFoundException("Colaborador não encontrado! Id: " + dto.getColaboradorId()));

        // Desativa vale transporte anterior se existir
        if (dto.getAtivo()) {
            Optional<ValeTransporte> valeAtivo = repository.findAtivoByColaboradorId(dto.getColaboradorId());
            if (valeAtivo.isPresent() && !valeAtivo.get().getId().equals(valeTransporte.getId())) {
                valeAtivo.get().setAtivo(false);
                repository.save(valeAtivo.get());
            }
        }

        valeTransporte.setColaborador(colaborador);
        valeTransporte.setDataInicio(dto.getDataInicio());
        valeTransporte.setDataFim(dto.getDataFim());
        valeTransporte.setValorDiario(dto.getValorDiario());
        valeTransporte.setDiasUtilizados(dto.getDiasUtilizados());
        valeTransporte.setValorTotal(dto.getValorTotal());
        valeTransporte.setTipoVale(dto.getTipoVale());
        valeTransporte.setNumeroCartao(dto.getNumeroCartao());
        valeTransporte.setEmpresaTransporte(dto.getEmpresaTransporte());
        valeTransporte.setObservacao(dto.getObservacao());
        valeTransporte.setAtivo(dto.getAtivo());

        return repository.save(valeTransporte);
    }

    public void delete(Long id) {
        ValeTransporte valeTransporte = findById(id);
        repository.delete(valeTransporte);
    }

    public ValeTransporte findById(Long id) {
        Optional<ValeTransporte> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Vale Transporte não encontrado! Id: " + id));
    }

    public List<ValeTransporte> findAll() {
        return repository.findAll();
    }

    public List<ValeTransporte> findByColaboradorId(Long colaboradorId) {
        return repository.findByColaboradorId(colaboradorId);
    }

    public List<ValeTransporte> findAtivos() {
        return repository.findByAtivoTrue();
    }

    public ValeTransporte findAtivoByColaboradorCpf(String cpf) {
        return repository.findByColaboradorCpfAndAtivo(cpf)
                .orElseThrow(() -> new ObjectNotFoundException("Vale Transporte ativo não encontrado para o CPF: " + cpf));
    }
}
