package br.com.codexloja.v1.service;

import br.com.codexloja.v1.domain.contabilidade.HistoricoPadrao;
import br.com.codexloja.v1.domain.dto.HistoricoPadraoDto;
import br.com.codexloja.v1.domain.repository.HistoricoPadraoRepository;
import br.com.codexloja.v1.service.exceptions.ObjectNotFoundException;
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

    public HistoricoPadrao update(Integer id, HistoricoPadraoDto historicoPadraoDto) {
        historicoPadraoDto.setId(id);
        HistoricoPadrao obj = findById(id);
        obj = new HistoricoPadrao(historicoPadraoDto);
        return historicoPadraoRepository.save(obj);
    }

    public HistoricoPadrao findById(Integer id) {
        Optional<HistoricoPadrao> obj = historicoPadraoRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Histórico Padrão não encontrado"));
    }

    public void delete(Integer id) {
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
}
