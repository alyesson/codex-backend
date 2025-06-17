package br.com.codex.v1.service;

import br.com.codex.v1.domain.contabilidade.HistoricoPadrao;
import br.com.codex.v1.domain.dto.HistoricoPadraoDto;
import br.com.codex.v1.domain.repository.HistoricoPadraoRepository;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HistoricoPadraoService {
    
    @Autowired
    private HistoricoPadraoRepository historicoPadraoRepository;

    public HistoricoPadrao create(HistoricoPadraoDto historicoPadraoDto) {
        historicoPadraoDto.setId(null);
        validaHistoricoPadrao(historicoPadraoDto);
        HistoricoPadrao historicoPadrao = new HistoricoPadrao(historicoPadraoDto);
        return historicoPadraoRepository.save(historicoPadrao);
    }

    public HistoricoPadrao update(Long id, HistoricoPadraoDto historicoPadraoDto) {
        historicoPadraoDto.setId(id);
        HistoricoPadrao obj = findById(id);
        obj = new HistoricoPadrao(historicoPadraoDto);
        return historicoPadraoRepository.save(obj);
    }

    public HistoricoPadrao findById(Long id) {
        Optional<HistoricoPadrao> obj = historicoPadraoRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Histórico Padrão não encontrado"));
    }

    public void delete(Long id) {
        historicoPadraoRepository.deleteById(id);
    }

    public List<HistoricoPadrao> findAll(){
        return historicoPadraoRepository.findAll();
    }

    private void validaHistoricoPadrao(HistoricoPadraoDto historicoPadraoDto){
        Optional<HistoricoPadrao> obj = historicoPadraoRepository.findByCodigo(historicoPadraoDto.getCodigo());
        if(obj.isPresent() && obj.get().getCodigo().equals(historicoPadraoDto.getCodigo())){
            throw new DataIntegrityViolationException("Este histórico padrão já existe");
        }
    }

    public HistoricoPadrao findByDescricao(String descricao) {
        return historicoPadraoRepository.findByDescricao(descricao)
                .orElseGet(() -> {
                    List<HistoricoPadrao> similares = historicoPadraoRepository.findByDescricaoIgnoreCaseContaining(descricao);
                    if (!similares.isEmpty()) {
                        return similares.get(0); // usa o primeiro histórico similar encontrado
                    }
                    throw new ObjectNotFoundException("Não foi encontrado um histórico padrão com a descrição ou semelhante a: " + descricao);
                });
    }
}
